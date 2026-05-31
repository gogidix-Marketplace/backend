package com.gogidix.monitoring.performance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

/**
 * Domain entity representing a single metric data point.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "metric_data")
public class MetricData {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String serviceId;

    @Indexed
    private String metricName;

    private MetricType metricType;

    private Double value;

    private Map<String, String> labels;

    @Indexed
    private Instant timestamp;

    /**
     * Create a new metric data point.
     */
    public static MetricData create(String tenantId, String serviceId, String metricName,
                                  MetricType metricType, Double value, Map<String, String> labels) {
        return MetricData.builder()
                .tenantId(tenantId)
                .serviceId(serviceId)
                .metricName(metricName)
                .metricType(metricType)
                .value(value)
                .labels(labels)
                .timestamp(Instant.now())
                .build();
    }
}
