package com.gogidix.analytics.metrics.infrastructure.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.analytics.metrics.domain.model.MetricAlert;
import com.gogidix.analytics.metrics.domain.model.MetricDataPoint;
import com.gogidix.analytics.metrics.domain.port.out.MetricEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Kafka implementation of MetricEventPublisher.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MetricEventPublisherImpl implements MetricEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publishMetricIngested(MetricDataPoint metric) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "METRIC_INGESTED");
            event.put("metricId", metric.getId());
            event.put("metricName", metric.getMetricName());
            event.put("metricValue", metric.getMetricValue());
            event.put("metricType", metric.getMetricType().name());
            event.put("sourceService", metric.getSourceService());
            event.put("tenantId", metric.getTenantId());
            event.put("timestamp", metric.getTimestamp().toString());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("metrics.raw", metric.getMetricName(), message);
            log.debug("Published metric ingested event: metricName={}", metric.getMetricName());
        } catch (Exception e) {
            log.error("Failed to publish metric ingested event", e);
        }
    }

    @Override
    public void publishMetricAggregated(MetricDataPoint metric, String aggregationType) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "METRIC_AGGREGATED");
            event.put("metricName", metric.getMetricName());
            event.put("aggregationType", aggregationType);
            event.put("metricValue", metric.getMetricValue());
            event.put("tenantId", metric.getTenantId());
            event.put("timestamp", LocalDateTime.now().toString());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("metrics.aggregated", metric.getMetricName(), message);
            log.debug("Published metric aggregated event: metricName={}", metric.getMetricName());
        } catch (Exception e) {
            log.error("Failed to publish metric aggregated event", e);
        }
    }

    @Override
    public void publishAlertTriggered(MetricAlert alert, String reason) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "ALERT_TRIGGERED");
            event.put("alertId", alert.getId());
            event.put("alertName", alert.getAlertName());
            event.put("metricName", alert.getMetricName());
            event.put("severity", alert.getSeverity().name());
            event.put("tenantId", alert.getTenantId());
            event.put("reason", reason);
            event.put("timestamp", LocalDateTime.now().toString());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("metrics.alerts", alert.getId(), message);
            log.warn("Published alert triggered event: alertId={}, metricName={}",
                alert.getId(), alert.getMetricName());
        } catch (Exception e) {
            log.error("Failed to publish alert triggered event", e);
        }
    }

    @Override
    public void publishAlertResolved(MetricAlert alert) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "ALERT_RESOLVED");
            event.put("alertId", alert.getId());
            event.put("alertName", alert.getAlertName());
            event.put("metricName", alert.getMetricName());
            event.put("tenantId", alert.getTenantId());
            event.put("timestamp", LocalDateTime.now().toString());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("metrics.alerts", alert.getId(), message);
            log.info("Published alert resolved event: alertId={}", alert.getId());
        } catch (Exception e) {
            log.error("Failed to publish alert resolved event", e);
        }
    }
}
