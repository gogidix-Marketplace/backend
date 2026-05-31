package com.gogidix.analytics.metrics.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "metric_aggregations", indexes = {
    @Index(name = "idx_agg_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_agg_metric_name", columnList = "metric_name"),
    @Index(name = "idx_agg_window_start", columnList = "window_start"),
    @Index(name = "idx_agg_window_end", columnList = "window_end"),
    @Index(name = "idx_agg_type", columnList = "aggregation_type"),
    @Index(name = "idx_agg_tenant_window", columnList = "tenant_id,window_start,window_end")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricAggregation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "metric_name", nullable = false, length = 255)
    private String metricName;

    @Enumerated(EnumType.STRING)
    @Column(name = "aggregation_type", nullable = false, length = 50)
    private AggregationType aggregationType;

    @Column(name = "window_size_seconds", nullable = false)
    private Integer windowSizeSeconds;

    @Column(name = "window_start", nullable = false)
    private LocalDateTime windowStart;

    @Column(name = "window_end", nullable = false)
    private LocalDateTime windowEnd;

    @Column(name = "count", nullable = false)
    private Long count;

    @Column(name = "sum", precision = 19, scale = 6)
    private BigDecimal sum;

    @Column(name = "avg", precision = 19, scale = 6)
    private BigDecimal avg;

    @Column(name = "min", precision = 19, scale = 6)
    private BigDecimal min;

    @Column(name = "max", precision = 19, scale = 6)
    private BigDecimal max;

    @Column(name = "p50", precision = 19, scale = 6)
    private BigDecimal p50;

    @Column(name = "p95", precision = 19, scale = 6)
    private BigDecimal p95;

    @Column(name = "p99", precision = 19, scale = 6)
    private BigDecimal p99;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AggregationType {
        SUM,
        AVG,
        MIN,
        MAX,
        COUNT,
        RATE,
        PERCENTILE
    }
}
