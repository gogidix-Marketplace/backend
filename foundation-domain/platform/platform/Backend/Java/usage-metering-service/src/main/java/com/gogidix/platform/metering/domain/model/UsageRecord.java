package com.gogidix.platform.metering.domain.model;

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
 * Usage record entity (raw events).
 *
 * Stores raw usage events before aggregation.
 * This table can grow very large - consider partitioning by month in production.
 */
@Entity
@Table(name = "usage_records", indexes = {
    @Index(name = "idx_usage_records_tenant_time", columnList = "tenant_id, event_time"),
    @Index(name = "idx_usage_records_metric_time", columnList = "metric_name, event_time"),
    @Index(name = "idx_usage_records_service_time", columnList = "service_name, event_time")
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
     * Metric name (references metric_definitions)
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
     * When the usage event occurred
     */
    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    /**
     * When the record was received
     */
    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    /**
     * Dimensions (JSON): {"service": "api-gateway", "operation": "create_order"}
     */
    @Convert(converter = com.gogidix.platform.metering.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "dimensions")
    @Builder.Default
    private Map<String, Object> dimensions = new HashMap<>();

    /**
     * Service that generated the usage
     */
    @Column(name = "service_name", length = 255)
    private String serviceName;

    /**
     * Resource being tracked
     */
    @Column(name = "resource_id", length = 255)
    private String resourceId;

    /**
     * User who generated the usage
     */
    @Column(name = "user_id", length = 255)
    private String userId;

    /**
     * Correlation ID for tracing
     */
    @Column(name = "correlation_id", length = 255)
    private String correlationId;

    /**
     * Additional metadata
     */
    @Convert(converter = com.gogidix.platform.metering.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "metadata")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    @PrePersist
    protected void onCreate() {
        if (receivedAt == null) {
            receivedAt = LocalDateTime.now();
        }
    }

    /**
     * Check if record is aggregatable (not too old)
     */
    public boolean isAggregatable() {
        // Records older than retention period should not be aggregated
        LocalDateTime cutoff = LocalDateTime.now().minusDays(90);
        return eventTime.isAfter(cutoff);
    }

    public enum MetricType {
        COUNTER,
        GAUGE,
        HISTOGRAM
    }
}
