package com.gogidix.platform.subscription.infrastructure.persistence.postgres;

import com.gogidix.platform.subscription.domain.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Subscription entity.
 */
@Repository
public interface SubscriptionJpaRepository extends JpaRepository<Subscription, String> {

    Optional<Subscription> findBySubscriptionNumber(String subscriptionNumber);

    Optional<Subscription> findByCustomerId(String customerId);

    List<Subscription> findByTenantId(String tenantId);

    List<Subscription> findByTenantIdAndStatus(String tenantId, Subscription.SubscriptionStatus status);

    List<Subscription> findByPlanId(String planId);

    long countByTenantIdAndStatus(String tenantId, Subscription.SubscriptionStatus status);
}
