package com.gogidix.platform.subscription.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Subscription plan entity.
 *
 * Defines subscription tiers (Free, Basic, Pro, Enterprise) with:
 * - Pricing and billing cycles
 * - Feature sets and limits
 * - Trial configuration
 * - Discount structures
 */
@Entity
@Table(name = "subscription_plans", indexes = {
    @Index(name = "idx_subscription_plans_tenant", columnList = "tenant_id"),
    @Index(name = "idx_subscription_plans_code", columnList = "plan_code"),
    @Index(name = "idx_subscription_plans_status", columnList = "status"),
    @Index(name = "idx_subscription_plans_type", columnList = "plan_type")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Unique plan code (e.g., "FREE", "BASIC", "PRO", "ENTERPRISE")
     */
    @Column(name = "plan_code", nullable = false, unique = true, length = 100)
    private String planCode;

    /**
     * Human-readable plan name
     */
    @Column(name = "plan_name", nullable = false, length = 255)
    private String planName;

    /**
     * Plan description
     */
    @Lob
    @Column(name = "description")
    private String description;

    /**
     * Plan type: FIXED, USAGE_BASED, or TIERED
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false, length = 50)
    private PlanType planType;

    /**
     * Billing cycle: MONTHLY, QUARTERLY, or ANNUAL
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false, length = 50)
    private BillingCycle billingCycle;

    /**
     * Base price for this plan
     */
    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    /**
     * Currency code (e.g., "USD", "EUR")
     */
    @Column(name = "currency", length = 3)
    @Builder.Default
    private String currency = "USD";

    /**
     * Trial period in days (0 if no trial)
     */
    @Column(name = "trial_days")
    @Builder.Default
    private Integer trialDays = 0;

    /**
     * Maximum number of users allowed
     */
    @Column(name = "max_users")
    private Integer maxUsers;

    /**
     * Maximum storage in GB
     */
    @Column(name = "max_storage_gb")
    private Integer maxStorageGb;

    /**
     * Feature set (JSON): {"feature1": true, "feature2": "limited"}
     */
    @Convert(converter = com.gogidix.platform.subscription.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "features")
    @Builder.Default
    private Map<String, Object> features = new HashMap<>();

    /**
     * Resource limits (JSON): {"api_calls_per_day": 1000, "projects": 10}
     */
    @Convert(converter = com.gogidix.platform.subscription.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "limits")
    @Builder.Default
    private Map<String, Object> limits = new HashMap<>();

    /**
     * Discount structure (JSON): {"annual": 0.2, "quarterly": 0.1}
     */
    @Convert(converter = com.gogidix.platform.subscription.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "discounts")
    @Builder.Default
    private Map<String, Object> discounts = new HashMap<>();

    /**
     * Additional metadata
     */
    @Convert(converter = com.gogidix.platform.subscription.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "metadata")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Plan status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private PlanStatus status = PlanStatus.ACTIVE;

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
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Calculate price with discount
     */
    public BigDecimal calculatePrice(BillingCycle cycle) {
        BigDecimal price = basePrice;

        if (discounts != null && discounts.containsKey(cycle.name().toLowerCase())) {
            BigDecimal discountPercent = new BigDecimal(
                discounts.get(cycle.name().toLowerCase()).toString()
            );
            price = price.multiply(BigDecimal.ONE.subtract(discountPercent));
        }

        return price;
    }

    /**
     * Check if plan has a feature
     */
    public boolean hasFeature(String featureKey) {
        return features != null && features.containsKey(featureKey) &&
               Boolean.TRUE.equals(features.get(featureKey));
    }

    /**
     * Get resource limit
     */
    public Object getLimit(String limitKey) {
        return limits != null ? limits.get(limitKey) : null;
    }

    public enum PlanType {
        FIXED,
        USAGE_BASED,
        TIERED
    }

    public enum BillingCycle {
        MONTHLY,
        QUARTERLY,
        ANNUAL
    }

    public enum PlanStatus {
        ACTIVE,
        INACTIVE,
        ARCHIVED
    }
}
