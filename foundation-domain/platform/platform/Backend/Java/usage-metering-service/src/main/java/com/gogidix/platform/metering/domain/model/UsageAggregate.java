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
 * Usage aggregate entity.
 *
 * Stores aggregated metrics by period:
 * - Hourly aggregations
 * - Daily aggregations
 * - Monthly aggregations
 */
@Entity
@Table(name = "usage_aggregates", indexes = {
    @Index(name = "idx_usage_aggregates_tenant_period", columnList = "tenant_id, period_start, period_end"),
    @Index(name = "idx_usage_aggregates_metric_period", columnList = "metric_name, period_start"),
    @Index(name = "idx_usage_aggregates_type_period", columnList = "aggregation_type, period_start")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Metric name
     */
    @Column(name = "metric_name", nullable = false, length = 255)
    private String metricName;

    /**
     * Aggregation type: HOURLY, DAILY, MONTHLY
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "aggregation_type", nullable = false, length = 50)
    private AggregationType aggregationType;

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
     * Total quantity (SUM)
     */
    @Column(name = "total_quantity", nullable = false, precision = 20, scale = 4)
    private BigDecimal totalQuantity;

    /**
     * Average quantity
     */
    @Column(name = "avg_quantity", precision = 20, scale = 4)
    private BigDecimal avgQuantity;

    /**
     * Maximum quantity
     */
    @Column(name = "max_quantity", precision = 20, scale = 4)
    private BigDecimal maxQuantity;

    /**
     * Minimum quantity
     */
    @Column(name = "min_quantity", precision = 20, scale = 4)
    private BigDecimal minQuantity;

    /**
     * Number of data points
     */
    @Column(name = "count", nullable = false)
    private Integer count;

    /**
     * Dimensions (JSON)
     */
    @Convert(converter = com.gogidix.platform.metering.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "dimensions")
    @Builder.Default
    private Map<String, Object> dimensions = new HashMap<>();

    /**
     * Quota limit for comparison
     */
    @Column(name = "quota_limit", precision = 20, scale = 4)
    private BigDecimal quotaLimit;

    /**
     * Quota usage percentage
     */
    @Column(name = "quota_usage_percentage", precision = 5, scale = 2)
    private BigDecimal quotaUsagePercentage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (quotaLimit != null && totalQuantity != null) {
            this.quotaUsagePercentage = totalQuantity
                    .divide(quotaLimit, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }

    /**
     * Calculate quota percentage
     */
    public void calculateQuotaPercentage() {
        if (quotaLimit != null && totalQuantity != null && quotaLimit.compareTo(BigDecimal.ZERO) > 0) {
            this.quotaUsagePercentage = totalQuantity
                    .divide(quotaLimit, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }

    /**
     * Check if quota is exceeded
     */
    public boolean isQuotaExceeded() {
        return quotaLimit != null && totalQuantity != null &&
               totalQuantity.compareTo(quotaLimit) > 0;
    }

    /**
     * Get usage percentage
     */
    public double getUsagePercentage() {
        if (quotaUsagePercentage != null) {
            return quotaUsagePercentage.doubleValue();
        }
        return 0.0;
    }

    public enum AggregationType {
        HOURLY,
        DAILY,
        MONTHLY
    }
}
