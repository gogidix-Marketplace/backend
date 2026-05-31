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
 * Metric definition entity.
 *
 * Defines catalog of metrics that can be tracked:
 * - API calls
 * - Storage
 * - Bandwidth
 * - CPU time
 * - Custom business metrics
 */
@Entity
@Table(name = "metric_definitions", indexes = {
    @Index(name = "idx_metric_definitions_name", columnList = "metric_name"),
    @Index(name = "idx_metric_definitions_category", columnList = "metric_category")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    /**
     * Unique metric name (e.g., "api_calls", "storage_gb")
     */
    @Column(name = "metric_name", nullable = false, unique = true, length = 255)
    private String metricName;

    /**
     * Display name
     */
    @Column(name = "metric_display_name", nullable = false, length = 255)
    private String metricDisplayName;

    /**
     * Metric description
     */
    @Lob
    @Column(name = "description")
    private String description;

    /**
     * Metric type: COUNTER, GAUGE, HISTOGRAM
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", nullable = false, length = 50)
    private MetricType metricType;

    /**
     * Metric category: API, STORAGE, COMPUTE, NETWORK, BUSINESS
     */
    @Column(name = "metric_category", length = 100)
    private String metricCategory;

    /**
     * Unit of measurement (calls, gb, hours, ms)
     */
    @Column(name = "unit", length = 50)
    private String unit;

    /**
     * Aggregation type: SUM, AVG, MAX, MIN, COUNT
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "aggregation_type", length = 50)
    private AggregationType aggregationType;

    /**
     * Retention period in days
     */
    @Column(name = "retention_days")
    @Builder.Default
    private Integer retentionDays = 90;

    /**
     * Whether metric is billable
     */
    @Column(name = "billable", nullable = false)
    @Builder.Default
    private boolean billable = false;

    /**
     * Unit price for billing
     */
    @Column(name = "unit_price", precision = 10, scale = 4)
    private BigDecimal unitPrice;

    /**
     * Allowed dimensions (JSON array)
     */
    @Column(name = "dimensions", columnDefinition = "JSONB")
    @Builder.Default
    private String[] dimensions = {};

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
     * Activate metric
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Deactivate metric
     */
    public void deactivate() {
        this.isActive = false;
    }

    public enum MetricType {
        COUNTER,
        GAUGE,
        HISTOGRAM
    }

    public enum AggregationType {
        SUM,
        AVG,
        MAX,
        MIN,
        COUNT
    }
}
