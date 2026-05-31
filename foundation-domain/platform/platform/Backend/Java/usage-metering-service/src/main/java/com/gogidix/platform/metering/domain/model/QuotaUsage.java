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
 * Quota usage tracking entity.
 *
 * Tracks current usage against quota limits.
 */
@Entity
@Table(name = "quota_usage", indexes = {
    @Index(name = "idx_quota_usage_tenant_quota", columnList = "tenant_id, quota_id"),
    @Index(name = "idx_quota_usage_period", columnList = "period_start, period_end"),
    @Index(name = "idx_quota_usage_exceeded", columnList = "hard_limit_exceeded")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotaUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Reference to quota definition
     */
    @Column(name = "quota_id", nullable = false)
    private String quotaId;

    /**
     * Current usage
     */
    @Column(name = "current_usage", nullable = false, precision = 20, scale = 4)
    private BigDecimal currentUsage;

    /**
     * Soft limit
     */
    @Column(name = "soft_limit", precision = 20, scale = 4)
    private BigDecimal softLimit;

    /**
     * Hard limit
     */
    @Column(name = "hard_limit", nullable = false, precision = 20, scale = 4)
    private BigDecimal hardLimit;

    /**
     * Period start
     */
    @Column(name = "period_start", nullable = false)
    private LocalDateTime periodStart;

    /**
     * Period end
     */
    @Column(name = "period_end", nullable = false)
    private LocalDateTime periodEnd;

    /**
     * Soft limit exceeded
     */
    @Column(name = "soft_limit_exceeded", nullable = false)
    @Builder.Default
    private boolean softLimitExceeded = false;

    /**
     * Hard limit exceeded
     */
    @Column(name = "hard_limit_exceeded", nullable = false)
    @Builder.Default
    private boolean hardLimitExceeded = false;

    /**
     * Soft limit percentage
     */
    @Column(name = "soft_limit_percentage", precision = 5, scale = 2)
    private BigDecimal softLimitPercentage;

    /**
     * Hard limit percentage
     */
    @Column(name = "hard_limit_percentage", precision = 5, scale = 2)
    private BigDecimal hardLimitPercentage;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        calculatePercentages();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculatePercentages();
    }

    /**
     * Update usage
     */
    public void updateUsage(BigDecimal newUsage) {
        this.currentUsage = newUsage;
        this.softLimitExceeded = softLimit != null && newUsage.compareTo(softLimit) > 0;
        this.hardLimitExceeded = newUsage.compareTo(hardLimit) > 0;
    }

    /**
     * Calculate percentage usage
     */
    private void calculatePercentages() {
        if (softLimit != null && softLimit.compareTo(BigDecimal.ZERO) > 0) {
            this.softLimitPercentage = currentUsage
                    .divide(softLimit, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        if (hardLimit != null && hardLimit.compareTo(BigDecimal.ZERO) > 0) {
            this.hardLimitPercentage = currentUsage
                    .divide(hardLimit, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }

    /**
     * Check if quota is exceeded
     */
    public boolean isQuotaExceeded() {
        return softLimitExceeded || hardLimitExceeded;
    }

    /**
     * Get hard limit percentage
     */
    public double getHardLimitPercentageDouble() {
        return hardLimitPercentage != null ? hardLimitPercentage.doubleValue() : 0.0;
    }
}
