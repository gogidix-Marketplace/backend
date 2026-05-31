package com.gogidix.sales.dashboard.infrastructure.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.sales.dashboard.domain.event.AggregationCompletedEvent;
import com.gogidix.sales.dashboard.domain.event.DashboardRefreshedEvent;
import com.gogidix.sales.dashboard.domain.event.MetricUpdatedEvent;
import com.gogidix.sales.dashboard.domain.event.RollupCompletedEvent;
import com.gogidix.sales.dashboard.domain.event.WidgetUpdatedEvent;
import com.gogidix.sales.dashboard.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka Event Publisher Implementation
 * Publishes domain events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topics.dashboard-events:global-sales-dashboard-service.events}")
    private String dashboardEventsTopic;

    @Value("${spring.kafka.topics.domain-events:sales.domain-events}")
    private String domainEventsTopic;

    @Value("${spring.kafka.topics.metrics-updated:sales.metrics-updated}")
    private String metricsUpdatedTopic;

    @Override
    public void publishMetricEvent(MetricUpdatedEvent event) {
        log.debug("Publishing metric event: {} for dashboard: {}",
                event.getEventType(), event.getDashboardId());

        try {
            String key = event.getTenantId() + ":" + event.getDashboardId();
            kafkaTemplate.send(metricsUpdatedTopic, key, event);
        } catch (Exception e) {
            log.error("Failed to publish metric event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAggregationEvent(AggregationCompletedEvent event) {
        log.debug("Publishing aggregation event: {} for aggregation: {}",
                event.getEventType(), event.getAggregationId());

        try {
            String key = event.getTenantId() + ":" + event.getAggregationId();
            kafkaTemplate.send(dashboardEventsTopic, key, event);
        } catch (Exception e) {
            log.error("Failed to publish aggregation event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishRollupEvent(RollupCompletedEvent event) {
        log.debug("Publishing rollup event: {} for rollup: {}",
                event.getEventType(), event.getRollupId());

        try {
            String key = event.getTenantId() + ":" + event.getRollupId();
            kafkaTemplate.send(dashboardEventsTopic, key, event);
        } catch (Exception e) {
            log.error("Failed to publish rollup event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishWidgetEvent(WidgetUpdatedEvent event) {
        log.debug("Publishing widget event: {} for widget: {}",
                event.getEventType(), event.getWidgetId());

        try {
            String key = event.getTenantId() + ":" + event.getWidgetId();
            kafkaTemplate.send(dashboardEventsTopic, key, event);
        } catch (Exception e) {
            log.error("Failed to publish widget event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishDashboardRefreshEvent(DashboardRefreshedEvent event) {
        log.debug("Publishing dashboard refresh event: {} for dashboard: {}",
                event.getEventType(), event.getDashboardId());

        try {
            String key = event.getTenantId() + ":" + event.getDashboardId();
            kafkaTemplate.send(dashboardEventsTopic, key, event);
        } catch (Exception e) {
            log.error("Failed to publish dashboard refresh event: {}", event.getEventId(), e);
        }
    }

    @Override
    public boolean isReady() {
        try {
            return kafkaTemplate != null &&
                    kafkaTemplate.getProducerFactory() != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        if (events == null || events.isEmpty()) {
            return;
        }

        log.debug("Publishing {} events", events.size());

        for (Object event : events) {
            try {
                if (event instanceof MetricUpdatedEvent) {
                    publishMetricEvent((MetricUpdatedEvent) event);
                } else if (event instanceof AggregationCompletedEvent) {
                    publishAggregationEvent((AggregationCompletedEvent) event);
                } else if (event instanceof RollupCompletedEvent) {
                    publishRollupEvent((RollupCompletedEvent) event);
                } else if (event instanceof WidgetUpdatedEvent) {
                    publishWidgetEvent((WidgetUpdatedEvent) event);
                } else if (event instanceof DashboardRefreshedEvent) {
                    publishDashboardRefreshEvent((DashboardRefreshedEvent) event);
                }
            } catch (Exception e) {
                log.error("Failed to publish event: {}", event.getClass().getSimpleName(), e);
            }
        }
    }
}
