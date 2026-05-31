package com.gogidix.monitoring.monitoringdataservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Domain model representing a single metric data point.
 * This is the core entity for time-series metrics storage.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricDataPoint {

    /**
     * Unique identifier for this metric data point.
     */
    private String id;

    /**
     * Tenant identifier for multi-tenancy support.
     */
    private String tenantId;

    /**
     * Service that generated this metric.
     * e.g., "ai-product-recommendation-service", "orchestration-workflow-engine"
     */
    private String serviceName;

    /**
     * Service type: AI_SERVICE or ORCHESTRATION_SERVICE
     */
    private ServiceType serviceType;

    /**
     * Metric name e.g., "cpu.usage", "memory.heap.used", "response.time"
     */
    private String metricName;

    /**
     * Metric value.
     */
    private double value;

    /**
     * Unit of measurement e.g., "percent", "bytes", "milliseconds"
     */
    private String unit;

    /**
     * Metric type for aggregation purposes.
     */
    private MetricType metricType;

    /**
     * Additional tags/labels for the metric.
     */
    private Map<String, String> tags;

    /**
     * Timestamp when the metric was collected.
     */
    private Instant timestamp;

    /**
     * Host/instance that reported this metric.
     */
    private String host;

    /**
     * Instance identifier (for multi-instance services).
     */
    private String instanceId;

    /**
     * Correlation ID for request tracing.
     */
    private String correlationId;

    /**
     * Service category for grouping.
     */
    private String category;

    /**
     * Timestamp when the record was created.
     */
    private Instant createdAt;

    /**
     * Enumeration for service types.
     */
    public enum ServiceType {
        AI_SERVICE,
        ORCHESTRATION_SERVICE
    }

    /**
     * Enumeration for metric types.
     */
    public enum MetricType {
        GAUGE,      // Current value (can go up or down)
        COUNTER,    // Cumulative value (only increases)
        HISTOGRAM,  // Distribution of values
        SUMMARY,    // Percentiles
        RATE        // Rate per time interval
    }

    /**
     * Validates if this metric data point is valid.
     */
    public boolean isValid() {
        return tenantId != null && !tenantId.isBlank()
                && serviceName != null && !serviceName.isBlank()
                && metricName != null && !metricName.isBlank()
                && timestamp != null;
    }

    /**
     * Creates a composite key for this metric.
     */
    public String getMetricKey() {
        return String.format("%s:%s:%s:%s",
                tenantId, serviceName, metricName,
                tags != null ? tags.hashCode() : "default");
    }
}
