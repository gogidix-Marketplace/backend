package com.gogidix.sales.notification.infrastructure.messaging.kafka;

import com.gogidix.sales.notification.domain.event.NotificationDeliveredEvent;
import com.gogidix.sales.notification.domain.event.NotificationFailedEvent;
import com.gogidix.sales.notification.domain.event.NotificationReadEvent;
import com.gogidix.sales.notification.domain.event.NotificationSentEvent;
import com.gogidix.sales.notification.domain.port.out.EventPublisher;
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

    @Value("${spring.kafka.topics.notification-events:notification-service.events}")
    private String notificationEventsTopic;

    @Value("${spring.kafka.topics.domain-events:sales.domain-events}")
    private String domainEventsTopic;

    private volatile boolean ready = true;

    @Override
    public void publish(NotificationSentEvent event) {
        try {
            String key = event.getNotificationId();
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(notificationEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish NotificationSentEvent: {}", event.getNotificationId(), ex);
                    ready = false;
                } else {
                    log.debug("Published NotificationSentEvent: {} to partition: {}",
                            event.getNotificationId(), result.getRecordMetadata().partition());
                    ready = true;
                }
            });
        } catch (Exception e) {
            log.error("Error publishing NotificationSentEvent", e);
            ready = false;
        }
    }

    @Override
    public void publish(NotificationReadEvent event) {
        try {
            String key = event.getNotificationId();
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(notificationEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish NotificationReadEvent: {}", event.getNotificationId(), ex);
                } else {
                    log.debug("Published NotificationReadEvent: {}", event.getNotificationId());
                }
            });
        } catch (Exception e) {
            log.error("Error publishing NotificationReadEvent", e);
        }
    }

    @Override
    public void publish(NotificationDeliveredEvent event) {
        try {
            String key = event.getNotificationId();
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(notificationEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish NotificationDeliveredEvent: {}", event.getNotificationId(), ex);
                } else {
                    log.debug("Published NotificationDeliveredEvent: {}", event.getNotificationId());
                }
            });
        } catch (Exception e) {
            log.error("Error publishing NotificationDeliveredEvent", e);
        }
    }

    @Override
    public void publish(NotificationFailedEvent event) {
        try {
            String key = event.getNotificationId();
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(notificationEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish NotificationFailedEvent: {}", event.getNotificationId(), ex);
                } else {
                    log.debug("Published NotificationFailedEvent: {}", event.getNotificationId());
                }
            });
        } catch (Exception e) {
            log.error("Error publishing NotificationFailedEvent", e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof NotificationSentEvent) {
                publish((NotificationSentEvent) event);
            } else if (event instanceof NotificationReadEvent) {
                publish((NotificationReadEvent) event);
            } else if (event instanceof NotificationDeliveredEvent) {
                publish((NotificationDeliveredEvent) event);
            } else if (event instanceof NotificationFailedEvent) {
                publish((NotificationFailedEvent) event);
            } else {
                log.warn("Unknown event type: {}", event.getClass().getSimpleName());
            }
        }
    }

    @Override
    public boolean isReady() {
        return ready;
    }
}
