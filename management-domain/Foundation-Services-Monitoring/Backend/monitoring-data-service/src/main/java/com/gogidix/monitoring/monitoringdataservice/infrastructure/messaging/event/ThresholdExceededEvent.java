package com.gogidix.monitoring.monitoringdataservice.infrastructure.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Kafka event for threshold exceeded alerts.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThresholdExceededEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private String serviceName;
    private String metricName;
    private double threshold;
    private double actualValue;
    private String message;
    private String severity;
    private Instant timestamp;
    private Map<String, String> metadata;
}
