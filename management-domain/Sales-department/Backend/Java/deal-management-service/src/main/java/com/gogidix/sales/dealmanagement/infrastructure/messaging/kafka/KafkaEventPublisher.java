package com.gogidix.sales.dealmanagement.infrastructure.messaging.kafka;

import com.gogidix.sales.dealmanagement.domain.event.DealCreatedEvent;
import com.gogidix.sales.dealmanagement.domain.event.DealLostEvent;
import com.gogidix.sales.dealmanagement.domain.event.DealStageChangedEvent;
import com.gogidix.sales.dealmanagement.domain.event.DealWonEvent;
import com.gogidix.sales.dealmanagement.domain.port.out.EventPublisher;
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
 * Publishes deal events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.deal-events:deal-management-service.events}")
    private String dealEventsTopic;

    @Value("${spring.kafka.topics.domain-events:sales.domain-events}")
    private String domainEventsTopic;

    @Override
    public void publish(DealCreatedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getDealId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(dealEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published deal created event: {} to topic: {}",
                            event.getEventId(), dealEventsTopic);
                } else {
                    log.error("Failed to publish deal created event: {}", event.getEventId(), ex);
                }
            });

            // Also publish to domain events topic
            kafkaTemplate.send(domainEventsTopic, key, event);

        } catch (Exception e) {
            log.error("Error publishing deal created event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(DealStageChangedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getDealId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(dealEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published deal stage changed event: {} to topic: {}",
                            event.getEventId(), dealEventsTopic);
                } else {
                    log.error("Failed to publish deal stage changed event: {}", event.getEventId(), ex);
                }
            });

            kafkaTemplate.send(domainEventsTopic, key, event);

        } catch (Exception e) {
            log.error("Error publishing deal stage changed event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(DealWonEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getDealId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(dealEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Published deal won event: {} to topic: {}",
                            event.getEventId(), dealEventsTopic);
                } else {
                    log.error("Failed to publish deal won event: {}", event.getEventId(), ex);
                }
            });

            kafkaTemplate.send(domainEventsTopic, key, event);

        } catch (Exception e) {
            log.error("Error publishing deal won event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(DealLostEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getDealId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(dealEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Published deal lost event: {} to topic: {}",
                            event.getEventId(), dealEventsTopic);
                } else {
                    log.error("Failed to publish deal lost event: {}", event.getEventId(), ex);
                }
            });

            kafkaTemplate.send(domainEventsTopic, key, event);

        } catch (Exception e) {
            log.error("Error publishing deal lost event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof DealCreatedEvent createdEvent) {
                publish(createdEvent);
            } else if (event instanceof DealStageChangedEvent stageChangedEvent) {
                publish(stageChangedEvent);
            } else if (event instanceof DealWonEvent wonEvent) {
                publish(wonEvent);
            } else if (event instanceof DealLostEvent lostEvent) {
                publish(lostEvent);
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
