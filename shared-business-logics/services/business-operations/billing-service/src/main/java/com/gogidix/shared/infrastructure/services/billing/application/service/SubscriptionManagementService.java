package com.gogidix.shared.infrastructure.services.billing.application.service;

import com.gogidix.shared.infrastructure.services.billing.application.port.in.SubscriptionManagementPort;
import com.gogidix.shared.infrastructure.services.billing.domain.aggregate.BillingRegistry;
import com.gogidix.shared.infrastructure.services.billing.domain.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Application service for subscription management.
 * Implements the SubscriptionManagementPort using the BillingRegistry aggregate.
 */
@Service
public class SubscriptionManagementService implements SubscriptionManagementPort {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionManagementService.class);

    private final BillingRegistry billingRegistry;

    public SubscriptionManagementService(BillingRegistry billingRegistry) {
        this.billingRegistry = billingRegistry;
    }

    @Override
    public Subscription createSubscription(String tenantId,
                                         Subscription.SubscriptionPlan plan,
                                         Subscription.BillingPeriod billingPeriod,
                                         String paymentMethodId) {
        log.info("Creating subscription: tenantId={}, plan={}, period={}", tenantId, plan, billingPeriod);
        return billingRegistry.createSubscription(tenantId, plan, billingPeriod, paymentMethodId);
    }

    @Override
    public Optional<Subscription> updateSubscriptionPlan(String subscriptionId,
                                                         Subscription.SubscriptionPlan newPlan) {
        log.info("Updating subscription plan: subscriptionId={}, newPlan={}", subscriptionId, newPlan);
        return billingRegistry.updateSubscriptionPlan(subscriptionId, newPlan);
    }

    @Override
    public boolean cancelSubscription(String subscriptionId, String reason, boolean effectiveImmediately) {
        log.info("Cancelling subscription: subscriptionId={}, reason={}, immediate={}",
                 subscriptionId, reason, effectiveImmediately);
        return billingRegistry.cancelSubscription(subscriptionId, reason, effectiveImmediately);
    }

    @Override
    public Optional<Subscription> getSubscription(String subscriptionId) {
        return billingRegistry.getSubscription(subscriptionId);
    }

    @Override
    public Optional<Subscription> getTenantSubscription(String tenantId) {
        return billingRegistry.getTenantSubscription(tenantId);
    }

    @Override
    public Optional<Subscription> changeSubscriptionPlan(String subscriptionId,
                                                         Subscription.SubscriptionPlan newPlan) {
        log.info("Changing subscription plan: subscriptionId={}, newPlan={}", subscriptionId, newPlan);
        return billingRegistry.updateSubscriptionPlan(subscriptionId, newPlan);
    }

    @Override
    public Optional<Subscription> renewSubscription(String subscriptionId) {
        log.info("Renewing subscription: subscriptionId={}", subscriptionId);
        Optional<Subscription> subscriptionOpt = billingRegistry.getSubscription(subscriptionId);

        if (subscriptionOpt.isEmpty()) {
            log.warn("Subscription not found for renewal: {}", subscriptionId);
            return Optional.empty();
        }

        Subscription subscription = subscriptionOpt.get();

        if (subscription.getEndDate() != null && LocalDateTime.now().isAfter(subscription.getEndDate())) {
            // Subscription has expired, reactivate it
            subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
            subscription.setStartDate(LocalDateTime.now());
            subscription.setEndDate(null);
            subscription.calculateNextBillingDate();
            log.info("Subscription renewed: subscriptionId={}", subscriptionId);
        }

        return Optional.of(subscription);
    }

    @Override
    public boolean pauseSubscription(String subscriptionId) {
        log.info("Pausing subscription: subscriptionId={}", subscriptionId);
        Optional<Subscription> subscriptionOpt = billingRegistry.getSubscription(subscriptionId);

        if (subscriptionOpt.isEmpty()) {
            log.warn("Subscription not found for pause: {}", subscriptionId);
            return false;
        }

        Subscription subscription = subscriptionOpt.get();
        subscription.setStatus(Subscription.SubscriptionStatus.SUSPENDED);
        subscription.setUpdatedAt(LocalDateTime.now());

        log.info("Subscription paused: subscriptionId={}", subscriptionId);
        return true;
    }

    @Override
    public boolean resumeSubscription(String subscriptionId) {
        log.info("Resuming subscription: subscriptionId={}", subscriptionId);
        Optional<Subscription> subscriptionOpt = billingRegistry.getSubscription(subscriptionId);

        if (subscriptionOpt.isEmpty()) {
            log.warn("Subscription not found for resume: {}", subscriptionId);
            return false;
        }

        Subscription subscription = subscriptionOpt.get();
        subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
        subscription.setUpdatedAt(LocalDateTime.now());
        subscription.calculateNextBillingDate();

        log.info("Subscription resumed: subscriptionId={}", subscriptionId);
        return true;
    }

    @Override
    public List<Subscription> getTenantSubscriptions(String tenantId) {
        // Implementation would query repository for all tenant subscriptions
        // For now, return empty list
        return List.of();
    }
}
