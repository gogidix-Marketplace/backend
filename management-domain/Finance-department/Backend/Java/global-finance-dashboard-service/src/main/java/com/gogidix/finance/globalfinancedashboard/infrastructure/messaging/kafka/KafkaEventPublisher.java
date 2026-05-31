package com.gogidix.finance.globalfinancedashboard.infrastructure.messaging.kafka;

import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardCreatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardSharedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardUpdatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardViewedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.MetricCalculatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.WidgetCreatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.WidgetUpdatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka Event Publisher
 * Publishes dashboard events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.dashboard-events:global-finance-dashboard-service.events}")
    private String dashboardEventsTopic;

    @Value("${spring.kafka.topics.domain-events:finance.domain-events}")
    private String domainEventsTopic;

    @Value("${spring.kafka.topics.dashboard-viewed:dashboard.viewed}")
    private String dashboardViewedTopic;

    @Value("${spring.kafka.topics.metric-calculated:metric.calculated}")
    private String metricCalculatedTopic;

    @Override
    public void publish(DashboardCreatedEvent event) {
        publishEvent(event, dashboardEventsTopic);
    }

    @Override
    public void publish(DashboardUpdatedEvent event) {
        publishEvent(event, dashboardEventsTopic);
    }

    @Override
    public void publish(DashboardSharedEvent event) {
        publishEvent(event, dashboardEventsTopic);
    }

    @Override
    public void publish(WidgetCreatedEvent event) {
        publishEvent(event, dashboardEventsTopic);
    }

    @Override
    public void publish(WidgetUpdatedEvent event) {
        publishEvent(event, dashboardEventsTopic);
    }

    @Override
    public void publish(MetricCalculatedEvent event) {
        publishEvent(event, metricCalculatedTopic);
    }

    @Override
    public void publish(String topic, Object event) {
        publishEvent(event, topic);
    }

    /**
     * Publishes an event to the specified topic
     */
    private void publishEvent(Object event, String topic) {
        try {
            String key = generateKey(event);

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(topic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published event: {} to topic: {}",
                            getEventId(event), topic);
                } else {
                    log.error("Failed to publish event: {} to topic: {}",
                            getEventId(event), topic, ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing event: {}", getEventId(event), e);
        }
    }

    /**
     * Generates a partition key for the event
     */
    private String generateKey(Object event) {
        if (event instanceof DashboardCreatedEvent d) {
            return d.getTenantId() + "-" + d.getDashboardId();
        } else if (event instanceof DashboardUpdatedEvent d) {
            return d.getTenantId() + "-" + d.getDashboardId();
        } else if (event instanceof DashboardSharedEvent d) {
            return d.getTenantId() + "-" + d.getDashboardId();
        } else if (event instanceof WidgetCreatedEvent w) {
            return w.getTenantId() + "-" + w.getWidgetId();
        } else if (event instanceof WidgetUpdatedEvent w) {
            return w.getTenantId() + "-" + w.getWidgetId();
        } else if (event instanceof MetricCalculatedEvent m) {
            return m.getTenantId() + "-" + m.getMetricId();
        } else if (event instanceof DashboardViewedEvent d) {
            return d.getTenantId() + "-" + d.getDashboardId();
        }
        return "unknown";
    }

    /**
     * Extracts event ID for logging
     */
    private String getEventId(Object event) {
        try {
            if (event instanceof DashboardCreatedEvent d) {
                return d.getDashboardId();
            } else if (event instanceof DashboardUpdatedEvent d) {
                return d.getDashboardId();
            } else if (event instanceof DashboardSharedEvent d) {
                return d.getDashboardId();
            } else if (event instanceof WidgetCreatedEvent w) {
                return w.getWidgetId();
            } else if (event instanceof WidgetUpdatedEvent w) {
                return w.getWidgetId();
            } else if (event instanceof MetricCalculatedEvent m) {
                return m.getMetricId();
            } else if (event instanceof DashboardViewedEvent d) {
                return d.getDashboardId();
            }
        } catch (Exception e) {
            log.warn("Could not extract event ID", e);
        }
        return event.getClass().getSimpleName();
    }
}
