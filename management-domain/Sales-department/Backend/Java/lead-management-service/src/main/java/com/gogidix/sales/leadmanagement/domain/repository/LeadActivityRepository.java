package com.gogidix.sales.leadmanagement.domain.repository;

import com.gogidix.sales.leadmanagement.domain.model.LeadActivity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Lead Activity Repository Interface
 * Defines the contract for lead activity persistence operations
 */
public interface LeadActivityRepository {

    LeadActivity save(LeadActivity activity);

    Optional<LeadActivity> findById(String id);

    Optional<LeadActivity> findByActivityIdAndTenantId(String activityId, String tenantId);

    List<LeadActivity> findByLeadIdAndTenantId(String leadId, String tenantId);

    List<LeadActivity> findByTenantIdAndCreatedBy(String tenantId, String createdBy);

    List<LeadActivity> findByTenantIdAndActivityType(String tenantId, LeadActivity.ActivityType activityType);

    List<LeadActivity> findByTenantIdAndStatus(String tenantId, LeadActivity.ActivityStatus status);

    List<LeadActivity> findPendingActivitiesByDueDateBefore(String tenantId, Instant dueDate);

    List<LeadActivity> findOverdueActivities(String tenantId);

    void deleteById(String id);

    void deleteByLeadIdAndTenantId(String leadId, String tenantId);

    void deleteByActivityIdAndTenantId(String activityId, String tenantId);

    boolean existsByActivityIdAndTenantId(String activityId, String tenantId);

    long countByLeadIdAndTenantId(String leadId, String tenantId);

    /**
     * Find recent activities for a lead
     */
    List<LeadActivity> findRecentActivitiesByLeadId(String leadId, String tenantId, int limit);

    /**
     * Find upcoming activities for a user
     */
    List<LeadActivity> findUpcomingActivitiesForUser(String tenantId, String userId, Instant since);

    /**
     * Count activities by type for a lead
     */
    long countByLeadIdAndTenantIdAndActivityType(String leadId, String tenantId, LeadActivity.ActivityType activityType);

    List<LeadActivity> findByLeadIdAndTenantIdOrderByCreatedAtDesc(String leadId, String tenantId);
}
