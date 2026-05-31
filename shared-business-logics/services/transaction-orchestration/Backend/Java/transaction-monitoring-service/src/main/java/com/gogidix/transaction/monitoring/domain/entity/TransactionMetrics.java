package com.gogidix.transaction.monitoring.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction Metrics entity for tracking performance metrics.
 * Converted from MongoDB to PostgreSQL JPA.
 */
@Entity
@Table(name = "transaction_metrics", indexes = {
    @Index(name = "idx_metrics_transaction_id", columnList = "transaction_id"),
    @Index(name = "idx_metrics_type", columnList = "metric_type"),
    @Index(name = "idx_metrics_timestamp", columnList = "timestamp"),
    @Index(name = "idx_metrics_severity", columnList = "severity")
})
@Data
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(name = "transaction_id")
    private UUID transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", length = 50)
    private MetricType metricType;

    @Column(name = "metric_name", length = 255)
    private String metricName;

    @Column(name = "metric_value", precision = 19, scale = 4)
    private BigDecimal metricValue;

    @Column(name = "metric_unit", length = 100)
    private String metricUnit;

    @Column(name = "threshold_warning", precision = 19, scale = 4)
    private BigDecimal thresholdWarning;

    @Column(name = "threshold_critical", precision = 19, scale = 4)
    private BigDecimal thresholdCritical;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 50)
    private MetricSeverity severity;

    @Column(name = "tags", length = 500)
    private String tags;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @CreatedDate
    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Version
    @Column(name = "version")
    private Long version;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
        if (severity == null) {
            severity = MetricSeverity.NORMAL;
        }
    }

    public enum MetricType {
        RESPONSE_TIME,
        THROUGHPUT,
        ERROR_RATE,
        SUCCESS_RATE,
        AVAILABILITY,
        RESOURCE_USAGE,
        QUEUE_SIZE,
        DATABASE_QUERY_TIME,
        API_CALL_DURATION,
        STEP_EXECUTION_TIME,
        BUSINESS_METRIC,
        CUSTOM_METRIC
    }

    public enum MetricSeverity {
        NORMAL,
        WARNING,
        CRITICAL
    }

    /**
     * Check if metric exceeds warning threshold
     */
    public boolean exceedsWarningThreshold() {
        return thresholdWarning != null &&
               metricValue != null &&
               metricValue.compareTo(thresholdWarning) >= 0;
    }

    /**
     * Check if metric exceeds critical threshold
     */
    public boolean exceedsCriticalThreshold() {
        return thresholdCritical != null &&
               metricValue != null &&
               metricValue.compareTo(thresholdCritical) >= 0;
    }
}
