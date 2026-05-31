package com.gogidix.shared.audit.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Clean Domain Entity for Audit Events - ZERO CONTAMINATION
 * 
 * Enterprise-grade audit tracking domain model with comprehensive business logic.
 * Implements hexagonal architecture principles with zero external dependencies.
 * 
 * Key Business Features:
 * - Comprehensive audit trail management
 * - Enterprise compliance tracking (GDPR, SOX, PCI-DSS, HIPAA)
 * - Real-time risk assessment and scoring
 * - Automated threat detection and escalation
 * - Financial transaction audit with money laundering detection
 * - Multi-tenant security isolation
 * - Performance monitoring and SLA tracking
 * - Regulatory reporting and evidence collection
 * 
 * @version 2.0.0
 * @since 2024-01-01
 */
public class AuditEventClean {
    
    // Core Event Identification
    private final UUID id;
    private final String eventId;
    private final String correlationId;
    private final String causationId;
    private final LocalDateTime timestamp;
    private final String version;
    
    // User and Session Context
    private final String userId;
    private final String sessionId;
    private final String tenantId;
    private final String organizationId;
    private final String userRole;
    private final Set<String> userPermissions;
    
    // Event Classification
    private final AuditEventTypeClean eventType;
    private final BusinessDomainClean businessDomain;
    private final String action;
    private final String subAction;
    private final AuditResultClean result;
    private final AuditSeverityClean severity;
    
    // Resource Context
    private final String resourceType;
    private final String resourceId;
    private final String resourceName;
    private final String parentResourceId;
    private final Map<String, Object> resourceMetadata;
    
    // Technical Context  
    private final String sourceSystem;
    private final String serviceVersion;
    private final String ipAddress;
    private final String userAgent;
    private final String requestMethod;
    private final String requestUri;
    private final String geographicLocation;
    private final String deviceFingerprint;
    
    // Business Context
    private final String businessProcess;
    private final String workflowId;
    private final String transactionId;
    private final BigDecimal financialAmount;
    private final String currency;
    private final String paymentMethod;
    private final String vendorId;
    private final String customerId;
    
    // Compliance and Security
    private final ComplianceTypeClean complianceType;
    private final Set<String> regulatoryRequirements;
    private final SecurityClassificationClean securityClassification;
    private final RiskLevelClean riskLevel;
    private final Integer riskScore;
    private final Boolean containsPII;
    private final Boolean containsPCI;
    private final Boolean containsPHI;
    
    // Performance and Quality
    private final Long processingTimeMs;
    private final Long responseTimeMs;
    private final String performanceTier;
    private final Double cpuUtilization;
    private final Double memoryUtilization;
    private final Integer errorCount;
    private final String errorCode;
    private final String errorMessage;
    
    // Evidence and Context
    private final String description;
    private final String businessReason;
    private final Map<String, Object> beforeState;
    private final Map<String, Object> afterState;
    private final Map<String, Object> additionalMetadata;
    private final List<String> evidenceFiles;
    private final String legalHold;
    
    // Lifecycle and Retention
    private final LocalDateTime retentionExpiryDate;
    private final Boolean isArchived;
    private final String archiveLocation;
    private final Integer hashCode;
    private final String digitalSignature;
    private final Boolean isImmutable;
    
    public AuditEventClean(UUID id, String eventId, String userId, String tenantId, 
                          AuditEventTypeClean eventType, BusinessDomainClean businessDomain, 
                          String action, AuditResultClean result) {
        this.id = Objects.requireNonNull(id, "Audit event ID cannot be null");
        this.eventId = Objects.requireNonNull(eventId, "Event ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "Tenant ID cannot be null");
        this.eventType = Objects.requireNonNull(eventType, "Event type cannot be null");
        this.businessDomain = Objects.requireNonNull(businessDomain, "Business domain cannot be null");
        this.action = Objects.requireNonNull(action, "Action cannot be null");
        this.result = Objects.requireNonNull(result, "Result cannot be null");
        
        // Set defaults for required fields
        this.timestamp = LocalDateTime.now();
        this.version = "2.0.0";
        this.correlationId = UUID.randomUUID().toString();
        this.causationId = null;
        this.sessionId = null;
        this.organizationId = null;
        this.userRole = null;
        this.userPermissions = Collections.emptySet();
        this.subAction = null;
        this.severity = calculateDefaultSeverity();
        this.resourceType = null;
        this.resourceId = null;
        this.resourceName = null;
        this.parentResourceId = null;
        this.resourceMetadata = new HashMap<>();
        this.sourceSystem = "gogidix-audit-system";
        this.serviceVersion = "1.0.0";
        this.ipAddress = null;
        this.userAgent = null;
        this.requestMethod = null;
        this.requestUri = null;
        this.geographicLocation = null;
        this.deviceFingerprint = null;
        this.businessProcess = null;
        this.workflowId = null;
        this.transactionId = null;
        this.financialAmount = null;
        this.currency = null;
        this.paymentMethod = null;
        this.vendorId = null;
        this.customerId = null;
        this.complianceType = ComplianceTypeClean.STANDARD;
        this.regulatoryRequirements = Collections.emptySet();
        this.securityClassification = SecurityClassificationClean.PUBLIC;
        this.riskLevel = calculateDefaultRiskLevel();
        this.riskScore = calculateRiskScore();
        this.containsPII = false;
        this.containsPCI = false;
        this.containsPHI = false;
        this.processingTimeMs = null;
        this.responseTimeMs = null;
        this.performanceTier = "STANDARD";
        this.cpuUtilization = null;
        this.memoryUtilization = null;
        this.errorCount = 0;
        this.errorCode = null;
        this.errorMessage = null;
        this.description = null;
        this.businessReason = null;
        this.beforeState = new HashMap<>();
        this.afterState = new HashMap<>();
        this.additionalMetadata = new HashMap<>();
        this.evidenceFiles = Collections.emptyList();
        this.legalHold = null;
        this.retentionExpiryDate = calculateRetentionExpiryDate();
        this.isArchived = false;
        this.archiveLocation = null;
        this.hashCode = Objects.hash(id, eventId, userId, timestamp);
        this.digitalSignature = generateDigitalSignature();
        this.isImmutable = true;
        
        validateBusinessRules();
    }
    
    // Full constructor for complete control
    public AuditEventClean(UUID id, String eventId, String correlationId, String causationId, 
                          LocalDateTime timestamp, String version, String userId, String sessionId, 
                          String tenantId, String organizationId, String userRole, Set<String> userPermissions,
                          AuditEventTypeClean eventType, BusinessDomainClean businessDomain, String action, 
                          String subAction, AuditResultClean result, AuditSeverityClean severity,
                          String resourceType, String resourceId, String resourceName, String parentResourceId,
                          Map<String, Object> resourceMetadata, String sourceSystem, String serviceVersion,
                          String ipAddress, String userAgent, String requestMethod, String requestUri,
                          String geographicLocation, String deviceFingerprint, String businessProcess,
                          String workflowId, String transactionId, BigDecimal financialAmount, String currency,
                          String paymentMethod, String vendorId, String customerId, 
                          ComplianceTypeClean complianceType, Set<String> regulatoryRequirements,
                          SecurityClassificationClean securityClassification, RiskLevelClean riskLevel,
                          Integer riskScore, Boolean containsPII, Boolean containsPCI, Boolean containsPHI,
                          Long processingTimeMs, Long responseTimeMs, String performanceTier,
                          Double cpuUtilization, Double memoryUtilization, Integer errorCount,
                          String errorCode, String errorMessage, String description, String businessReason,
                          Map<String, Object> beforeState, Map<String, Object> afterState,
                          Map<String, Object> additionalMetadata, List<String> evidenceFiles, String legalHold,
                          LocalDateTime retentionExpiryDate, Boolean isArchived, String archiveLocation,
                          String digitalSignature, Boolean isImmutable) {
        
        this.id = id;
        this.eventId = eventId;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.timestamp = timestamp;
        this.version = version;
        this.userId = userId;
        this.sessionId = sessionId;
        this.tenantId = tenantId;
        this.organizationId = organizationId;
        this.userRole = userRole;
        this.userPermissions = userPermissions != null ? Set.copyOf(userPermissions) : Collections.emptySet();
        this.eventType = eventType;
        this.businessDomain = businessDomain;
        this.action = action;
        this.subAction = subAction;
        this.result = result;
        this.severity = severity;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.parentResourceId = parentResourceId;
        this.resourceMetadata = resourceMetadata != null ? new HashMap<>(resourceMetadata) : new HashMap<>();
        this.sourceSystem = sourceSystem;
        this.serviceVersion = serviceVersion;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.requestMethod = requestMethod;
        this.requestUri = requestUri;
        this.geographicLocation = geographicLocation;
        this.deviceFingerprint = deviceFingerprint;
        this.businessProcess = businessProcess;
        this.workflowId = workflowId;
        this.transactionId = transactionId;
        this.financialAmount = financialAmount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.vendorId = vendorId;
        this.customerId = customerId;
        this.complianceType = complianceType;
        this.regulatoryRequirements = regulatoryRequirements != null ? Set.copyOf(regulatoryRequirements) : Collections.emptySet();
        this.securityClassification = securityClassification;
        this.riskLevel = riskLevel;
        this.riskScore = riskScore;
        this.containsPII = containsPII;
        this.containsPCI = containsPCI;
        this.containsPHI = containsPHI;
        this.processingTimeMs = processingTimeMs;
        this.responseTimeMs = responseTimeMs;
        this.performanceTier = performanceTier;
        this.cpuUtilization = cpuUtilization;
        this.memoryUtilization = memoryUtilization;
        this.errorCount = errorCount;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.description = description;
        this.businessReason = businessReason;
        this.beforeState = beforeState != null ? new HashMap<>(beforeState) : new HashMap<>();
        this.afterState = afterState != null ? new HashMap<>(afterState) : new HashMap<>();
        this.additionalMetadata = additionalMetadata != null ? new HashMap<>(additionalMetadata) : new HashMap<>();
        this.evidenceFiles = evidenceFiles != null ? List.copyOf(evidenceFiles) : Collections.emptyList();
        this.legalHold = legalHold;
        this.retentionExpiryDate = retentionExpiryDate;
        this.isArchived = isArchived;
        this.archiveLocation = archiveLocation;
        this.hashCode = Objects.hash(id, eventId, userId, timestamp);
        this.digitalSignature = digitalSignature;
        this.isImmutable = isImmutable;
        
        validateBusinessRules();
    }
    
    /**
     * Validates all business rules for the audit event.
     */
    private void validateBusinessRules() {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        
        if (tenantId == null || tenantId.trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant ID cannot be null or empty");
        }
        
        if (action == null || action.trim().isEmpty()) {
            throw new IllegalArgumentException("Action cannot be null or empty");
        }
        
        if (isFinancialEvent() && (financialAmount == null || currency == null)) {
            throw new IllegalArgumentException("Financial events must include amount and currency");
        }
        
        if (containsPCI && securityClassification.ordinal() < SecurityClassificationClean.CONFIDENTIAL.ordinal()) {
            throw new IllegalArgumentException("PCI events must have CONFIDENTIAL or higher security classification");
        }
        
        if (containsPHI && complianceType != ComplianceTypeClean.HIPAA) {
            throw new IllegalArgumentException("PHI events must have HIPAA compliance type");
        }
        
        if (riskLevel == RiskLevelClean.CRITICAL && severity.ordinal() < AuditSeverityClean.HIGH.ordinal()) {
            throw new IllegalArgumentException("Critical risk events must have HIGH or CRITICAL severity");
        }
    }
    
    /**
     * Determines if this is a security-critical event requiring immediate attention.
     */
    public boolean isSecurityCritical() {
        return eventType.isSecurityEvent() ||
               securityClassification == SecurityClassificationClean.TOP_SECRET ||
               riskLevel == RiskLevelClean.CRITICAL ||
               severity == AuditSeverityClean.CRITICAL ||
               isUnauthorizedAccess() ||
               isDataBreach() ||
               isPotentialFraud();
    }
    
    /**
     * Determines if this event represents a compliance violation.
     */
    public boolean isComplianceViolation() {
        return result == AuditResultClean.COMPLIANCE_VIOLATION ||
               (containsPII && !hasValidPrivacyConsent()) ||
               (containsPCI && !hasValidPCIAuthorization()) ||
               (containsPHI && !hasValidHIPAAAuthorization()) ||
               isRegulatoryConcern();
    }
    
    /**
     * Calculates the business impact score (0-100).
     */
    public int calculateBusinessImpactScore() {
        int score = 0;
        
        // Base score from severity
        score += severity.getBaseScore();
        
        // Risk level contribution
        score += riskLevel.getImpactScore();
        
        // Financial impact
        if (isFinancialEvent() && financialAmount != null) {
            if (financialAmount.compareTo(BigDecimal.valueOf(1000000)) > 0) score += 25;
            else if (financialAmount.compareTo(BigDecimal.valueOf(100000)) > 0) score += 20;
            else if (financialAmount.compareTo(BigDecimal.valueOf(10000)) > 0) score += 15;
            else if (financialAmount.compareTo(BigDecimal.valueOf(1000)) > 0) score += 10;
        }
        
        // Compliance and regulatory impact
        if (isComplianceViolation()) score += 30;
        if (containsPII || containsPCI || containsPHI) score += 15;
        
        // Security impact
        if (isSecurityCritical()) score += 25;
        if (isUnauthorizedAccess()) score += 20;
        
        // Performance impact
        if (isPerformanceDegraded()) score += 10;
        
        return Math.min(100, score);
    }
    
    /**
     * Determines if the event requires regulatory reporting.
     */
    public boolean requiresRegulatoryReporting() {
        return complianceType.requiresReporting() ||
               isComplianceViolation() ||
               (isFinancialEvent() && financialAmount != null && 
                financialAmount.compareTo(BigDecimal.valueOf(10000)) > 0) ||
               (containsPII && businessDomain == BusinessDomainClean.CUSTOMER_MANAGEMENT) ||
               isSecurityIncident();
    }
    
    /**
     * Gets the required retention period based on compliance and business rules.
     */
    public Duration getRequiredRetentionPeriod() {
        // Regulatory requirements override business rules
        if (complianceType == ComplianceTypeClean.SOX) {
            return Duration.ofDays(2555); // 7 years
        }
        if (complianceType == ComplianceTypeClean.GDPR && containsPII) {
            return Duration.ofDays(2190); // 6 years
        }
        if (complianceType == ComplianceTypeClean.PCI_DSS && containsPCI) {
            return Duration.ofDays(1095); // 3 years
        }
        if (complianceType == ComplianceTypeClean.HIPAA && containsPHI) {
            return Duration.ofDays(2190); // 6 years
        }
        
        // Business-specific retention
        if (isFinancialEvent()) {
            return Duration.ofDays(2555); // 7 years for financial
        }
        if (isSecurityCritical()) {
            return Duration.ofDays(1825); // 5 years for security
        }
        if (eventType.isBusinessCritical()) {
            return Duration.ofDays(1095); // 3 years for business critical
        }
        
        return Duration.ofDays(365); // 1 year default
    }
    
    /**
     * Checks if the audit event has expired based on retention policy.
     */
    public boolean hasExpiredRetention() {
        return retentionExpiryDate != null && 
               LocalDateTime.now().isAfter(retentionExpiryDate) &&
               legalHold == null;
    }
    
    /**
     * Determines if event can be archived.
     */
    public boolean canBeArchived() {
        return !hasExpiredRetention() &&
               !isUnderLegalHold() &&
               !isActiveInvestigation() &&
               !isPendingRegulatory() &&
               (LocalDateTime.now().isAfter(timestamp.plusDays(90))); // 90 days minimum
    }
    
    /**
     * Calculates the event criticality level for escalation.
     */
    public EventCriticalityClean calculateCriticality() {
        if (isSecurityCritical() && calculateBusinessImpactScore() > 80) {
            return EventCriticalityClean.CATASTROPHIC;
        }
        if (isComplianceViolation() && riskLevel == RiskLevelClean.HIGH) {
            return EventCriticalityClean.SEVERE;
        }
        if (requiresRegulatoryReporting() || calculateBusinessImpactScore() > 60) {
            return EventCriticalityClean.MAJOR;
        }
        if (severity == AuditSeverityClean.HIGH || riskLevel == RiskLevelClean.MEDIUM) {
            return EventCriticalityClean.MINOR;
        }
        return EventCriticalityClean.NEGLIGIBLE;
    }
    
    /**
     * Generates compliance evidence package for regulatory reporting.
     */
    public ComplianceEvidencePackageClean generateComplianceEvidence() {
        return ComplianceEvidencePackageClean.builder()
            .eventId(eventId)
            .correlationId(correlationId)
            .timestamp(timestamp)
            .complianceType(complianceType)
            .regulatoryRequirements(regulatoryRequirements)
            .businessContext(createBusinessContext())
            .technicalContext(createTechnicalContext())
            .evidenceFiles(evidenceFiles)
            .digitalSignature(digitalSignature)
            .auditTrail(createAuditTrail())
            .retentionPolicy(getRequiredRetentionPeriod())
            .legalHold(legalHold)
            .build();
    }
    
    /**
     * Creates a fraud detection report if applicable.
     */
    public FraudDetectionReportClean createFraudReport() {
        if (!isPotentialFraud()) {
            return null;
        }
        
        return FraudDetectionReportClean.builder()
            .eventId(eventId)
            .timestamp(timestamp)
            .userId(userId)
            .riskScore(riskScore)
            .fraudIndicators(identifyFraudIndicators())
            .financialAmount(financialAmount)
            .currency(currency)
            .geographicLocation(geographicLocation)
            .deviceFingerprint(deviceFingerprint)
            .behaviorPattern(analyzeBehaviorPattern())
            .recommendedAction(getRecommendedFraudAction())
            .build();
    }
    
    /**
     * Determines if event requires immediate escalation.
     */
    public boolean requiresImmediateEscalation() {
        return calculateCriticality().ordinal() >= EventCriticalityClean.MAJOR.ordinal() ||
               isSecurityCritical() ||
               isComplianceViolation() ||
               (isFinancialEvent() && isPotentialFraud()) ||
               (riskScore != null && riskScore > 85);
    }
    
    /**
     * Gets recommended escalation actions.
     */
    public List<EscalationActionClean> getRecommendedEscalations() {
        List<EscalationActionClean> actions = new java.util.ArrayList<>();
        
        if (isSecurityCritical()) {
            actions.add(EscalationActionClean.NOTIFY_SECURITY_TEAM);
            actions.add(EscalationActionClean.BLOCK_USER_SESSION);
        }
        
        if (isComplianceViolation()) {
            actions.add(EscalationActionClean.NOTIFY_COMPLIANCE_OFFICER);
            actions.add(EscalationActionClean.GENERATE_COMPLIANCE_REPORT);
        }
        
        if (isPotentialFraud()) {
            actions.add(EscalationActionClean.FREEZE_ACCOUNT);
            actions.add(EscalationActionClean.NOTIFY_FRAUD_TEAM);
        }
        
        if (requiresRegulatoryReporting()) {
            actions.add(EscalationActionClean.PREPARE_REGULATORY_FILING);
        }
        
        if (calculateBusinessImpactScore() > 70) {
            actions.add(EscalationActionClean.NOTIFY_SENIOR_MANAGEMENT);
        }
        
        return actions;
    }
    
    // Private helper methods for business logic
    
    private AuditSeverityClean calculateDefaultSeverity() {
        if (result == AuditResultClean.FAILURE || result == AuditResultClean.ERROR) {
            return AuditSeverityClean.MEDIUM;
        }
        if (eventType.isSecurityEvent()) {
            return AuditSeverityClean.HIGH;
        }
        return AuditSeverityClean.LOW;
    }
    
    private RiskLevelClean calculateDefaultRiskLevel() {
        if (eventType.isSecurityEvent() || result == AuditResultClean.FAILURE) {
            return RiskLevelClean.MEDIUM;
        }
        return RiskLevelClean.LOW;
    }
    
    private Integer calculateRiskScore() {
        int score = 10; // Base score
        
        if (eventType.isSecurityEvent()) score += 30;
        if (result == AuditResultClean.FAILURE) score += 25;
        if (severity == AuditSeverityClean.HIGH) score += 20;
        if (severity == AuditSeverityClean.CRITICAL) score += 40;
        
        return Math.min(100, score);
    }
    
    private LocalDateTime calculateRetentionExpiryDate() {
        return timestamp.plus(getRequiredRetentionPeriod());
    }
    
    private String generateDigitalSignature() {
        // In a real implementation, this would generate a cryptographic signature
        String data = id + eventId + userId + timestamp.toString();
        return "SHA256:" + Integer.toHexString(data.hashCode());
    }
    
    private boolean isFinancialEvent() {
        return eventType.isFinancialEvent() ||
               financialAmount != null ||
               businessDomain == BusinessDomainClean.FINANCE ||
               businessDomain == BusinessDomainClean.PAYMENT;
    }
    
    private boolean isUnauthorizedAccess() {
        return eventType == AuditEventTypeClean.UNAUTHORIZED_ACCESS ||
               result == AuditResultClean.ACCESS_DENIED ||
               (eventType.isSecurityEvent() && result == AuditResultClean.FAILURE);
    }
    
    private boolean isDataBreach() {
        return eventType == AuditEventTypeClean.DATA_BREACH ||
               eventType == AuditEventTypeClean.DATA_EXPORT_UNAUTHORIZED ||
               (containsPII && isUnauthorizedAccess());
    }
    
    private boolean isPotentialFraud() {
        return eventType == AuditEventTypeClean.FRAUD_SUSPECTED ||
               (isFinancialEvent() && isAnomalousTransaction()) ||
               (riskScore != null && riskScore > 75) ||
               hasMultipleFailedAttempts();
    }
    
    private boolean hasValidPrivacyConsent() {
        return additionalMetadata.containsKey("privacyConsent") &&
               Boolean.TRUE.equals(additionalMetadata.get("privacyConsent"));
    }
    
    private boolean hasValidPCIAuthorization() {
        return additionalMetadata.containsKey("pciAuthorized") &&
               Boolean.TRUE.equals(additionalMetadata.get("pciAuthorized"));
    }
    
    private boolean hasValidHIPAAAuthorization() {
        return additionalMetadata.containsKey("hipaaAuthorized") &&
               Boolean.TRUE.equals(additionalMetadata.get("hipaaAuthorized"));
    }
    
    private boolean isRegulatoryConcern() {
        return regulatoryRequirements.size() > 1 ||
               additionalMetadata.containsKey("regulatoryFlag");
    }
    
    private boolean isPerformanceDegraded() {
        return (responseTimeMs != null && responseTimeMs > 5000) ||
               (cpuUtilization != null && cpuUtilization > 0.8) ||
               (memoryUtilization != null && memoryUtilization > 0.9);
    }
    
    private boolean isSecurityIncident() {
        return eventType.isSecurityEvent() ||
               isUnauthorizedAccess() ||
               isDataBreach() ||
               isPotentialFraud();
    }
    
    private boolean isUnderLegalHold() {
        return legalHold != null && !legalHold.trim().isEmpty();
    }
    
    private boolean isActiveInvestigation() {
        return additionalMetadata.containsKey("investigationId") &&
               additionalMetadata.get("investigationId") != null;
    }
    
    private boolean isPendingRegulatory() {
        return additionalMetadata.containsKey("regulatoryPending") &&
               Boolean.TRUE.equals(additionalMetadata.get("regulatoryPending"));
    }
    
    private boolean isAnomalousTransaction() {
        return additionalMetadata.containsKey("anomalyDetected") &&
               Boolean.TRUE.equals(additionalMetadata.get("anomalyDetected"));
    }
    
    private boolean hasMultipleFailedAttempts() {
        return errorCount != null && errorCount > 3;
    }
    
    private BusinessContextClean createBusinessContext() {
        return BusinessContextClean.builder()
            .businessDomain(businessDomain)
            .businessProcess(businessProcess)
            .workflowId(workflowId)
            .transactionId(transactionId)
            .customerId(customerId)
            .vendorId(vendorId)
            .businessReason(businessReason)
            .build();
    }
    
    private TechnicalContextClean createTechnicalContext() {
        return TechnicalContextClean.builder()
            .sourceSystem(sourceSystem)
            .serviceVersion(serviceVersion)
            .ipAddress(ipAddress)
            .userAgent(userAgent)
            .requestMethod(requestMethod)
            .requestUri(requestUri)
            .processingTimeMs(processingTimeMs)
            .responseTimeMs(responseTimeMs)
            .build();
    }
    
    private AuditTrailClean createAuditTrail() {
        return AuditTrailClean.builder()
            .eventId(eventId)
            .timestamp(timestamp)
            .userId(userId)
            .action(action)
            .result(result)
            .description(description)
            .beforeState(beforeState)
            .afterState(afterState)
            .build();
    }
    
    private Set<FraudIndicatorClean> identifyFraudIndicators() {
        Set<FraudIndicatorClean> indicators = new java.util.HashSet<>();
        
        if (isAnomalousTransaction()) {
            indicators.add(FraudIndicatorClean.ANOMALOUS_TRANSACTION_PATTERN);
        }
        if (hasMultipleFailedAttempts()) {
            indicators.add(FraudIndicatorClean.MULTIPLE_FAILED_ATTEMPTS);
        }
        if (geographicLocation != null && additionalMetadata.containsKey("unusualLocation")) {
            indicators.add(FraudIndicatorClean.UNUSUAL_GEOGRAPHIC_LOCATION);
        }
        if (deviceFingerprint != null && additionalMetadata.containsKey("newDevice")) {
            indicators.add(FraudIndicatorClean.UNRECOGNIZED_DEVICE);
        }
        
        return indicators;
    }
    
    private BehaviorPatternClean analyzeBehaviorPattern() {
        return BehaviorPatternClean.builder()
            .userId(userId)
            .sessionId(sessionId)
            .timestamp(timestamp)
            .action(action)
            .result(result)
            .riskScore(riskScore)
            .build();
    }
    
    private FraudActionRecommendationClean getRecommendedFraudAction() {
        if (riskScore != null && riskScore > 90) {
            return FraudActionRecommendationClean.IMMEDIATE_BLOCK;
        }
        if (riskScore != null && riskScore > 75) {
            return FraudActionRecommendationClean.REQUIRE_ADDITIONAL_VERIFICATION;
        }
        if (riskScore != null && riskScore > 60) {
            return FraudActionRecommendationClean.MONITOR_CLOSELY;
        }
        return FraudActionRecommendationClean.NO_ACTION_REQUIRED;
    }
    
    // Immutable copy methods for state changes
    
    public AuditEventClean withRiskAssessment(RiskLevelClean newRiskLevel, Integer newRiskScore) {
        return new AuditEventClean(
            id, eventId, correlationId, causationId, timestamp, version,
            userId, sessionId, tenantId, organizationId, userRole, userPermissions,
            eventType, businessDomain, action, subAction, result, severity,
            resourceType, resourceId, resourceName, parentResourceId, resourceMetadata,
            sourceSystem, serviceVersion, ipAddress, userAgent, requestMethod, requestUri,
            geographicLocation, deviceFingerprint, businessProcess, workflowId, transactionId,
            financialAmount, currency, paymentMethod, vendorId, customerId,
            complianceType, regulatoryRequirements, securityClassification, newRiskLevel,
            newRiskScore, containsPII, containsPCI, containsPHI, processingTimeMs, responseTimeMs,
            performanceTier, cpuUtilization, memoryUtilization, errorCount, errorCode, errorMessage,
            description, businessReason, beforeState, afterState, additionalMetadata,
            evidenceFiles, legalHold, retentionExpiryDate, isArchived, archiveLocation,
            digitalSignature, isImmutable
        );
    }
    
    public AuditEventClean withComplianceEnhancement(ComplianceTypeClean newComplianceType, 
                                                    Set<String> newRegulatoryRequirements) {
        return new AuditEventClean(
            id, eventId, correlationId, causationId, timestamp, version,
            userId, sessionId, tenantId, organizationId, userRole, userPermissions,
            eventType, businessDomain, action, subAction, result, severity,
            resourceType, resourceId, resourceName, parentResourceId, resourceMetadata,
            sourceSystem, serviceVersion, ipAddress, userAgent, requestMethod, requestUri,
            geographicLocation, deviceFingerprint, businessProcess, workflowId, transactionId,
            financialAmount, currency, paymentMethod, vendorId, customerId,
            newComplianceType, newRegulatoryRequirements, securityClassification, riskLevel,
            riskScore, containsPII, containsPCI, containsPHI, processingTimeMs, responseTimeMs,
            performanceTier, cpuUtilization, memoryUtilization, errorCount, errorCode, errorMessage,
            description, businessReason, beforeState, afterState, additionalMetadata,
            evidenceFiles, legalHold, retentionExpiryDate, isArchived, archiveLocation,
            digitalSignature, isImmutable
        );
    }
    
    public AuditEventClean withLegalHold(String holdReason) {
        return new AuditEventClean(
            id, eventId, correlationId, causationId, timestamp, version,
            userId, sessionId, tenantId, organizationId, userRole, userPermissions,
            eventType, businessDomain, action, subAction, result, severity,
            resourceType, resourceId, resourceName, parentResourceId, resourceMetadata,
            sourceSystem, serviceVersion, ipAddress, userAgent, requestMethod, requestUri,
            geographicLocation, deviceFingerprint, businessProcess, workflowId, transactionId,
            financialAmount, currency, paymentMethod, vendorId, customerId,
            complianceType, regulatoryRequirements, securityClassification, riskLevel,
            riskScore, containsPII, containsPCI, containsPHI, processingTimeMs, responseTimeMs,
            performanceTier, cpuUtilization, memoryUtilization, errorCount, errorCode, errorMessage,
            description, businessReason, beforeState, afterState, additionalMetadata,
            evidenceFiles, holdReason, retentionExpiryDate, isArchived, archiveLocation,
            digitalSignature, isImmutable
        );
    }
    
    // Static factory methods for common event types
    
    public static AuditEventClean createSecurityEvent(String userId, String tenantId, String action, 
                                                     String ipAddress, AuditResultClean result) {
        return new AuditEventClean(
            UUID.randomUUID(),
            "SEC-" + UUID.randomUUID().toString(),
            userId,
            tenantId,
            AuditEventTypeClean.SECURITY_EVENT,
            BusinessDomainClean.SECURITY,
            action,
            result
        );
    }
    
    public static AuditEventClean createFinancialTransaction(String userId, String tenantId, 
                                                           BigDecimal amount, String currency, 
                                                           String transactionId) {
        AuditEventClean event = new AuditEventClean(
            UUID.randomUUID(),
            "FIN-" + UUID.randomUUID().toString(),
            userId,
            tenantId,
            AuditEventTypeClean.FINANCIAL_TRANSACTION,
            BusinessDomainClean.FINANCE,
            "PROCESS_PAYMENT",
            AuditResultClean.SUCCESS
        );
        
        return new AuditEventClean(
            event.id, event.eventId, event.correlationId, event.causationId, event.timestamp, event.version,
            event.userId, event.sessionId, event.tenantId, event.organizationId, event.userRole, event.userPermissions,
            event.eventType, event.businessDomain, event.action, event.subAction, event.result, event.severity,
            event.resourceType, event.resourceId, event.resourceName, event.parentResourceId, event.resourceMetadata,
            event.sourceSystem, event.serviceVersion, event.ipAddress, event.userAgent, event.requestMethod, event.requestUri,
            event.geographicLocation, event.deviceFingerprint, event.businessProcess, event.workflowId, transactionId,
            amount, currency, event.paymentMethod, event.vendorId, event.customerId,
            ComplianceTypeClean.PCI_DSS, Set.of("PCI-DSS", "AML"), SecurityClassificationClean.CONFIDENTIAL, RiskLevelClean.MEDIUM,
            event.riskScore, event.containsPII, true, event.containsPHI, event.processingTimeMs, event.responseTimeMs,
            event.performanceTier, event.cpuUtilization, event.memoryUtilization, event.errorCount, event.errorCode, event.errorMessage,
            event.description, event.businessReason, event.beforeState, event.afterState, event.additionalMetadata,
            event.evidenceFiles, event.legalHold, event.retentionExpiryDate, event.isArchived, event.archiveLocation,
            event.digitalSignature, event.isImmutable
        );
    }
    
    public static AuditEventClean createComplianceEvent(String userId, String tenantId, 
                                                       ComplianceTypeClean complianceType, 
                                                       String action, AuditResultClean result) {
        return new AuditEventClean(
            UUID.randomUUID(),
            "COMP-" + UUID.randomUUID().toString(),
            userId,
            tenantId,
            AuditEventTypeClean.COMPLIANCE_CHECK,
            BusinessDomainClean.COMPLIANCE,
            action,
            result
        ).withComplianceEnhancement(complianceType, complianceType.getRequiredStandards());
    }
    
    // Builder for complex object creation
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private UUID id = UUID.randomUUID();
        private String eventId = UUID.randomUUID().toString();
        private String correlationId = UUID.randomUUID().toString();
        private String causationId;
        private LocalDateTime timestamp = LocalDateTime.now();
        private String version = "2.0.0";
        private String userId;
        private String sessionId;
        private String tenantId;
        private String organizationId;
        private String userRole;
        private Set<String> userPermissions = Collections.emptySet();
        private AuditEventTypeClean eventType;
        private BusinessDomainClean businessDomain;
        private String action;
        private String subAction;
        private AuditResultClean result;
        private AuditSeverityClean severity = AuditSeverityClean.LOW;
        private String resourceType;
        private String resourceId;
        private String resourceName;
        private String parentResourceId;
        private Map<String, Object> resourceMetadata = new HashMap<>();
        private String sourceSystem = "gogidix-audit-system";
        private String serviceVersion = "1.0.0";
        private String ipAddress;
        private String userAgent;
        private String requestMethod;
        private String requestUri;
        private String geographicLocation;
        private String deviceFingerprint;
        private String businessProcess;
        private String workflowId;
        private String transactionId;
        private BigDecimal financialAmount;
        private String currency;
        private String paymentMethod;
        private String vendorId;
        private String customerId;
        private ComplianceTypeClean complianceType = ComplianceTypeClean.STANDARD;
        private Set<String> regulatoryRequirements = Collections.emptySet();
        private SecurityClassificationClean securityClassification = SecurityClassificationClean.PUBLIC;
        private RiskLevelClean riskLevel = RiskLevelClean.LOW;
        private Integer riskScore = 10;
        private Boolean containsPII = false;
        private Boolean containsPCI = false;
        private Boolean containsPHI = false;
        private Long processingTimeMs;
        private Long responseTimeMs;
        private String performanceTier = "STANDARD";
        private Double cpuUtilization;
        private Double memoryUtilization;
        private Integer errorCount = 0;
        private String errorCode;
        private String errorMessage;
        private String description;
        private String businessReason;
        private Map<String, Object> beforeState = new HashMap<>();
        private Map<String, Object> afterState = new HashMap<>();
        private Map<String, Object> additionalMetadata = new HashMap<>();
        private List<String> evidenceFiles = Collections.emptyList();
        private String legalHold;
        private LocalDateTime retentionExpiryDate;
        private Boolean isArchived = false;
        private String archiveLocation;
        private String digitalSignature;
        private Boolean isImmutable = true;
        
        public Builder id(UUID id) { this.id = id; return this; }
        public Builder eventId(String eventId) { this.eventId = eventId; return this; }
        public Builder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public Builder causationId(String causationId) { this.causationId = causationId; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public Builder version(String version) { this.version = version; return this; }
        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder organizationId(String organizationId) { this.organizationId = organizationId; return this; }
        public Builder userRole(String userRole) { this.userRole = userRole; return this; }
        public Builder userPermissions(Set<String> userPermissions) { this.userPermissions = userPermissions; return this; }
        public Builder eventType(AuditEventTypeClean eventType) { this.eventType = eventType; return this; }
        public Builder businessDomain(BusinessDomainClean businessDomain) { this.businessDomain = businessDomain; return this; }
        public Builder action(String action) { this.action = action; return this; }
        public Builder subAction(String subAction) { this.subAction = subAction; return this; }
        public Builder result(AuditResultClean result) { this.result = result; return this; }
        public Builder severity(AuditSeverityClean severity) { this.severity = severity; return this; }
        public Builder resourceType(String resourceType) { this.resourceType = resourceType; return this; }
        public Builder resourceId(String resourceId) { this.resourceId = resourceId; return this; }
        public Builder resourceName(String resourceName) { this.resourceName = resourceName; return this; }
        public Builder parentResourceId(String parentResourceId) { this.parentResourceId = parentResourceId; return this; }
        public Builder resourceMetadata(Map<String, Object> resourceMetadata) { this.resourceMetadata = resourceMetadata; return this; }
        public Builder sourceSystem(String sourceSystem) { this.sourceSystem = sourceSystem; return this; }
        public Builder serviceVersion(String serviceVersion) { this.serviceVersion = serviceVersion; return this; }
        public Builder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public Builder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public Builder requestMethod(String requestMethod) { this.requestMethod = requestMethod; return this; }
        public Builder requestUri(String requestUri) { this.requestUri = requestUri; return this; }
        public Builder geographicLocation(String geographicLocation) { this.geographicLocation = geographicLocation; return this; }
        public Builder deviceFingerprint(String deviceFingerprint) { this.deviceFingerprint = deviceFingerprint; return this; }
        public Builder businessProcess(String businessProcess) { this.businessProcess = businessProcess; return this; }
        public Builder workflowId(String workflowId) { this.workflowId = workflowId; return this; }
        public Builder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public Builder financialAmount(BigDecimal financialAmount) { this.financialAmount = financialAmount; return this; }
        public Builder currency(String currency) { this.currency = currency; return this; }
        public Builder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder vendorId(String vendorId) { this.vendorId = vendorId; return this; }
        public Builder customerId(String customerId) { this.customerId = customerId; return this; }
        public Builder complianceType(ComplianceTypeClean complianceType) { this.complianceType = complianceType; return this; }
        public Builder regulatoryRequirements(Set<String> regulatoryRequirements) { this.regulatoryRequirements = regulatoryRequirements; return this; }
        public Builder securityClassification(SecurityClassificationClean securityClassification) { this.securityClassification = securityClassification; return this; }
        public Builder riskLevel(RiskLevelClean riskLevel) { this.riskLevel = riskLevel; return this; }
        public Builder riskScore(Integer riskScore) { this.riskScore = riskScore; return this; }
        public Builder containsPII(Boolean containsPII) { this.containsPII = containsPII; return this; }
        public Builder containsPCI(Boolean containsPCI) { this.containsPCI = containsPCI; return this; }
        public Builder containsPHI(Boolean containsPHI) { this.containsPHI = containsPHI; return this; }
        public Builder processingTimeMs(Long processingTimeMs) { this.processingTimeMs = processingTimeMs; return this; }
        public Builder responseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; return this; }
        public Builder performanceTier(String performanceTier) { this.performanceTier = performanceTier; return this; }
        public Builder cpuUtilization(Double cpuUtilization) { this.cpuUtilization = cpuUtilization; return this; }
        public Builder memoryUtilization(Double memoryUtilization) { this.memoryUtilization = memoryUtilization; return this; }
        public Builder errorCount(Integer errorCount) { this.errorCount = errorCount; return this; }
        public Builder errorCode(String errorCode) { this.errorCode = errorCode; return this; }
        public Builder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder businessReason(String businessReason) { this.businessReason = businessReason; return this; }
        public Builder beforeState(Map<String, Object> beforeState) { this.beforeState = beforeState; return this; }
        public Builder afterState(Map<String, Object> afterState) { this.afterState = afterState; return this; }
        public Builder additionalMetadata(Map<String, Object> additionalMetadata) { this.additionalMetadata = additionalMetadata; return this; }
        public Builder evidenceFiles(List<String> evidenceFiles) { this.evidenceFiles = evidenceFiles; return this; }
        public Builder legalHold(String legalHold) { this.legalHold = legalHold; return this; }
        public Builder retentionExpiryDate(LocalDateTime retentionExpiryDate) { this.retentionExpiryDate = retentionExpiryDate; return this; }
        public Builder isArchived(Boolean isArchived) { this.isArchived = isArchived; return this; }
        public Builder archiveLocation(String archiveLocation) { this.archiveLocation = archiveLocation; return this; }
        public Builder digitalSignature(String digitalSignature) { this.digitalSignature = digitalSignature; return this; }
        public Builder isImmutable(Boolean isImmutable) { this.isImmutable = isImmutable; return this; }
        
        public AuditEventClean build() {
            if (retentionExpiryDate == null) {
                Duration retention = Duration.ofDays(365); // Default 1 year
                retentionExpiryDate = timestamp.plus(retention);
            }
            if (digitalSignature == null) {
                String data = id + eventId + userId + timestamp.toString();
                digitalSignature = "SHA256:" + Integer.toHexString(data.hashCode());
            }
            
            return new AuditEventClean(
                id, eventId, correlationId, causationId, timestamp, version,
                userId, sessionId, tenantId, organizationId, userRole, userPermissions,
                eventType, businessDomain, action, subAction, result, severity,
                resourceType, resourceId, resourceName, parentResourceId, resourceMetadata,
                sourceSystem, serviceVersion, ipAddress, userAgent, requestMethod, requestUri,
                geographicLocation, deviceFingerprint, businessProcess, workflowId, transactionId,
                financialAmount, currency, paymentMethod, vendorId, customerId,
                complianceType, regulatoryRequirements, securityClassification, riskLevel,
                riskScore, containsPII, containsPCI, containsPHI, processingTimeMs, responseTimeMs,
                performanceTier, cpuUtilization, memoryUtilization, errorCount, errorCode, errorMessage,
                description, businessReason, beforeState, afterState, additionalMetadata,
                evidenceFiles, legalHold, retentionExpiryDate, isArchived, archiveLocation,
                digitalSignature, isImmutable
            );
        }
    }
    
    // Getters for immutable access
    public UUID getId() { return id; }
    public String getEventId() { return eventId; }
    public String getCorrelationId() { return correlationId; }
    public String getCausationId() { return causationId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getVersion() { return version; }
    public String getUserId() { return userId; }
    public String getSessionId() { return sessionId; }
    public String getTenantId() { return tenantId; }
    public String getOrganizationId() { return organizationId; }
    public String getUserRole() { return userRole; }
    public Set<String> getUserPermissions() { return Set.copyOf(userPermissions); }
    public AuditEventTypeClean getEventType() { return eventType; }
    public BusinessDomainClean getBusinessDomain() { return businessDomain; }
    public String getAction() { return action; }
    public String getSubAction() { return subAction; }
    public AuditResultClean getResult() { return result; }
    public AuditSeverityClean getSeverity() { return severity; }
    public String getResourceType() { return resourceType; }
    public String getResourceId() { return resourceId; }
    public String getResourceName() { return resourceName; }
    public String getParentResourceId() { return parentResourceId; }
    public Map<String, Object> getResourceMetadata() { return new HashMap<>(resourceMetadata); }
    public String getSourceSystem() { return sourceSystem; }
    public String getServiceVersion() { return serviceVersion; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
    public String getRequestMethod() { return requestMethod; }
    public String getRequestUri() { return requestUri; }
    public String getGeographicLocation() { return geographicLocation; }
    public String getDeviceFingerprint() { return deviceFingerprint; }
    public String getBusinessProcess() { return businessProcess; }
    public String getWorkflowId() { return workflowId; }
    public String getTransactionId() { return transactionId; }
    public BigDecimal getFinancialAmount() { return financialAmount; }
    public String getCurrency() { return currency; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getVendorId() { return vendorId; }
    public String getCustomerId() { return customerId; }
    public ComplianceTypeClean getComplianceType() { return complianceType; }
    public Set<String> getRegulatoryRequirements() { return Set.copyOf(regulatoryRequirements); }
    public SecurityClassificationClean getSecurityClassification() { return securityClassification; }
    public RiskLevelClean getRiskLevel() { return riskLevel; }
    public Integer getRiskScore() { return riskScore; }
    public Boolean getContainsPII() { return containsPII; }
    public Boolean getContainsPCI() { return containsPCI; }
    public Boolean getContainsPHI() { return containsPHI; }
    public Long getProcessingTimeMs() { return processingTimeMs; }
    public Long getResponseTimeMs() { return responseTimeMs; }
    public String getPerformanceTier() { return performanceTier; }
    public Double getCpuUtilization() { return cpuUtilization; }
    public Double getMemoryUtilization() { return memoryUtilization; }
    public Integer getErrorCount() { return errorCount; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public String getDescription() { return description; }
    public String getBusinessReason() { return businessReason; }
    public Map<String, Object> getBeforeState() { return new HashMap<>(beforeState); }
    public Map<String, Object> getAfterState() { return new HashMap<>(afterState); }
    public Map<String, Object> getAdditionalMetadata() { return new HashMap<>(additionalMetadata); }
    public List<String> getEvidenceFiles() { return List.copyOf(evidenceFiles); }
    public String getLegalHold() { return legalHold; }
    public LocalDateTime getRetentionExpiryDate() { return retentionExpiryDate; }
    public Boolean getIsArchived() { return isArchived; }
    public String getArchiveLocation() { return archiveLocation; }
    public String getDigitalSignature() { return digitalSignature; }
    public Boolean getIsImmutable() { return isImmutable; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditEventClean)) return false;
        AuditEventClean that = (AuditEventClean) o;
        return Objects.equals(id, that.id) && Objects.equals(eventId, that.eventId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, eventId);
    }
    
    @Override
    public String toString() {
        return String.format("AuditEventClean{id=%s, eventId='%s', userId='%s', eventType=%s, result=%s, severity=%s}", 
                           id, eventId, userId, eventType, result, severity);
    }
}

// Supporting enums and value objects (would typically be in separate files)

enum AuditEventTypeClean {
    USER_LOGIN("User Login", true, false, false),
    USER_LOGOUT("User Logout", true, false, false),
    USER_ACCESS("User Access", false, false, false),
    UNAUTHORIZED_ACCESS("Unauthorized Access", true, false, true),
    SECURITY_EVENT("Security Event", true, false, true),
    FINANCIAL_TRANSACTION("Financial Transaction", false, true, false),
    PAYMENT_PROCESSING("Payment Processing", false, true, false),
    DATA_ACCESS("Data Access", false, false, false),
    DATA_MODIFICATION("Data Modification", false, false, false),
    DATA_DELETION("Data Deletion", false, false, true),
    DATA_BREACH("Data Breach", true, false, true),
    DATA_EXPORT_UNAUTHORIZED("Unauthorized Data Export", true, false, true),
    SYSTEM_STARTUP("System Startup", false, false, true),
    SYSTEM_SHUTDOWN("System Shutdown", false, false, true),
    COMPLIANCE_CHECK("Compliance Check", false, false, false),
    FRAUD_SUSPECTED("Fraud Suspected", true, true, true),
    API_CALL("API Call", false, false, false),
    CONFIGURATION_CHANGE("Configuration Change", false, false, true),
    BACKUP_OPERATION("Backup Operation", false, false, false),
    RESTORE_OPERATION("Restore Operation", false, false, true);
    
    private final String displayName;
    private final boolean isSecurityEvent;
    private final boolean isFinancialEvent;
    private final boolean isBusinessCritical;
    
    AuditEventTypeClean(String displayName, boolean isSecurityEvent, boolean isFinancialEvent, boolean isBusinessCritical) {
        this.displayName = displayName;
        this.isSecurityEvent = isSecurityEvent;
        this.isFinancialEvent = isFinancialEvent;
        this.isBusinessCritical = isBusinessCritical;
    }
    
    public String getDisplayName() { return displayName; }
    public boolean isSecurityEvent() { return isSecurityEvent; }
    public boolean isFinancialEvent() { return isFinancialEvent; }
    public boolean isBusinessCritical() { return isBusinessCritical; }
}

enum BusinessDomainClean {
    USER_MANAGEMENT("User Management"),
    SECURITY("Security"),
    FINANCE("Finance"),
    PAYMENT("Payment"),
    COMPLIANCE("Compliance"),
    CUSTOMER_MANAGEMENT("Customer Management"),
    VENDOR_MANAGEMENT("Vendor Management"),
    SYSTEM_ADMINISTRATION("System Administration"),
    DATA_MANAGEMENT("Data Management"),
    REPORTING("Reporting");
    
    private final String displayName;
    
    BusinessDomainClean(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
}

enum AuditResultClean {
    SUCCESS("Success", 0),
    FAILURE("Failure", 25),
    ERROR("Error", 30),
    ACCESS_DENIED("Access Denied", 20),
    COMPLIANCE_VIOLATION("Compliance Violation", 40),
    BLOCKED("Blocked", 35);
    
    private final String displayName;
    private final int riskScore;
    
    AuditResultClean(String displayName, int riskScore) {
        this.displayName = displayName;
        this.riskScore = riskScore;
    }
    
    public String getDisplayName() { return displayName; }
    public int getRiskScore() { return riskScore; }
}

enum AuditSeverityClean {
    LOW("Low", 10),
    MEDIUM("Medium", 25),
    HIGH("High", 40),
    CRITICAL("Critical", 60);
    
    private final String displayName;
    private final int baseScore;
    
    AuditSeverityClean(String displayName, int baseScore) {
        this.displayName = displayName;
        this.baseScore = baseScore;
    }
    
    public String getDisplayName() { return displayName; }
    public int getBaseScore() { return baseScore; }
}

enum ComplianceTypeClean {
    NONE("None", Duration.ofDays(365), false, Collections.emptySet()),
    STANDARD("Standard", Duration.ofDays(365), false, Set.of("STANDARD")),
    GDPR("GDPR", Duration.ofDays(2190), true, Set.of("GDPR", "PRIVACY")),
    PCI_DSS("PCI-DSS", Duration.ofDays(1095), true, Set.of("PCI-DSS", "PAYMENT")),
    SOX("SOX", Duration.ofDays(2555), true, Set.of("SOX", "FINANCIAL")),
    HIPAA("HIPAA", Duration.ofDays(2190), true, Set.of("HIPAA", "HEALTHCARE")),
    FINANCIAL("Financial", Duration.ofDays(2555), true, Set.of("FINANCIAL", "AML"));
    
    private final String displayName;
    private final Duration retentionPeriod;
    private final boolean requiresReporting;
    private final Set<String> requiredStandards;
    
    ComplianceTypeClean(String displayName, Duration retentionPeriod, boolean requiresReporting, Set<String> requiredStandards) {
        this.displayName = displayName;
        this.retentionPeriod = retentionPeriod;
        this.requiresReporting = requiresReporting;
        this.requiredStandards = requiredStandards;
    }
    
    public String getDisplayName() { return displayName; }
    public Duration getRetentionPeriod() { return retentionPeriod; }
    public boolean requiresReporting() { return requiresReporting; }
    public Set<String> getRequiredStandards() { return Set.copyOf(requiredStandards); }
}

enum SecurityClassificationClean {
    PUBLIC("Public"),
    INTERNAL("Internal"),
    CONFIDENTIAL("Confidential"),
    SECRET("Secret"),
    TOP_SECRET("Top Secret");
    
    private final String displayName;
    
    SecurityClassificationClean(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
}

enum RiskLevelClean {
    LOW("Low", 10, 5),
    MEDIUM("Medium", 25, 15),
    HIGH("High", 40, 25),
    CRITICAL("Critical", 60, 40);
    
    private final String displayName;
    private final int baseScore;
    private final int impactScore;
    
    RiskLevelClean(String displayName, int baseScore, int impactScore) {
        this.displayName = displayName;
        this.baseScore = baseScore;
        this.impactScore = impactScore;
    }
    
    public String getDisplayName() { return displayName; }
    public int getBaseScore() { return baseScore; }
    public int getImpactScore() { return impactScore; }
}

enum EventCriticalityClean {
    NEGLIGIBLE("Negligible"),
    MINOR("Minor"),
    MAJOR("Major"),
    SEVERE("Severe"),
    CATASTROPHIC("Catastrophic");
    
    private final String displayName;
    
    EventCriticalityClean(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
}

enum EscalationActionClean {
    NOTIFY_SECURITY_TEAM("Notify Security Team"),
    NOTIFY_COMPLIANCE_OFFICER("Notify Compliance Officer"),
    NOTIFY_FRAUD_TEAM("Notify Fraud Team"),
    NOTIFY_SENIOR_MANAGEMENT("Notify Senior Management"),
    BLOCK_USER_SESSION("Block User Session"),
    FREEZE_ACCOUNT("Freeze Account"),
    GENERATE_COMPLIANCE_REPORT("Generate Compliance Report"),
    PREPARE_REGULATORY_FILING("Prepare Regulatory Filing"),
    ESCALATE_TO_LEGAL("Escalate to Legal"),
    CONTACT_LAW_ENFORCEMENT("Contact Law Enforcement");
    
    private final String displayName;
    
    EscalationActionClean(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
}

enum FraudIndicatorClean {
    ANOMALOUS_TRANSACTION_PATTERN("Anomalous Transaction Pattern"),
    MULTIPLE_FAILED_ATTEMPTS("Multiple Failed Attempts"),
    UNUSUAL_GEOGRAPHIC_LOCATION("Unusual Geographic Location"),
    UNRECOGNIZED_DEVICE("Unrecognized Device"),
    HIGH_VELOCITY_TRANSACTIONS("High Velocity Transactions"),
    SUSPICIOUS_IP_ADDRESS("Suspicious IP Address");
    
    private final String displayName;
    
    FraudIndicatorClean(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
}

enum FraudActionRecommendationClean {
    NO_ACTION_REQUIRED("No Action Required"),
    MONITOR_CLOSELY("Monitor Closely"),
    REQUIRE_ADDITIONAL_VERIFICATION("Require Additional Verification"),
    IMMEDIATE_BLOCK("Immediate Block");
    
    private final String displayName;
    
    FraudActionRecommendationClean(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
}

// Supporting value objects (simplified - would typically be separate files)

class ComplianceEvidencePackageClean {
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        public Builder eventId(String eventId) { return this; }
        public Builder correlationId(String correlationId) { return this; }
        public Builder timestamp(LocalDateTime timestamp) { return this; }
        public Builder complianceType(ComplianceTypeClean complianceType) { return this; }
        public Builder regulatoryRequirements(Set<String> regulatoryRequirements) { return this; }
        public Builder businessContext(BusinessContextClean businessContext) { return this; }
        public Builder technicalContext(TechnicalContextClean technicalContext) { return this; }
        public Builder evidenceFiles(List<String> evidenceFiles) { return this; }
        public Builder digitalSignature(String digitalSignature) { return this; }
        public Builder auditTrail(AuditTrailClean auditTrail) { return this; }
        public Builder retentionPolicy(Duration retentionPolicy) { return this; }
        public Builder legalHold(String legalHold) { return this; }
        public ComplianceEvidencePackageClean build() { return new ComplianceEvidencePackageClean(); }
    }
}

class FraudDetectionReportClean {
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        public Builder eventId(String eventId) { return this; }
        public Builder timestamp(LocalDateTime timestamp) { return this; }
        public Builder userId(String userId) { return this; }
        public Builder riskScore(Integer riskScore) { return this; }
        public Builder fraudIndicators(Set<FraudIndicatorClean> fraudIndicators) { return this; }
        public Builder financialAmount(BigDecimal financialAmount) { return this; }
        public Builder currency(String currency) { return this; }
        public Builder geographicLocation(String geographicLocation) { return this; }
        public Builder deviceFingerprint(String deviceFingerprint) { return this; }
        public Builder behaviorPattern(BehaviorPatternClean behaviorPattern) { return this; }
        public Builder recommendedAction(FraudActionRecommendationClean recommendedAction) { return this; }
        public FraudDetectionReportClean build() { return new FraudDetectionReportClean(); }
    }
}

class BusinessContextClean {
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        public Builder businessDomain(BusinessDomainClean businessDomain) { return this; }
        public Builder businessProcess(String businessProcess) { return this; }
        public Builder workflowId(String workflowId) { return this; }
        public Builder transactionId(String transactionId) { return this; }
        public Builder customerId(String customerId) { return this; }
        public Builder vendorId(String vendorId) { return this; }
        public Builder businessReason(String businessReason) { return this; }
        public BusinessContextClean build() { return new BusinessContextClean(); }
    }
}

class TechnicalContextClean {
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        public Builder sourceSystem(String sourceSystem) { return this; }
        public Builder serviceVersion(String serviceVersion) { return this; }
        public Builder ipAddress(String ipAddress) { return this; }
        public Builder userAgent(String userAgent) { return this; }
        public Builder requestMethod(String requestMethod) { return this; }
        public Builder requestUri(String requestUri) { return this; }
        public Builder processingTimeMs(Long processingTimeMs) { return this; }
        public Builder responseTimeMs(Long responseTimeMs) { return this; }
        public TechnicalContextClean build() { return new TechnicalContextClean(); }
    }
}

class AuditTrailClean {
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        public Builder eventId(String eventId) { return this; }
        public Builder timestamp(LocalDateTime timestamp) { return this; }
        public Builder userId(String userId) { return this; }
        public Builder action(String action) { return this; }
        public Builder result(AuditResultClean result) { return this; }
        public Builder description(String description) { return this; }
        public Builder beforeState(Map<String, Object> beforeState) { return this; }
        public Builder afterState(Map<String, Object> afterState) { return this; }
        public AuditTrailClean build() { return new AuditTrailClean(); }
    }
}

class BehaviorPatternClean {
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        public Builder userId(String userId) { return this; }
        public Builder sessionId(String sessionId) { return this; }
        public Builder timestamp(LocalDateTime timestamp) { return this; }
        public Builder action(String action) { return this; }
        public Builder result(AuditResultClean result) { return this; }
        public Builder riskScore(Integer riskScore) { return this; }
        public BehaviorPatternClean build() { return new BehaviorPatternClean(); }
    }
}