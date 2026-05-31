package com.gogidix.sales.forecast.infrastructure.messaging.kafka;

import com.gogidix.sales.forecast.domain.event.*;
import com.gogidix.sales.forecast.domain.port.out.EventPublisher;
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
 * Publishes forecast events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.forecast-events:forecast-management-service.events}")
    private String forecastEventsTopic;

    @Value("${spring.kafka.topics.domain-events:sales.domain-events}")
    private String domainEventsTopic;

    @Override
    public void publishForecastCreated(ForecastCreatedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getForecastId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(forecastEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published forecast created event: {} to topic: {}",
                        event.getEventId(), forecastEventsTopic);
                } else {
                    log.error("Failed to publish forecast created event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing forecast created event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishForecastUpdated(ForecastUpdatedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getForecastId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(forecastEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published forecast updated event: {} to topic: {}",
                        event.getEventId(), forecastEventsTopic);
                } else {
                    log.error("Failed to publish forecast updated event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing forecast updated event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishForecastApproved(ForecastApprovedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getForecastId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(forecastEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published forecast approved event: {} to topic: {}",
                        event.getEventId(), forecastEventsTopic);
                } else {
                    log.error("Failed to publish forecast approved event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing forecast approved event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishLineItemEvent(ForecastLineItemEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getForecastId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(forecastEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published line item event: {} to topic: {}",
                        event.getEventId(), forecastEventsTopic);
                } else {
                    log.error("Failed to publish line item event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing line item event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof ForecastCreatedEvent forecastCreatedEvent) {
                publishForecastCreated(forecastCreatedEvent);
            } else if (event instanceof ForecastUpdatedEvent forecastUpdatedEvent) {
                publishForecastUpdated(forecastUpdatedEvent);
            } else if (event instanceof ForecastApprovedEvent forecastApprovedEvent) {
                publishForecastApproved(forecastApprovedEvent);
            } else if (event instanceof ForecastLineItemEvent lineItemEvent) {
                publishLineItemEvent(lineItemEvent);
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
