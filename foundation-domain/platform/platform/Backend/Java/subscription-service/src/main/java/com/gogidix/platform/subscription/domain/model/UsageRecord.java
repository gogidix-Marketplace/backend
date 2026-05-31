package com.gogidix.platform.subscription.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Usage record entity for usage-based billing.
 *
 * Tracks resource usage for billing:
 * - API calls
 * - Storage
 * - Bandwidth
 * - Custom metrics
 */
@Entity
@Table(name = "usage_records", indexes = {
    @Index(name = "idx_usage_records_tenant", columnList = "tenant_id"),
    @Index(name = "idx_usage_records_subscription", columnList = "subscription_id"),
    @Index(name = "idx_usage_records_customer", columnList = "customer_id"),
    @Index(name = "idx_usage_records_metric", columnList = "metric_name"),
    @Index(name = "idx_usage_records_period", columnList = "period_start, period_end")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Reference to subscription
     */
    @Column(name = "subscription_id", nullable = false)
    private String subscriptionId;

    /**
     * Customer ID
     */
    @Column(name = "customer_id", nullable = false, length = 255)
    private String customerId;

    /**
     * Metric name (e.g., "api_calls", "storage_gb", "bandwidth_gb")
     */
    @Column(name = "metric_name", nullable = false, length = 255)
    private String metricName;

    /**
     * Metric type
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", nullable = false, length = 50)
    private MetricType metricType;

    /**
     * Quantity/amount
     */
    @Column(name = "quantity", nullable = false, precision = 15, scale = 4)
    private BigDecimal quantity;

    /**
     * Unit of measurement
     */
    @Column(name = "unit", length = 50)
    private String unit;

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
     * Unit price for billing
     */
    @Column(name = "unit_price", precision = 10, scale = 4)
    private BigDecimal unitPrice;

    /**
     * Total cost for this usage
     */
    @Column(name = "total_cost", precision = 10, scale = 2)
    private BigDecimal totalCost;

    /**
     * Description
     */
    @Lob
    @Column(name = "description")
    private String description;

    /**
     * Additional metadata
     */
    @Convert(converter = com.gogidix.platform.subscription.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "metadata")
    private Map<String, Object> metadata;

    /**
     * When usage was recorded
     */
    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @PrePersist
    protected void onCreate() {
        recordedAt = LocalDateTime.now();
        if (totalCost == null && unitPrice != null && quantity != null) {
            totalCost = unitPrice.multiply(quantity);
        }
    }

    /**
     * Calculate cost
     */
    public void calculateCost() {
        if (unitPrice != null && quantity != null) {
            this.totalCost = unitPrice.multiply(quantity);
        }
    }

    public enum MetricType {
        COUNTER,
        GAUGE
    }
}
