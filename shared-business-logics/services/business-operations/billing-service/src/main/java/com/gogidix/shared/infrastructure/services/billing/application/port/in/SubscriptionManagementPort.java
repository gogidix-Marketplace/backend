package com.gogidix.shared.infrastructure.services.billing.application.port.in;

import com.gogidix.shared.infrastructure.services.billing.domain.model.Subscription;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Input port for subscription management operations.
 * Defines the contract for subscription use cases.
 */
public interface SubscriptionManagementPort {

    /**
     * Create a new subscription for a tenant.
     */
    Subscription createSubscription(String tenantId,
                                   Subscription.SubscriptionPlan plan,
                                   Subscription.BillingPeriod billingPeriod,
                                   String paymentMethodId);

    /**
     * Update subscription plan.
     */
    Optional<Subscription> updateSubscriptionPlan(String subscriptionId,
                                                  Subscription.SubscriptionPlan newPlan);

    /**
     * Cancel subscription.
     */
    boolean cancelSubscription(String subscriptionId, String reason, boolean effectiveImmediately);

    /**
     * Get subscription by ID.
     */
    Optional<Subscription> getSubscription(String subscriptionId);

    /**
     * Get active subscription for a tenant.
     */
    Optional<Subscription> getTenantSubscription(String tenantId);

    /**
     * Upgrade/downgrade subscription.
     */
    Optional<Subscription> changeSubscriptionPlan(String subscriptionId,
                                                  Subscription.SubscriptionPlan newPlan);

    /**
     * Renew subscription.
     */
    Optional<Subscription> renewSubscription(String subscriptionId);

    /**
     * Pause subscription.
     */
    boolean pauseSubscription(String subscriptionId);

    /**
     * Resume paused subscription.
     */
    boolean resumeSubscription(String subscriptionId);

    /**
     * Get all subscriptions for a tenant.
     */
    List<Subscription> getTenantSubscriptions(String tenantId);
}
