package com.gogidix.monitoring.monitoringdataservice.infrastructure.messaging.publisher;

import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.MetricEventPublisherPort;
import com.gogidix.monitoring.monitoringdataservice.infrastructure.messaging.event.MetricEvent;
import com.gogidix.monitoring.monitoringdataservice.infrastructure.messaging.event.ThresholdExceededEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka publisher for metric events.
 */
@Component
public class MetricEventPublisher implements MetricEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(MetricEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.metrics:metrics}")
    private String metricsTopic;

    @Value("${kafka.topic.alerts:alerts}")
    private String alertsTopic;

    public MetricEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishMetric(MetricDataPoint metric) {
        log.trace("Publishing metric event: {} for service: {}", metric.getMetricName(), metric.getServiceName());

        MetricEvent event = MetricEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("metric.collected")
                .timestamp(Instant.now())
                .metricData(toMetricData(metric))
                .build();

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
                metricsTopic,
                metric.getTenantId(),
                event
        );

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish metric event: {}", metric.getMetricName(), ex);
            } else {
                log.trace("Published metric event: {} to partition: {}",
                        metric.getMetricName(), result.getRecordMetadata().partition());
            }
        });
    }

    @Override
    public void publishMetrics(List<MetricDataPoint> metrics) {
        log.debug("Publishing {} metric events", metrics.size());

        metrics.forEach(this::publishMetric);
    }

    @Override
    public void publishThresholdExceeded(
            String tenantId,
            String serviceName,
            String metricName,
            double threshold,
            double actualValue,
            String message
    ) {
        log.warn("Threshold exceeded: {} for service: {}, value: {}, threshold: {}",
                metricName, serviceName, actualValue, threshold);

        ThresholdExceededEvent event = ThresholdExceededEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("threshold.exceeded")
                .tenantId(tenantId)
                .serviceName(serviceName)
                .metricName(metricName)
                .threshold(threshold)
                .actualValue(actualValue)
                .message(message)
                .severity(calculateSeverity(threshold, actualValue))
                .timestamp(Instant.now())
                .build();

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
                alertsTopic,
                tenantId,
                event
        );

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish threshold exceeded event: {}", metricName, ex);
            } else {
                log.info("Published threshold exceeded event: {} for service: {}", metricName, serviceName);
            }
        });
    }

    private MetricEvent.MetricData toMetricData(MetricDataPoint metric) {
        return MetricEvent.MetricData.builder()
                .tenantId(metric.getTenantId())
                .serviceName(metric.getServiceName())
                .serviceType(metric.getServiceType() != null ? metric.getServiceType().name() : null)
                .metricName(metric.getMetricName())
                .value(metric.getValue())
                .unit(metric.getUnit())
                .metricType(metric.getMetricType() != null ? metric.getMetricType().name() : null)
                .tags(metric.getTags())
                .host(metric.getHost())
                .instanceId(metric.getInstanceId())
                .correlationId(metric.getCorrelationId())
                .build();
    }

    private String calculateSeverity(double threshold, double actualValue) {
        double percentageExceeded = ((actualValue - threshold) / threshold) * 100;

        if (percentageExceeded > 100) {
            return "CRITICAL";
        } else if (percentageExceeded > 50) {
            return "HIGH";
        } else if (percentageExceeded > 20) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
}
