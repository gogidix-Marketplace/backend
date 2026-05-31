package com.gogidix.shared.infrastructure.services.billing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Subscription domain model.
 * Represents a tenant's subscription plan and billing status.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    private String subscriptionId;
    private String tenantId;
    private SubscriptionPlan plan;
    private SubscriptionStatus status;
    private BillingPeriod billingPeriod;
    private BigDecimal monthlyPrice;
    private BigDecimal yearlyPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime trialEndDate;
    private String currency;
    private int maxUsers;
    private long maxStorageGB;
    private boolean autoRenew;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String paymentMethodId;
    private LocalDateTime lastBillingDate;
    private LocalDateTime nextBillingDate;
    private BigDecimal currentBalance;

    /**
     * Subscription status.
     */
    public enum SubscriptionStatus {
        ACTIVE,
        TRIAL,
        PAST_DUE,
        CANCELLED,
        SUSPENDED,
        PENDING
    }

    /**
     * Subscription plans.
     */
    public enum SubscriptionPlan {
        FREE,
        STARTER,
        PROFESSIONAL,
        ENTERPRISE,
        CUSTOM
    }

    /**
     * Billing period.
     */
    public enum BillingPeriod {
        MONTHLY,
        YEARLY,
        CUSTOM
    }

    /**
     * Check if subscription is currently active.
     */
    public boolean isActive() {
        if (status == SubscriptionStatus.CANCELLED || status == SubscriptionStatus.SUSPENDED) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        if (status == SubscriptionStatus.TRIAL && trialEndDate != null) {
            return now.isBefore(trialEndDate);
        }

        if (status == SubscriptionStatus.ACTIVE && endDate != null) {
            return now.isBefore(endDate);
        }

        return status == SubscriptionStatus.ACTIVE;
    }

    /**
     * Check if subscription is in trial period.
     */
    public boolean isTrial() {
        return status == SubscriptionStatus.TRIAL &&
               trialEndDate != null &&
               LocalDateTime.now().isBefore(trialEndDate);
    }

    /**
     * Get days remaining in current period.
     */
    public long getDaysRemaining() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reference = (status == SubscriptionStatus.TRIAL && trialEndDate != null)
            ? trialEndDate
            : endDate;

        if (reference == null) {
            return Long.MAX_VALUE;
        }

        return java.time.Duration.between(now, reference).toDays();
    }

    /**
     * Calculate next billing date.
     */
    public void calculateNextBillingDate() {
        LocalDateTime base = (lastBillingDate != null) ? lastBillingDate : startDate;
        if (base == null) {
            base = LocalDateTime.now();
        }

        if (billingPeriod == BillingPeriod.MONTHLY) {
            nextBillingDate = base.plusMonths(1);
        } else if (billingPeriod == BillingPeriod.YEARLY) {
            nextBillingDate = base.plusYears(1);
        }
    }

    /**
     * Get the price for the current billing period.
     */
    public BigDecimal getCurrentPrice() {
        return billingPeriod == BillingPeriod.YEARLY
            ? yearlyPrice
            : monthlyPrice;
    }

    /**
     * Create a new subscription ID.
     */
    public static String generateSubscriptionId() {
        return "sub-" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Check if user count is within limit.
     */
    public boolean canAddUsers(int currentUsers) {
        return maxUsers == -1 || currentUsers < maxUsers;
    }

    /**
     * Check if storage is within limit.
     */
    public boolean canAddStorage(long currentStorageGB) {
        return maxStorageGB == -1 || currentStorageGB < maxStorageGB;
    }
}
