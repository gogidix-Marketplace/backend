package com.gogidix.dashboard.gateway.chart.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing time-series data point.
 */
@Entity
@Table(name = "time_series_data", indexes = {
    @Index(name = "idx_metric_name", columnList = "metric_name"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_timestamp", columnList = "timestamp")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TimeSeriesData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metric_name", nullable = false)
    private String metricName;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "labels")
    private String labels;

    @Column(name = "source")
    private String source;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
