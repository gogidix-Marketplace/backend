package com.gogidix.platform.subscription.application.service;

import com.gogidix.platform.subscription.domain.model.Subscription;
import com.gogidix.platform.subscription.domain.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Application service for Subscription operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    /**
     * Create a new subscription.
     */
    @Transactional
    public Subscription createSubscription(Subscription subscription) {
        log.info("Creating subscription: {}", subscription.getId());
        return subscriptionRepository.save(subscription);
    }

    /**
     * Get subscription by ID.
     */
    @Transactional(readOnly = true)
    public Subscription getSubscription(UUID id) {
        return subscriptionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + id));
    }

    /**
     * Get subscriptions by tenant.
     */
    @Transactional(readOnly = true)
    public List<Subscription> getSubscriptionsByTenant(String tenantId) {
        return subscriptionRepository.findByTenantId(tenantId);
    }

    /**
     * Activate subscription.
     */
    @Transactional
    public void activateSubscription(UUID id) {
        log.info("Activating subscription: {}", id);
        Subscription subscription = getSubscription(id);
        subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
        subscriptionRepository.save(subscription);
    }

    /**
     * Cancel subscription.
     */
    @Transactional
    public void cancelSubscription(UUID id) {
        log.info("Cancelling subscription: {}", id);
        Subscription subscription = getSubscription(id);
        subscription.cancel("Cancelled by service");
        subscriptionRepository.save(subscription);
    }
}
