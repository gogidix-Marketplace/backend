package com.gogidix.platform.subscription.domain.repository;

import com.gogidix.platform.subscription.domain.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for SubscriptionPlan entity.
 */
@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, String> {

    /**
     * Find active plans
     */
    List<SubscriptionPlan> findByIsActiveTrue();

    /**
     * Find plan by code
     */
    Optional<SubscriptionPlan> findByPlanCode(String planCode);

    /**
     * Find plans by tier
     */
    List<SubscriptionPlan> findByTier(String tier);
}
