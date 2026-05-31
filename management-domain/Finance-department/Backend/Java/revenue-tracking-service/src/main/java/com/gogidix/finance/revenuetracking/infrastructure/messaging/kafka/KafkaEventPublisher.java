package com.gogidix.finance.revenuetracking.infrastructure.messaging.kafka;

import com.gogidix.finance.revenue.domain.event.RevenueForecastGeneratedEvent;
import com.gogidix.finance.revenue.domain.event.RevenueRecognizedEvent;
import com.gogidix.finance.revenue.domain.event.RevenueStreamEvent;
import com.gogidix.finance.revenue.domain.port.out.EventPublisher;
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
 * Publishes revenue events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.revenue-events:revenue-tracking-service.events}")
    private String revenueEventsTopic;

    @Value("${spring.kafka.topics.domain-events:finance.domain-events}")
    private String domainEventsTopic;

    @Override
    public void publishRevenueEvent(RevenueRecognizedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getRevenueId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(revenueEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published revenue event: {} to topic: {}",
                        event.getEventId(), revenueEventsTopic);
                } else {
                    log.error("Failed to publish revenue event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing revenue event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishStreamEvent(RevenueStreamEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getStreamId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(revenueEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published stream event: {} to topic: {}",
                        event.getEventId(), revenueEventsTopic);
                } else {
                    log.error("Failed to publish stream event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing stream event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishForecastEvent(RevenueForecastGeneratedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getForecastId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(revenueEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published forecast event: {} to topic: {}",
                        event.getEventId(), revenueEventsTopic);
                } else {
                    log.error("Failed to publish forecast event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing forecast event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof RevenueRecognizedEvent revenueEvent) {
                publishRevenueEvent(revenueEvent);
            } else if (event instanceof RevenueStreamEvent streamEvent) {
                publishStreamEvent(streamEvent);
            } else if (event instanceof RevenueForecastGeneratedEvent forecastEvent) {
                publishForecastEvent(forecastEvent);
            } else {
                log.warn("Unknown event type: {}", event.getClass().getSimpleName());
            }
        }
    }

    @Override
    public boolean isReady() {
        try {
            // Kafka health check would go here
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
