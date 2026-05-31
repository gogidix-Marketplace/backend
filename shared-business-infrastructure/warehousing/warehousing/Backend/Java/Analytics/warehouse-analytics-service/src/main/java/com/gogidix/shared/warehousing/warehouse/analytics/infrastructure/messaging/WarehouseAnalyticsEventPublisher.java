package com.gogidix.shared.warehousing.warehouse.analytics.infrastructure.messaging;

import com.gogidix.shared.warehousing.warehouse.analytics.domain.events.MetricsGeneratedEvent;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.events.ReportGeneratedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka event publisher for warehouse analytics domain events
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseAnalyticsEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.metrics-generated:warehouse-metrics-generated}")
    private String metricsGeneratedTopic;

    @Value("${kafka.topic.report-generated:warehouse-report-generated}")
    private String reportGeneratedTopic;

    /**
     * Publish metrics generated event
     */
    public void publishMetricsGenerated(MetricsGeneratedEvent event) {
        log.info("Publishing metrics generated event: {}", event.getEventId());
        kafkaTemplate.send(metricsGeneratedTopic, event.getMetricsId(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish metrics generated event: {}", ex.getMessage());
                } else {
                    log.debug("Metrics generated event published successfully: {}", event.getEventId());
                }
            });
    }

    /**
     * Publish report generated event
     */
    public void publishReportGenerated(ReportGeneratedEvent event) {
        log.info("Publishing report generated event: {}", event.getEventId());
        kafkaTemplate.send(reportGeneratedTopic, event.getReportId(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish report generated event: {}", ex.getMessage());
                } else {
                    log.debug("Report generated event published successfully: {}", event.getEventId());
                }
            });
    }
}
