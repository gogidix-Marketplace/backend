package com.gogidix.platform.metering.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Quota definition entity.
 *
 * Defines resource quota rules:
 * - Soft limits (alerts)
 * - Hard limits (blocking)
 * - Notification thresholds
 * - Actions when limits are exceeded
 */
@Entity
@Table(name = "quota_definitions", indexes = {
    @Index(name = "idx_quota_definitions_tenant", columnList = "tenant_id"),
    @Index(name = "idx_quota_definitions_metric", columnList = "metric_name")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotaDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Quota name
     */
    @Column(name = "quota_name", nullable = false, length = 255)
    private String quotaName;

    /**
     * Display name
     */
    @Column(name = "quota_display_name", nullable = false, length = 255)
    private String quotaDisplayName;

    /**
     * Metric to track (references metric_definitions)
     */
    @Column(name = "metric_name", nullable = false, length = 255)
    private String metricName;

    /**
     * Soft limit (generates alert)
     */
    @Column(name = "soft_limit", precision = 20, scale = 4)
    private BigDecimal softLimit;

    /**
     * Hard limit (blocks usage)
     */
    @Column(name = "hard_limit", nullable = false, precision = 20, scale = 4)
    private BigDecimal hardLimit;

    /**
     * Quota period: HOURLY, DAILY, MONTHLY, BILLING_CYCLE
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "quota_period", nullable = false, length = 50)
    private QuotaPeriod quotaPeriod;

    /**
     * Action when soft limit is exceeded
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "soft_limit_action", length = 50)
    private LimitAction softLimitAction;

    /**
     * Action when hard limit is exceeded
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "hard_limit_action", nullable = false, length = 50)
    private LimitAction hardLimitAction;

    /**
     * Notification thresholds (JSON array): [80, 90, 100]
     */
    @Column(name = "notification_thresholds", columnDefinition = "JSONB")
    @Builder.Default
    private Integer[] notificationThresholds = {80, 90, 100};

    /**
     * Notification channels: email, webhook, sms
     */
    @Column(name = "notification_channels")
    @Builder.Default
    private String[] notificationChannels = {"email"};

    /**
     * Is active
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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
     * Check if value exceeds soft limit
     */
    public boolean exceedsSoftLimit(BigDecimal value) {
        return softLimit != null && value.compareTo(softLimit) > 0;
    }

    /**
     * Check if value exceeds hard limit
     */
    public boolean exceedsHardLimit(BigDecimal value) {
        return hardLimit != null && value.compareTo(hardLimit) > 0;
    }

    /**
     * Get usage percentage
     */
    public double getUsagePercentage(BigDecimal currentValue) {
        if (hardLimit == null || hardLimit.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return currentValue.divide(hardLimit, 4, RoundingMode.HALF_UP)
                     .multiply(BigDecimal.valueOf(100))
                     .doubleValue();
    }

    /**
     * Activate quota
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Deactivate quota
     */
    public void deactivate() {
        this.isActive = false;
    }

    public enum QuotaPeriod {
        HOURLY,
        DAILY,
        MONTHLY,
        BILLING_CYCLE
    }

    public enum LimitAction {
        ALERT,
        THROTTLE,
        BLOCK,
        CHARGE_OVERAGE
    }
}
