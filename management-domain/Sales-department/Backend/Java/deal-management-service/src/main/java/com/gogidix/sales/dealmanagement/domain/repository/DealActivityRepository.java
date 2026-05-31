package com.gogidix.sales.dealmanagement.domain.repository;

import com.gogidix.sales.dealmanagement.domain.model.DealActivity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Deal Activity Repository Interface
 * Defines the contract for deal activity persistence operations
 */
public interface DealActivityRepository {

    DealActivity save(DealActivity activity);

    Optional<DealActivity> findById(String id);

    Optional<DealActivity> findByActivityIdAndTenantId(String activityId, String tenantId);

    List<DealActivity> findByDealIdAndTenantId(String dealId, String tenantId);

    List<DealActivity> findByDealIdAndTenantIdOrderByActivityDateDesc(
            String dealId, String tenantId);

    List<DealActivity> findByUserIdAndTenantId(String userId, String tenantId);

    List<DealActivity> findByTenantIdAndDueDateBefore(String tenantId, Instant dueDate);

    List<DealActivity> findPendingActivitiesByTenantId(String tenantId);

    void deleteById(String id);

    void deleteByActivityIdAndTenantId(String activityId, String tenantId);

    void deleteByDealIdAndTenantId(String dealId, String tenantId);

    boolean existsByActivityIdAndTenantId(String activityId, String tenantId);
}
