package com.gogidix.monitoring.performance.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.monitoring.performance.domain.model.MetricData;
import com.gogidix.monitoring.performance.domain.port.out.MetricPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka implementation of MetricPublisher.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMetricPublisher implements MetricPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String METRIC_TOPIC = "performance.metrics";
    private static final String ALERT_TOPIC = "performance.alerts";

    @Override
    public void publishMetricEvent(MetricData metric) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("tenantId", metric.getTenantId());
            event.put("serviceId", metric.getServiceId());
            event.put("metricName", metric.getMetricName());
            event.put("metricType", metric.getMetricType());
            event.put("value", metric.getValue());
            event.put("labels", metric.getLabels());
            event.put("timestamp", metric.getTimestamp());

            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(METRIC_TOPIC, metric.getTenantId(), json);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize metric event", e);
        }
    }

    @Override
    public void publishAlertEvent(String alertId, String tenantId, String serviceId, String message) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("alertId", alertId);
            event.put("tenantId", tenantId);
            event.put("serviceId", serviceId);
            event.put("message", message);

            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(ALERT_TOPIC, tenantId, json);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize alert event", e);
        }
    }
}
