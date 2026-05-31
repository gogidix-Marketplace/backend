package com.gogidix.platform.subscription.domain.repository;

import com.gogidix.platform.subscription.domain.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for Subscription entity.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    /**
     * Find subscriptions by tenant.
     */
    List<Subscription> findByTenantId(String tenantId);

    /**
     * Find active subscriptions by tenant.
     */
    List<Subscription> findByTenantIdAndStatus(String tenantId, String status);

    /**
     * Find subscription by tenant and plan.
     */
    List<Subscription> findByTenantIdAndPlanId(String tenantId, String planId);
}
