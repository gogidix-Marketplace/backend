package com.gogidix.universal.tracking.domain.model;

import com.gogidix.shared.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Tracking Metric entity representing aggregated metrics.
 * Stores pre-aggregated metrics for efficient querying and reporting.
 *
 * Multi-tenancy: Inherits tenantId from BaseEntity for tenant isolation.
 */
@Entity
@Table(name = "tracking_metrics", indexes = {
    @Index(name = "idx_metric_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_metric_name", columnList = "metric_name"),
    @Index(name = "idx_metric_date", columnList = "metric_date"),
    @Index(name = "idx_metric_type", columnList = "metric_type")
})
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingMetric extends BaseEntity {

    /**
     * Unique identifier for this metric
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Metric name (e.g., "daily_page_views", "user_signups")
     */
    @Column(name = "metric_name", nullable = false, length = 100)
    private String metricName;

    /**
     * Metric type (e.g., "COUNT", "SUM", "AVG", "MAX", "MIN")
     */
    @Column(name = "metric_type", nullable = false, length = 20)
    private String metricType;

    /**
     * Metric value
     */
    @Column(name = "metric_value", nullable = false)
    private Double metricValue;

    /**
     * Date the metric represents
     */
    @Column(name = "metric_date", nullable = false)
    private LocalDate metricDate;

    /**
     * Hour of the day (0-23, null if daily metric)
     */
    @Column(name = "metric_hour")
    private Integer metricHour;

    /**
     * Dimension values (e.g., event_type=PAGE_VIEW, source=WEB)
     * Stored as JSON object
     */
    @Column(name = "dimensions", columnDefinition = "TEXT")
    private String dimensions;

    /**
     * Tenant ID for multi-tenancy isolation
     */
    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    /**
     * Event type this metric is based on
     */
    @Column(name = "event_type", length = 100)
    private String eventType;

    /**
     * Source filter
     */
    @Column(name = "source", length = 50)
    private String source;

    /**
     * Count of items aggregated
     */
    @Column(name = "count", nullable = false)
    @Builder.Default
    private Long count = 0L;

    /**
     * Timestamp when metric was last updated
     */
    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;

    /**
     * Increment metric value
     */
    public void incrementValue(double delta) {
        this.metricValue += delta;
        this.count++;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    /**
     * Set metric value and update count
     */
    public void setValue(double value, long count) {
        this.metricValue = value;
        this.count = count;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    /**
     * Check if metric is for a specific hour
     */
    public boolean isHourlyMetric() {
        return metricHour != null;
    }

    /**
     * Check if metric is for a specific date (daily)
     */
    public boolean isDailyMetric() {
        return metricHour == null;
    }
}
