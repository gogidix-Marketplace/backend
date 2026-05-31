package com.gogidix.shared.infrastructure.services.security.management.infrastructure.sharedinfrastructure.securitymanagementservice.service;

import com.gogidix.shared.infrastructure.services.security.management.infrastructure.sharedinfrastructure.securitymanagementservice.entity.SecurityPolicy;
import com.gogidix.shared.infrastructure.services.security.management.infrastructure.sharedinfrastructure.securitymanagementservice.repository.SecurityPolicyMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SecurityPolicyService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityPolicyService.class);

    @Autowired
    private SecurityPolicyMongoRepository securityPolicyRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${security.management.policies.enabled:true}")
    private boolean policiesEnabled;

    @Value("${security.management.policies.enforcement-mode:STRICT}")
    private String defaultEnforcementMode;

    @Value("${security.management.policies.auto-update-enabled:true}")
    private boolean autoUpdateEnabled;

    private String getCurrentTenantId() {
        return "default-tenant"; // TODO: Implement proper tenant resolution
    }

    @Transactional
    @CacheEvict(value = "security-policies", allEntries = true)
    public SecurityPolicy createSecurityPolicy(SecurityPolicy policy) {
        if (!policiesEnabled) {
            throw new IllegalStateException("Security policies are disabled");
        }

        logger.info("Creating security policy: {} by user: {}", policy.getPolicyName(), policy.getCreatedBy());

        validateSecurityPolicy(policy);
        
        // Set default enforcement mode if not specified
        if (policy.getEnforcementMode() == null) {
            policy.setEnforcementMode(SecurityPolicy.EnforcementMode.valueOf(defaultEnforcementMode));
        }

        SecurityPolicy savedPolicy = securityPolicyRepository.save(policy);
        
        // Send policy creation event
        sendPolicyEvent("POLICY_CREATED", savedPolicy);
        
        logger.info("Created security policy with ID: {}", savedPolicy.getId());
        return savedPolicy;
    }

    @Transactional
    @CacheEvict(value = "security-policies", allEntries = true)
    public SecurityPolicy updateSecurityPolicy(String policyId, SecurityPolicy updatedPolicy, String modifiedBy) {
        logger.info("Updating security policy with ID: {} by user: {}", policyId, modifiedBy);

        SecurityPolicy existing = securityPolicyRepository.findById(policyId)
            .orElseThrow(() -> new IllegalArgumentException("Security policy not found: " + policyId));

        validateSecurityPolicy(updatedPolicy);

        // Create new version
        existing.updateVersion(modifiedBy, updatedPolicy.getChangeReason());
        
        // Update fields
        existing.setPolicyName(updatedPolicy.getPolicyName());
        existing.setDescription(updatedPolicy.getDescription());
        existing.setPolicyType(updatedPolicy.getPolicyType());
        existing.setPolicyCategory(updatedPolicy.getPolicyCategory());
        existing.setPriorityLevel(updatedPolicy.getPriorityLevel());
        existing.setDomainScope(updatedPolicy.getDomainScope());
        existing.setApplicableServices(updatedPolicy.getApplicableServices());
        existing.setApplicableRoles(updatedPolicy.getApplicableRoles());
        existing.setPolicyRules(updatedPolicy.getPolicyRules());
        existing.setEnforcementActions(updatedPolicy.getEnforcementActions());
        existing.setEnforcementMode(updatedPolicy.getEnforcementMode());
        existing.setEffectiveDate(updatedPolicy.getEffectiveDate());
        existing.setExpiryDate(updatedPolicy.getExpiryDate());
        existing.setComplianceMapping(updatedPolicy.getComplianceMapping());
        existing.setConditions(updatedPolicy.getConditions());
        existing.setExceptions(updatedPolicy.getExceptions());
        existing.setMetadata(updatedPolicy.getMetadata());
        existing.setAutoRemediationEnabled(updatedPolicy.getAutoRemediationEnabled());
        existing.setNotificationEnabled(updatedPolicy.getNotificationEnabled());
        existing.setLoggingEnabled(updatedPolicy.getLoggingEnabled());
        existing.setMonitoringEnabled(updatedPolicy.getMonitoringEnabled());

        SecurityPolicy savedPolicy = securityPolicyRepository.save(existing);
        
        // Send policy update event
        sendPolicyEvent("POLICY_UPDATED", savedPolicy);
        
        logger.info("Updated security policy: {}", savedPolicy.getPolicyName());
        return savedPolicy;
    }

    @Transactional
    @CacheEvict(value = "security-policies", allEntries = true)
    public SecurityPolicy approveSecurityPolicy(String policyId, String approvedBy) {
        logger.info("Approving security policy with ID: {} by user: {}", policyId, approvedBy);

        SecurityPolicy policy = securityPolicyRepository.findById(policyId)
            .orElseThrow(() -> new IllegalArgumentException("Security policy not found: " + policyId));

        if (!policy.requiresApproval()) {
            throw new IllegalStateException("Policy does not require approval");
        }

        policy.approve(approvedBy);
        SecurityPolicy savedPolicy = securityPolicyRepository.save(policy);
        
        // Send policy approval event
        sendPolicyEvent("POLICY_APPROVED", savedPolicy);
        
        logger.info("Approved security policy: {}", savedPolicy.getPolicyName());
        return savedPolicy;
    }

    @Transactional
    @CacheEvict(value = "security-policies", allEntries = true)
    public SecurityPolicy activateSecurityPolicy(String policyId) {
        logger.info("Activating security policy with ID: {}", policyId);

        SecurityPolicy policy = securityPolicyRepository.findById(policyId)
            .orElseThrow(() -> new IllegalArgumentException("Security policy not found: " + policyId));

        policy.activate();
        SecurityPolicy savedPolicy = securityPolicyRepository.save(policy);
        
        // Send policy activation event
        sendPolicyEvent("POLICY_ACTIVATED", savedPolicy);
        
        logger.info("Activated security policy: {}", savedPolicy.getPolicyName());
        return savedPolicy;
    }

    @Transactional
    @CacheEvict(value = "security-policies", allEntries = true)
    public SecurityPolicy deactivateSecurityPolicy(String policyId, String reason) {
        logger.info("Deactivating security policy with ID: {}", policyId);

        SecurityPolicy policy = securityPolicyRepository.findById(policyId)
            .orElseThrow(() -> new IllegalArgumentException("Security policy not found: " + policyId));

        policy.deactivate();
        policy.setChangeReason(reason);
        SecurityPolicy savedPolicy = securityPolicyRepository.save(policy);
        
        // Send policy deactivation event
        sendPolicyEvent("POLICY_DEACTIVATED", savedPolicy);
        
        logger.info("Deactivated security policy: {}", savedPolicy.getPolicyName());
        return savedPolicy;
    }

    @Transactional(readOnly = true)
    @Cacheable("security-policies")
    public Page<SecurityPolicy> getAllSecurityPolicies(Pageable pageable) {
        return securityPolicyRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Cacheable("security-policies")
    public List<SecurityPolicy> getActivePolicies() {
        return securityPolicyRepository.findByIsActiveTrueAndPolicyStatus(SecurityPolicy.PolicyStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    @Cacheable("security-policies")
    public List<SecurityPolicy> getPoliciesByType(SecurityPolicy.PolicyType policyType) {
        return securityPolicyRepository.findByPolicyTypeAndIsActiveTrue(policyType);
    }

    @Transactional(readOnly = true)
    @Cacheable("security-policies")
    public List<SecurityPolicy> getPoliciesForService(String serviceName) {
        return securityPolicyRepository.findApplicablePoliciesForService(serviceName, getCurrentTenantId());
    }

    @Transactional(readOnly = true)
    @Cacheable("security-policies")
    public List<SecurityPolicy> getPoliciesForRole(String roleName) {
        return securityPolicyRepository.findApplicablePoliciesForRole(roleName, getCurrentTenantId());
    }

    @Transactional(readOnly = true)
    public Optional<SecurityPolicy> getSecurityPolicyById(String id) {
        return securityPolicyRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<SecurityPolicy> getCriticalPolicies() {
        return securityPolicyRepository.findByPriorityLevelAndIsActiveTrue(SecurityPolicy.PriorityLevel.CRITICAL);
    }

    @Transactional(readOnly = true)
    public List<SecurityPolicy> getExpiringPolicies(int daysFromNow) {
        LocalDateTime expiryThreshold = LocalDateTime.now().plusDays(daysFromNow);
        LocalDateTime currentDate = LocalDateTime.now();
        return securityPolicyRepository.findExpiringPolicies(expiryThreshold, currentDate, getCurrentTenantId());
    }

    @Transactional
    public SecurityPolicy recordPolicyViolation(String policyId, String violationType, String details) {
        SecurityPolicy policy = securityPolicyRepository.findById(policyId)
            .orElseThrow(() -> new IllegalArgumentException("Security policy not found: " + policyId));

        policy.incrementViolation();
        SecurityPolicy savedPolicy = securityPolicyRepository.save(policy);

        // Send policy violation event
        Map<String, Object> violationEvent = new HashMap<>();
        violationEvent.put("eventType", "POLICY_VIOLATION");
        violationEvent.put("policyId", policyId);
        violationEvent.put("policyName", policy.getPolicyName());
        violationEvent.put("violationType", violationType);
        violationEvent.put("details", details);
        violationEvent.put("timestamp", LocalDateTime.now());
        violationEvent.put("severity", policy.getPriorityLevel().toString());

        kafkaTemplate.send("security-violations", policyId.toString(), violationEvent);

        logger.warn("Policy violation recorded for policy: {} - Type: {}", policy.getPolicyName(), violationType);
        return savedPolicy;
    }

    @Transactional
    public SecurityPolicy recordPolicyExecution(String policyId, boolean successful) {
        SecurityPolicy policy = securityPolicyRepository.findById(policyId)
            .orElseThrow(() -> new IllegalArgumentException("Security policy not found: " + policyId));

        policy.incrementExecution();
        if (!successful) {
            policy.incrementViolation();
        }

        return securityPolicyRepository.save(policy);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> generatePolicyComplianceReport() {
        Map<String, Object> report = new HashMap<>();

        long totalPolicies = securityPolicyRepository.count();
        long activePolicies = securityPolicyRepository.countByIsActiveTrueAndPolicyStatus(SecurityPolicy.PolicyStatus.ACTIVE);
        long expiredPolicies = securityPolicyRepository.countExpiredPolicies(LocalDateTime.now());
        long criticalPolicies = securityPolicyRepository.countByPriorityLevelAndIsActiveTrue(SecurityPolicy.PriorityLevel.CRITICAL);

        report.put("totalPolicies", totalPolicies);
        report.put("activePolicies", activePolicies);
        report.put("expiredPolicies", expiredPolicies);
        report.put("criticalPolicies", criticalPolicies);

        // Policy types breakdown
        Map<String, Long> policyTypes = new HashMap<>();
        for (SecurityPolicy.PolicyType type : SecurityPolicy.PolicyType.values()) {
            long count = securityPolicyRepository.countByPolicyTypeAndIsActiveTrue(type);
            policyTypes.put(type.toString(), count);
        }
        report.put("policyTypeBreakdown", policyTypes);

        // Compliance mapping
        List<SecurityPolicy> complianceStats = securityPolicyRepository.getComplianceStatistics(getCurrentTenantId());
        Map<String, Long> complianceBreakdown = new HashMap<>();
        for (SecurityPolicy policy : complianceStats) {
            if (policy.getComplianceMapping() != null) {
                complianceBreakdown.put(policy.getPolicyType().toString(), policy.getViolationCount());
            }
        }
        report.put("complianceBreakdown", complianceBreakdown);

        // Violation statistics
        List<SecurityPolicy> policiesWithViolations = securityPolicyRepository.findPoliciesWithViolations(getCurrentTenantId());
        long totalViolations = policiesWithViolations.stream()
            .mapToLong(SecurityPolicy::getViolationCount)
            .sum();
        report.put("totalViolations", totalViolations);
        report.put("policiesWithViolations", policiesWithViolations.size());

        // Top violated policies
        List<SecurityPolicy> topViolatedPolicies = policiesWithViolations.stream()
            .sorted((p1, p2) -> Long.compare(p2.getViolationCount(), p1.getViolationCount()))
            .limit(10)
            .collect(Collectors.toList());
        report.put("topViolatedPolicies", topViolatedPolicies);

        report.put("generatedAt", LocalDateTime.now());
        report.put("complianceScore", calculateOverallComplianceScore());

        return report;
    }

    @Scheduled(cron = "0 0 1 * * ?") // Daily at 1 AM
    @Async
    public void performPolicyMaintenance() {
        logger.info("Starting scheduled policy maintenance");

        try {
            // Check for expired policies
            List<SecurityPolicy> expiredPolicies = securityPolicyRepository.findExpiredPolicies(LocalDateTime.now());
            for (SecurityPolicy policy : expiredPolicies) {
                if (policy.getIsActive()) {
                    policy.deactivate();
                    securityPolicyRepository.save(policy);
                    sendPolicyEvent("POLICY_EXPIRED", policy);
                    logger.info("Deactivated expired policy: {}", policy.getPolicyName());
                }
            }

            // Check for policies requiring renewal
            List<SecurityPolicy> expiringPolicies = getExpiringPolicies(30); // 30 days warning
            for (SecurityPolicy policy : expiringPolicies) {
                sendPolicyRenewalNotification(policy);
            }

            // Update policy statistics
            if (autoUpdateEnabled) {
                updatePolicyStatistics();
            }

            logger.info("Completed scheduled policy maintenance");

        } catch (Exception e) {
            logger.error("Error during policy maintenance: {}", e.getMessage(), e);
        }
    }

    @Async
    private void sendPolicyEvent(String eventType, SecurityPolicy policy) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", eventType);
            event.put("policyId", policy.getId());
            event.put("policyName", policy.getPolicyName());
            event.put("policyType", policy.getPolicyType().toString());
            event.put("priorityLevel", policy.getPriorityLevel().toString());
            event.put("enforcementMode", policy.getEnforcementMode().toString());
            event.put("timestamp", LocalDateTime.now());

            kafkaTemplate.send("security-policy-events", policy.getId().toString(), event);
            logger.debug("Sent policy event: {} for policy: {}", eventType, policy.getPolicyName());

        } catch (Exception e) {
            logger.error("Failed to send policy event: {}", e.getMessage(), e);
        }
    }

    private void sendPolicyRenewalNotification(SecurityPolicy policy) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("notificationType", "POLICY_RENEWAL_REQUIRED");
        notification.put("policyId", policy.getId());
        notification.put("policyName", policy.getPolicyName());
        notification.put("expiryDate", policy.getExpiryDate());
        notification.put("daysUntilExpiry", java.time.Duration.between(LocalDateTime.now(), policy.getExpiryDate()).toDays());
        notification.put("createdBy", policy.getCreatedBy());
        notification.put("timestamp", LocalDateTime.now());

        kafkaTemplate.send("policy-renewal-notifications", policy.getId().toString(), notification);
        logger.info("Sent renewal notification for policy: {}", policy.getPolicyName());
    }

    private void updatePolicyStatistics() {
        List<SecurityPolicy> allPolicies = securityPolicyRepository.findAll();
        for (SecurityPolicy policy : allPolicies) {
            // Update success rate and other calculated fields
            // This would typically involve querying execution logs
            securityPolicyRepository.save(policy);
        }
        logger.debug("Updated statistics for {} policies", allPolicies.size());
    }

    private double calculateOverallComplianceScore() {
        List<SecurityPolicy> activePolicies = getActivePolicies();
        if (activePolicies.isEmpty()) {
            return 100.0;
        }

        double totalScore = activePolicies.stream()
            .mapToDouble(SecurityPolicy::getSuccessRate)
            .average()
            .orElse(100.0);

        return Math.round(totalScore * 100.0) / 100.0;
    }

    private void validateSecurityPolicy(SecurityPolicy policy) {
        if (policy.getPolicyName() == null || policy.getPolicyName().trim().isEmpty()) {
            throw new IllegalArgumentException("Policy name is required");
        }

        if (policy.getPolicyType() == null) {
            throw new IllegalArgumentException("Policy type is required");
        }

        if (policy.getPolicyCategory() == null) {
            throw new IllegalArgumentException("Policy category is required");
        }

        if (policy.getPriorityLevel() == null) {
            throw new IllegalArgumentException("Priority level is required");
        }

        if (policy.getExpiryDate() != null && policy.getEffectiveDate() != null &&
            policy.getExpiryDate().isBefore(policy.getEffectiveDate())) {
            throw new IllegalArgumentException("Expiry date cannot be before effective date");
        }

        // Check for duplicate policy names
        SecurityPolicy existing = securityPolicyRepository.findByPolicyNameAndTenantId(policy.getPolicyName(), getCurrentTenantId());
        if (existing != null && !existing.getId().equals(policy.getId())) {
            throw new IllegalArgumentException("Policy with name '" + policy.getPolicyName() + "' already exists");
        }
    }
}
