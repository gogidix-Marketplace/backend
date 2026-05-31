package com.gogidix.platform.subscription.infrastructure.persistence.postgres;

import com.gogidix.platform.subscription.domain.model.Subscription;
import com.gogidix.platform.subscription.domain.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of SubscriptionRepository.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PostgresSubscriptionRepository {

    private final SubscriptionJpaRepository jpaRepository;

    public Subscription save(Subscription subscription) {
        log.debug("Saving subscription: id={}", subscription.getId());
        return jpaRepository.save(subscription);
    }

    public Optional<Subscription> findById(String id) {
        return jpaRepository.findById(id);
    }

    public Optional<Subscription> findBySubscriptionNumber(String subscriptionNumber) {
        return jpaRepository.findBySubscriptionNumber(subscriptionNumber);
    }

    public Optional<Subscription> findByCustomerId(String customerId) {
        return jpaRepository.findByCustomerId(customerId);
    }

    public List<Subscription> findByTenantId(String tenantId) {
        return jpaRepository.findByTenantId(tenantId);
    }

    public List<Subscription> findByTenantIdAndStatus(String tenantId, Subscription.SubscriptionStatus status) {
        return jpaRepository.findByTenantIdAndStatus(tenantId, status);
    }

    public List<Subscription> findByPlanId(String planId) {
        return jpaRepository.findByPlanId(planId);
    }

    public void delete(Subscription subscription) {
        jpaRepository.delete(subscription);
    }

    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    public List<Subscription> findAll() {
        return jpaRepository.findAll();
    }

    public long countByTenantIdAndStatus(String tenantId, Subscription.SubscriptionStatus status) {
        return jpaRepository.countByTenantIdAndStatus(tenantId, status);
    }
}
