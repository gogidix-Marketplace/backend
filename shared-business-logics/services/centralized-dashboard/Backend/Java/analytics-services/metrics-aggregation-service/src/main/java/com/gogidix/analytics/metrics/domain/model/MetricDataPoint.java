package com.gogidix.analytics.metrics.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "metric_data_points", indexes = {
    @Index(name = "idx_metric_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_metric_name", columnList = "metric_name"),
    @Index(name = "idx_metric_timestamp", columnList = "timestamp"),
    @Index(name = "idx_metric_source", columnList = "source_service"),
    @Index(name = "idx_metric_tenant_timestamp", columnList = "tenant_id,timestamp")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricDataPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "metric_name", nullable = false, length = 255)
    private String metricName;

    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", nullable = false, length = 50)
    private MetricType metricType;

    @Column(name = "metric_value", nullable = false, precision = 19, scale = 6)
    private BigDecimal metricValue;

    @Column(name = "unit", length = 50)
    private String unit;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "source_service", nullable = false, length = 100)
    private String sourceService;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @Column(name = "aggregation_level", length = 50)
    private String aggregationLevel;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "dimensions", columnDefinition = "JSONB")
    private String dimensions;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum MetricType {
        COUNTER,
        GAUGE,
        HISTOGRAM,
        SUMMARY,
        TIMER
    }
}
