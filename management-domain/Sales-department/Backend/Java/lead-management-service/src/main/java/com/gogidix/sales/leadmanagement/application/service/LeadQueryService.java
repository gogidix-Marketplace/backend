package com.gogidix.sales.leadmanagement.application.service;

import com.gogidix.sales.leadmanagement.application.dto.response.LeadActivityResponseDto;
import com.gogidix.sales.leadmanagement.application.dto.response.LeadResponseDto;
import com.gogidix.sales.leadmanagement.domain.model.Lead;
import com.gogidix.sales.leadmanagement.domain.model.LeadActivity;
import com.gogidix.sales.leadmanagement.domain.repository.LeadActivityRepository;
import com.gogidix.sales.leadmanagement.domain.repository.LeadRepository;
import com.gogidix.sales.leadmanagement.shared.exception.NotFoundException;
import com.gogidix.sales.leadmanagement.shared.requestcontext.RequestContextHolder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lead Query Service
 * Handles all read operations for leads
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeadQueryService {

    private final LeadRepository leadRepository;
    private final LeadActivityRepository activityRepository;

    public Lead getById(String leadId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching lead: {} for tenant: {}", leadId, tenantId);

        return leadRepository.findByLeadIdAndTenantId(leadId, tenantId)
                .orElseThrow(() -> new NotFoundException("Lead", leadId));
    }

    public List<Lead> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all leads for tenant: {}", tenantId);

        return leadRepository.findAllByTenantId(tenantId);
    }

    public Page<Lead> getLeads(Pageable pageable) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching leads for tenant: {} with pagination", tenantId);

        List<Lead> leads = leadRepository.findAllByTenantId(tenantId);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), leads.size());

        List<Lead> pagedLeads = leads.subList(start, end);

        return new PageImpl<>(pagedLeads, pageable, leads.size());
    }

    public List<Lead> getLeadsByStatus(Lead.LeadStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching leads for tenant: {} with status: {}", tenantId, status);

        return leadRepository.findByTenantIdAndStatus(tenantId, status);
    }

    public List<Lead> getLeadsByStage(Lead.LeadStage stage) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching leads for tenant: {} with stage: {}", tenantId, stage);

        return leadRepository.findByTenantIdAndStage(tenantId, stage);
    }

    public List<Lead> getLeadsBySource(Lead.LeadSource source) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching leads for tenant: {} from source: {}", tenantId, source);

        return leadRepository.findByTenantIdAndSource(tenantId, source);
    }

    public List<Lead> getLeadsByQuality(Lead.LeadQuality quality) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching leads for tenant: {} with quality: {}", tenantId, quality);

        return leadRepository.findByTenantIdAndQuality(tenantId, quality);
    }

    public List<Lead> getLeadsByOwner(String ownerId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching leads for tenant: {} owned by: {}", tenantId, ownerId);

        return leadRepository.findByOwnerIdAndTenantId(ownerId, tenantId);
    }

    public List<Lead> getLeadsByMinScore(Integer minScore) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching leads for tenant: {} with score >= {}", tenantId, minScore);

        return leadRepository.findByTenantIdAndScoreGreaterThanEqual(tenantId, minScore);
    }

    public List<Lead> getLeadsNeedingFollowUp(LocalDate since) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching leads for tenant: {} needing follow-up since: {}", tenantId, since);

        return leadRepository.findLeadsNeedingFollowUp(tenantId, since);
    }

    public List<Lead> getStaleLeads(int staleDays) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching stale leads for tenant: {} with stale days: {}", tenantId, staleDays);

        return leadRepository.findStaleLeads(tenantId, staleDays);
    }

    public List<Lead> searchLeads(String searchTerm) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Searching leads for tenant: {} with term: {}", tenantId, searchTerm);

        return leadRepository.searchLeads(tenantId, searchTerm);
    }

    public List<Lead> getLeadsByTag(String tag) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching leads for tenant: {} with tag: {}", tenantId, tag);

        return leadRepository.findByTenantIdAndTagListContaining(tenantId, tag);
    }

    public List<LeadActivity> getLeadActivities(String leadId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching activities for lead: {} for tenant: {}", leadId, tenantId);

        // Verify lead exists
        getById(leadId);

        return activityRepository.findByLeadIdAndTenantIdOrderByCreatedAtDesc(leadId, tenantId);
    }

    /**
     * Get lead summary statistics
     */
    public LeadSummary getLeadSummary() {
        String tenantId = RequestContextHolder.getTenantId();

        long newLeads = leadRepository.countByTenantIdAndStage(tenantId, Lead.LeadStage.NEW);
        long contactedLeads = leadRepository.countByTenantIdAndStage(tenantId, Lead.LeadStage.CONTACTED);
        long qualifiedLeads = leadRepository.countByTenantIdAndStage(tenantId, Lead.LeadStage.QUALIFIED);
        long convertedLeads = leadRepository.countByTenantIdAndStage(tenantId, Lead.LeadStage.CONVERTED);
        long lostLeads = leadRepository.countByTenantIdAndStage(tenantId, Lead.LeadStage.LOST);

        long activeLeads = leadRepository.countByTenantIdAndStatus(tenantId, Lead.LeadStatus.ACTIVE);

        return LeadSummary.builder()
                .totalLeads((int) (newLeads + contactedLeads + qualifiedLeads + convertedLeads + lostLeads))
                .newLeads((int) newLeads)
                .contactedLeads((int) contactedLeads)
                .qualifiedLeads((int) qualifiedLeads)
                .convertedLeads((int) convertedLeads)
                .lostLeads((int) lostLeads)
                .activeLeads((int) activeLeads)
                .build();
    }

    /**
     * Convert Lead entity to Response DTO
     */
    public LeadResponseDto toResponseDto(Lead lead) {
        List<LeadActivity> activities = activityRepository.findByLeadIdAndTenantIdOrderByCreatedAtDesc(
                lead.getLeadId(), lead.getTenantId());

        LeadResponseDto dto = LeadResponseDto.fromEntity(lead);

        // Override activities with full list
        dto.setRecentActivities(activities.stream()
                .limit(5)
                .map(activity -> LeadResponseDto.ActivitySummary.builder()
                        .activityId(activity.getActivityId())
                        .activityType(activity.getActivityType() != null ? activity.getActivityType().name() : null)
                        .subject(activity.getSubject())
                        .status(activity.getStatus() != null ? activity.getStatus().name() : null)
                        .priority(activity.getPriority() != null ? activity.getPriority().name() : null)
                        .createdAt(activity.getCreatedAt())
                        .dueDate(activity.getDueDate())
                        .build())
                .collect(Collectors.toList()));

        return dto;
    }

    /**
     * Convert LeadActivity entity to Response DTO
     */
    public LeadActivityResponseDto toActivityResponseDto(LeadActivity activity) {
        return LeadActivityResponseDto.builder()
                .id(activity.getId())
                .activityId(activity.getActivityId())
                .leadId(activity.getLeadId())
                .tenantId(activity.getTenantId())
                .activityType(activity.getActivityType() != null ? activity.getActivityType().name() : null)
                .subject(activity.getSubject())
                .description(activity.getDescription())
                .createdBy(activity.getCreatedBy())
                .createdByName(activity.getCreatedByName())
                .dueDate(activity.getDueDate())
                .completedAt(activity.getCompletedAt())
                .priority(activity.getPriority() != null ? activity.getPriority().name() : null)
                .status(activity.getStatus() != null ? activity.getStatus().name() : null)
                .durationMinutes(activity.getDurationMinutes())
                .outcome(activity.getOutcome())
                .createdAt(activity.getCreatedAt())
                .updatedAt(activity.getUpdatedAt())
                .build();
    }

    @lombok.Data
    @Builder
    public static class LeadSummary {
        private Integer totalLeads;
        private Integer newLeads;
        private Integer contactedLeads;
        private Integer qualifiedLeads;
        private Integer convertedLeads;
        private Integer lostLeads;
        private Integer activeLeads;
    }
}
