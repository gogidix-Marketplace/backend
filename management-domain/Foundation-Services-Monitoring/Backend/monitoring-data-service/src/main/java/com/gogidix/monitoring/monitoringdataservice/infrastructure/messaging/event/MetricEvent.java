package com.gogidix.monitoring.monitoringdataservice.infrastructure.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Kafka event for metric data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricEvent {

    private String eventId;
    private String eventType;
    private Instant timestamp;
    private MetricData metricData;
    private Map<String, String> metadata;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricData {
        private String tenantId;
        private String serviceName;
        private String serviceType;
        private String metricName;
        private double value;
        private String unit;
        private String metricType;
        private Map<String, String> tags;
        private String host;
        private String instanceId;
        private String correlationId;
    }
}
