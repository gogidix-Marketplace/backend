package com.gogidix.monitoring.performance.infrastructure.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Event class for metric publication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricEvent {

    private String tenantId;
    private String serviceId;
    private String metricName;
    private String metricType;
    private Double value;
    private Map<String, String> labels;
    private Instant timestamp;
}
