package com.gogidix.customersupport.slamanagement.application.service;

import com.gogidix.customersupport.slamanagement.application.dto.SLABreachResponseDto;
import com.gogidix.customersupport.slamanagement.application.dto.SLAPolicyRequestDto;
import com.gogidix.customersupport.slamanagement.application.dto.SLAPolicyResponseDto;
import com.gogidix.customersupport.slamanagement.application.mapper.SLABreachMapper;
import com.gogidix.customersupport.slamanagement.application.mapper.SLAPolicyMapper;
import com.gogidix.customersupport.slamanagement.domain.model.SLABreach;
import com.gogidix.customersupport.slamanagement.domain.model.SLAPolicy;
import com.gogidix.customersupport.slamanagement.domain.repository.SLABreachRepository;
import com.gogidix.customersupport.slamanagement.domain.repository.SLAPolicyRepository;
import com.gogidix.customersupport.slamanagement.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlaManagementService {

    private final SLAPolicyRepository slaPolicyRepository;
    private final SLABreachRepository slaBreachRepository;
    private final SLAPolicyMapper slaPolicyMapper;
    private final SLABreachMapper slaBreachMapper;

    public List<SLAPolicyResponseDto> getAllPolicies() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all SLA policies for tenant: {}", tenantId);
        return slaPolicyRepository.findByTenantId(tenantId).stream()
                .map(slaPolicyMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<SLAPolicyResponseDto> getActivePolicies() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching active SLA policies for tenant: {}", tenantId);
        return slaPolicyRepository.findByTenantIdAndIsActiveTrue(tenantId).stream()
                .map(slaPolicyMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public SLAPolicyResponseDto getPolicyById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching SLA policy with id: {} for tenant: {}", id, tenantId);
        SLAPolicy policy = slaPolicyRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("SLA policy not found with id: " + id));
        return slaPolicyMapper.toResponseDto(policy);
    }

    public SLAPolicyResponseDto getPolicyByCode(String policyCode) {
        log.debug("Fetching SLA policy with code: {}", policyCode);
        SLAPolicy policy = slaPolicyRepository.findByPolicyCode(policyCode)
                .orElseThrow(() -> new IllegalArgumentException("SLA policy not found with code: " + policyCode));
        return slaPolicyMapper.toResponseDto(policy);
    }

    @Transactional
    public SLAPolicyResponseDto createPolicy(SLAPolicyRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Creating new SLA policy for tenant: {}", tenantId);

        if (slaPolicyRepository.existsByPolicyCode(request.getPolicyCode())) {
            throw new IllegalArgumentException("SLA policy with code " + request.getPolicyCode() + " already exists");
        }

        SLAPolicy policy = slaPolicyMapper.toEntity(request, tenantId);
        SLAPolicy saved = slaPolicyRepository.save(policy);
        log.info("Created SLA policy with code: {} for tenant: {}", saved.getPolicyCode(), tenantId);
        return slaPolicyMapper.toResponseDto(saved);
    }

    @Transactional
    public SLAPolicyResponseDto updatePolicy(String id, SLAPolicyRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Updating SLA policy with id: {} for tenant: {}", id, tenantId);

        SLAPolicy existing = slaPolicyRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("SLA policy not found with id: " + id));

        SLAPolicy updated = slaPolicyMapper.toEntity(request, tenantId);
        updated.setId(existing.getId());
        updated.setCreatedAt(existing.getCreatedAt());
        updated.setUpdatedAt(java.time.Instant.now());

        SLAPolicy saved = slaPolicyRepository.save(updated);
        log.info("Updated SLA policy with code: {} for tenant: {}", saved.getPolicyCode(), tenantId);
        return slaPolicyMapper.toResponseDto(saved);
    }

    @Transactional
    public void deletePolicy(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Deleting SLA policy with id: {} for tenant: {}", id, tenantId);

        SLAPolicy policy = slaPolicyRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("SLA policy not found with id: " + id));

        slaPolicyRepository.deleteByTenantIdAndId(tenantId, id);
        log.info("Deleted SLA policy with code: {} for tenant: {}", policy.getPolicyCode(), tenantId);
    }

    public List<SLABreachResponseDto> getAllBreaches() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all SLA breaches for tenant: {}", tenantId);
        return slaBreachRepository.findByTenantIdOrderByBreachDateTimeDesc(tenantId).stream()
                .map(slaBreachMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<SLABreachResponseDto> getBreachesByTicketId(String ticketId) {
        log.debug("Fetching SLA breaches for ticket: {}", ticketId);
        return slaBreachRepository.findByTicketId(ticketId).stream()
                .map(slaBreachMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<SLABreachResponseDto> getUnresolvedBreaches() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching unresolved SLA breaches for tenant: {}", tenantId);
        return slaBreachRepository.findByTenantIdAndResolvedAtIsNull(tenantId).stream()
                .map(slaBreachMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SLABreachResponseDto recordBreach(String ticketId, String ticketNumber, String slaPolicyId,
                                             String slaPolicyName, SLABreach.BreachType breachType,
                                             Instant dueDateTime, Integer targetTimeMinutes) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Recording SLA breach for ticket: {} for tenant: {}", ticketId, tenantId);

        Instant now = Instant.now();
        long overdueMinutes = ChronoUnit.MINUTES.between(dueDateTime, now);

        SLABreach breach = SLABreach.create(
                tenantId, ticketId, ticketNumber, slaPolicyId, slaPolicyName,
                breachType, now, dueDateTime, targetTimeMinutes
        );
        breach.setOverdueByMinutes(overdueMinutes);

        SLABreach saved = slaBreachRepository.save(breach);
        log.info("Recorded SLA breach for ticket: {}", ticketNumber);
        return slaBreachMapper.toResponseDto(saved);
    }

    @Transactional
    public SLABreachResponseDto markBreachAsNotified(String breachId) {
        log.debug("Marking SLA breach: {} as notified", breachId);
        SLABreach breach = slaBreachRepository.findById(breachId)
                .orElseThrow(() -> new IllegalArgumentException("SLA breach not found with id: " + breachId));

        breach.markAsNotified();
        SLABreach updated = slaBreachRepository.save(breach);

        log.info("Marked SLA breach: {} as notified", breachId);
        return slaBreachMapper.toResponseDto(updated);
    }

    @Transactional
    public SLABreachResponseDto triggerEscalation(String breachId, Integer level) {
        log.debug("Triggering escalation for SLA breach: {} at level: {}", breachId, level);
        SLABreach breach = slaBreachRepository.findById(breachId)
                .orElseThrow(() -> new IllegalArgumentException("SLA breach not found with id: " + breachId));

        breach.triggerEscalation(level);
        SLABreach updated = slaBreachRepository.save(breach);

        log.info("Triggered escalation for SLA breach: {} at level: {}", breachId, level);
        return slaBreachMapper.toResponseDto(updated);
    }

    @Transactional
    public SLABreachResponseDto resolveBreach(String breachId, String resolutionNotes) {
        log.debug("Resolving SLA breach: {}", breachId);
        SLABreach breach = slaBreachRepository.findById(breachId)
                .orElseThrow(() -> new IllegalArgumentException("SLA breach not found with id: " + breachId));

        breach.resolve(resolutionNotes);
        SLABreach updated = slaBreachRepository.save(breach);

        log.info("Resolved SLA breach: {}", breachId);
        return slaBreachMapper.toResponseDto(updated);
    }

    public double calculateComplianceRate(String slaPolicyId) {
        SLAPolicy policy = slaPolicyRepository.findById(slaPolicyId)
                .orElseThrow(() -> new IllegalArgumentException("SLA policy not found with id: " + slaPolicyId));

        List<SLABreach> breaches = slaBreachRepository.findByTenantIdAndSlaPolicyId(policy.getTenantId(), slaPolicyId);
        long totalBreaches = breaches.size();
        long resolvedBreaches = breaches.stream().filter(b -> b.getResolvedAt() != null).count();

        if (totalBreaches == 0) {
            return 100.0;
        }

        return ((double) (totalBreaches - resolvedBreaches) / totalBreaches) * 100;
    }

    public long getBreachCountByType(SLABreach.BreachType breachType) {
        String tenantId = RequestContextHolder.getTenantId();
        return slaBreachRepository.countByTenantIdAndBreachType(tenantId, breachType);
    }
}
