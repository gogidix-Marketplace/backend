package com.gogidix.sales.crm.application.service;

import com.gogidix.sales.crm.domain.model.Interaction;
import com.gogidix.sales.crm.domain.repository.InteractionRepository;
import com.gogidix.sales.crm.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interaction Query Service
 * Handles all read operations for interactions
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InteractionQueryService {

    private final InteractionRepository interactionRepository;

    public Interaction getById(String interactionId) {
        log.debug("Getting interaction by id: {}", interactionId);
        return interactionRepository.findById(interactionId)
            .orElseThrow(() -> new NotFoundException("Interaction", interactionId));
    }

    public Interaction getByInteractionIdAndTenantId(String interactionId, String tenantId) {
        log.debug("Getting interaction: {} for tenant: {}", interactionId, tenantId);
        return interactionRepository.findByInteractionIdAndTenantId(interactionId, tenantId)
            .orElseThrow(() -> new NotFoundException("Interaction", interactionId));
    }

    public List<Interaction> getAllForTenant(String tenantId) {
        log.debug("Getting all interactions for tenant: {}", tenantId);
        return interactionRepository.findByTenantId(tenantId);
    }

    public Page<Interaction> getPaginatedForTenant(String tenantId, Pageable pageable) {
        log.debug("Getting paginated interactions for tenant: {}", tenantId);
        return interactionRepository.findByTenantId(tenantId, pageable);
    }

    public List<Interaction> getByCustomerId(String tenantId, String customerId) {
        log.debug("Getting interactions for customer: {} in tenant: {}", customerId, tenantId);
        return interactionRepository.findByTenantIdAndCustomerId(tenantId, customerId);
    }

    public List<Interaction> getByContactId(String tenantId, String contactId) {
        log.debug("Getting interactions for contact: {} in tenant: {}", contactId, tenantId);
        return interactionRepository.findByContactIdAndTenantId(contactId, tenantId);
    }

    public List<Interaction> getByType(String tenantId, Interaction.InteractionType type) {
        log.debug("Getting interactions by type: {} for tenant: {}", type, tenantId);
        return interactionRepository.findByTenantIdAndType(tenantId, type);
    }

    public List<Interaction> getByStatus(String tenantId, Interaction.InteractionStatus status) {
        log.debug("Getting interactions by status: {} for tenant: {}", status, tenantId);
        return interactionRepository.findByTenantIdAndStatus(tenantId, status);
    }

    public List<Interaction> getByAssignedTo(String tenantId, String assignedTo) {
        log.debug("Getting interactions assigned to: {} for tenant: {}", assignedTo, tenantId);
        return interactionRepository.findByTenantIdAndAssignedTo(tenantId, assignedTo);
    }

    public List<Interaction> getByDateRange(String tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Getting interactions by date range: {} to {} for tenant: {}", startDate, endDate, tenantId);
        return interactionRepository.findByTenantIdAndInteractionDateBetween(tenantId, startDate, endDate);
    }

    public List<Interaction> getUpcomingInteractions(String tenantId, LocalDateTime from) {
        log.debug("Getting upcoming interactions from: {} for tenant: {}", from, tenantId);
        return interactionRepository.findByTenantIdAndInteractionDateAfter(tenantId, from);
    }

    public List<Interaction> getOverdueInteractions(String tenantId) {
        log.debug("Getting overdue interactions for tenant: {}", tenantId);
        return interactionRepository.findByTenantIdAndStatusAndInteractionDateBefore(
            tenantId, Interaction.InteractionStatus.SCHEDULED, LocalDateTime.now());
    }

    public List<Interaction> getInteractionsNeedingFollowUp(String tenantId, LocalDate beforeDate) {
        log.debug("Getting interactions needing follow-up before: {} for tenant: {}", beforeDate, tenantId);
        return interactionRepository.findByTenantIdAndHasFollowUpTrueAndFollowUpDateBefore(tenantId, beforeDate);
    }

    public List<Interaction> getByDealId(String tenantId, String dealId) {
        log.debug("Getting interactions for deal: {} in tenant: {}", dealId, tenantId);
        return interactionRepository.findByTenantIdAndDealId(tenantId, dealId);
    }

    public List<Interaction> getHighPriorityInteractions(String tenantId) {
        log.debug("Getting high priority interactions for tenant: {}", tenantId);
        return interactionRepository.findByTenantIdAndIsHighPriorityTrue(tenantId);
    }

    public Page<Interaction> searchInteractions(String tenantId, String searchTerm, Pageable pageable) {
        log.debug("Searching interactions: {} for tenant: {}", searchTerm, tenantId);
        return interactionRepository.findByTenantIdAndSubjectContainingIgnoreCase(tenantId, searchTerm, pageable);
    }

    public List<Interaction> getRecentInteractions(String tenantId, Pageable pageable) {
        log.debug("Getting recent interactions for tenant: {}", tenantId);
        return interactionRepository.findByTenantIdOrderByInteractionDateDesc(tenantId, pageable);
    }

    public InteractionSummary getSummary(String tenantId) {
        log.debug("Getting interaction summary for tenant: {}", tenantId);

        long totalInteractions = interactionRepository.countByTenantId(tenantId);
        long scheduledCount = interactionRepository.countByTenantIdAndStatus(
            tenantId, Interaction.InteractionStatus.SCHEDULED);
        long completedCount = interactionRepository.countByTenantIdAndStatus(
            tenantId, Interaction.InteractionStatus.COMPLETED);
        long overdueCount = interactionRepository.findByTenantIdAndStatusAndInteractionDateBefore(
            tenantId, Interaction.InteractionStatus.SCHEDULED, LocalDateTime.now()).size();

        return new InteractionSummary(totalInteractions, scheduledCount, completedCount, overdueCount);
    }

    /**
     * Interaction Summary DTO
     */
    public static class InteractionSummary {
        private Long totalInteractions;
        private Long scheduledCount;
        private Long completedCount;
        private Long overdueCount;

        public InteractionSummary(Long totalInteractions, Long scheduledCount,
                                   Long completedCount, Long overdueCount) {
            this.totalInteractions = totalInteractions;
            this.scheduledCount = scheduledCount;
            this.completedCount = completedCount;
            this.overdueCount = overdueCount;
        }

        public Long getTotalInteractions() { return totalInteractions; }
        public void setTotalInteractions(Long totalInteractions) { this.totalInteractions = totalInteractions; }
        public Long getScheduledCount() { return scheduledCount; }
        public void setScheduledCount(Long scheduledCount) { this.scheduledCount = scheduledCount; }
        public Long getCompletedCount() { return completedCount; }
        public void setCompletedCount(Long completedCount) { this.completedCount = completedCount; }
        public Long getOverdueCount() { return overdueCount; }
        public void setOverdueCount(Long overdueCount) { this.overdueCount = overdueCount; }
    }
}
