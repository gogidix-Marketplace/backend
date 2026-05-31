package com.gogidix.platform.subscription.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Subscription entity.
 *
 * Manages customer subscriptions including:
 * - Subscription lifecycle
 * - Trial management
 * - Billing cycles
 * - Usage tracking
 * - Auto-renewal
 */
@Entity
@Table(name = "subscriptions", indexes = {
    @Index(name = "idx_subscriptions_tenant", columnList = "tenant_id"),
    @Index(name = "idx_subscriptions_customer", columnList = "customer_id"),
    @Index(name = "idx_subscriptions_plan", columnList = "plan_id"),
    @Index(name = "idx_subscriptions_status", columnList = "status"),
    @Index(name = "idx_subscriptions_period", columnList = "current_period_start, current_period_end"),
    @Index(name = "idx_subscriptions_number", columnList = "subscription_number")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Human-readable subscription number
     */
    @Column(name = "subscription_number", nullable = false, unique = true, length = 100)
    private String subscriptionNumber;

    /**
     * Customer ID from tenant's system
     */
    @Column(name = "customer_id", nullable = false, length = 255)
    private String customerId;

    /**
     * Reference to subscription plan
     */
    @Column(name = "plan_id", nullable = false)
    private String planId;

    /**
     * Current subscription status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private SubscriptionStatus status;

    /**
     * Trial period dates
     */
    @Column(name = "trial_start")
    private LocalDate trialStart;

    @Column(name = "trial_end")
    private LocalDate trialEnd;

    /**
     * Current billing period
     */
    @Column(name = "current_period_start", nullable = false)
    private LocalDateTime currentPeriodStart;

    @Column(name = "current_period_end", nullable = false)
    private LocalDateTime currentPeriodEnd;

    /**
     * Billing cycle
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false, length = 50)
    private BillingCycle billingCycle;

    /**
     * Pricing details
     */
    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @Column(name = "tax_rate", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency", length = 3)
    @Builder.Default
    private String currency = "USD";

    /**
     * Current usage metrics
     */
    @Column(name = "current_users")
    @Builder.Default
    private Integer currentUsers = 0;

    @Column(name = "current_storage_gb", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal currentStorageGb = BigDecimal.ZERO;

    /**
     * Additional usage metrics (JSON)
     */
    @Convert(converter = com.gogidix.platform.subscription.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "usage_json")
    @Builder.Default
    private Map<String, Object> usageJson = new HashMap<>();

    /**
     * Trial information
     */
    @Column(name = "is_trial", nullable = false)
    @Builder.Default
    private boolean isTrial = false;

    @Column(name = "trial_days_remaining")
    private Integer trialDaysRemaining;

    /**
     * Auto-renewal settings
     */
    @Column(name = "auto_renew", nullable = false)
    @Builder.Default
    private boolean autoRenew = true;

    @Column(name = "cancel_at_period_end", nullable = false)
    @Builder.Default
    private boolean cancelAtPeriodEnd = false;

    /**
     * Cancellation details
     */
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Lob
    @Column(name = "cancellation_reason")
    private String cancellationReason;

    /**
     * Additional metadata
     */
    @Convert(converter = com.gogidix.platform.subscription.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "metadata")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Stripe subscription ID
     */
    @Column(name = "stripe_subscription_id", length = 255)
    private String stripeSubscriptionId;

    /**
     * Stripe customer ID
     */
    @Column(name = "stripe_customer_id", length = 255)
    private String stripeCustomerId;

    /**
     * Audit fields
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        if (subscriptionNumber == null) {
            subscriptionNumber = generateSubscriptionNumber();
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Check if subscription is in trial
     */
    public boolean isInTrial() {
        return isTrial && trialStart != null && trialEnd != null &&
               LocalDate.now().isBefore(trialEnd);
    }

    /**
     * Check if trial has expired
     */
    public boolean isTrialExpired() {
        return isTrial && trialEnd != null &&
               LocalDate.now().isAfter(trialEnd);
    }

    /**
     * Check if subscription is active
     */
    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE;
    }

    /**
     * Start trial period
     */
    public void startTrial(int days) {
        this.isTrial = true;
        this.trialStart = LocalDate.now();
        this.trialEnd = LocalDate.now().plusDays(days);
        this.trialDaysRemaining = days;
        this.status = SubscriptionStatus.ACTIVE;
    }

    /**
     * End trial and convert to paid
     */
    public void endTrial() {
        this.isTrial = false;
        this.trialDaysRemaining = 0;
    }

    /**
     * Cancel subscription
     */
    public void cancel(String reason) {
        if (status == SubscriptionStatus.CANCELLED || status == SubscriptionStatus.EXPIRED) {
            throw new IllegalStateException("Cannot cancel subscription with status: " + status);
        }
        this.status = SubscriptionStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
        this.cancellationReason = reason;
    }

    /**
     * Renew subscription for next period
     */
    public void renew() {
        if (cancelAtPeriodEnd) {
            cancel("Auto-cancel at period end");
            return;
        }

        this.currentPeriodStart = this.currentPeriodEnd;
        this.currentPeriodEnd = calculateNextPeriodDate();
        this.status = SubscriptionStatus.ACTIVE;
    }

    /**
     * Calculate next period date based on billing cycle
     */
    private LocalDateTime calculateNextPeriodDate() {
        return switch (billingCycle) {
            case MONTHLY -> currentPeriodStart.plusMonths(1);
            case QUARTERLY -> currentPeriodStart.plusMonths(3);
            case ANNUAL -> currentPeriodStart.plusYears(1);
        };
    }

    /**
     * Generate subscription number
     */
    private String generateSubscriptionNumber() {
        return "SUB-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    /**
     * Calculate monthly recurring revenue (MRR)
     */
    public BigDecimal calculateMRR() {
        BigDecimal monthlyAmount = switch (billingCycle) {
            case MONTHLY -> totalAmount;
            case QUARTERLY -> totalAmount.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
            case ANNUAL -> totalAmount.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        };
        return monthlyAmount.multiply(BigDecimal.ONE.subtract(discountPercentage));
    }

    public enum SubscriptionStatus {
        TRIAL,
        ACTIVE,
        SUSPENDED,
        CANCELLED,
        EXPIRED,
        PENDING
    }

    public enum BillingCycle {
        MONTHLY,
        QUARTERLY,
        ANNUAL
    }
}
