package com.gogidix.sales.crm.infrastructure.messaging.kafka;

import com.gogidix.sales.crm.domain.port.out.EventPublisher;
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

    @Value("${spring.kafka.topics.crm-events:crm-service.events}")
    private String crmEventsTopic;

    @Value("${spring.kafka.topics.customer-events:crm.customer-events}")
    private String customerEventsTopic;

    @Value("${spring.kafka.topics.interaction-events:crm.interaction-events}")
    private String interactionEventsTopic;

    @Value("${spring.kafka.topics.domain-events:sales.domain-events}")
    private String domainEventsTopic;

    private volatile boolean ready = true;

    @Override
    public void publish(Object event) {
        if (!ready) {
            log.warn("Kafka publisher is not ready. Event not published: {}", event);
            return;
        }

        String topic = determineTopic(event);
        String key = determineKey(event);

        try {
            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(topic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Successfully published event to topic: {}, key: {}, event: {}",
                        topic, key, event.getClass().getSimpleName());
                } else {
                    log.error("Failed to publish event to topic: {}, key: {}, event: {}",
                        topic, key, event.getClass().getSimpleName(), ex);
                    ready = false;
                }
            });
        } catch (Exception e) {
            log.error("Error publishing event to topic: {}, key: {}, event: {}",
                topic, key, event.getClass().getSimpleName(), e);
            ready = false;
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            publish(event);
        }
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    private String determineTopic(Object event) {
        String className = event.getClass().getSimpleName();

        if (className.contains("Customer")) {
            return customerEventsTopic;
        } else if (className.contains("Interaction") || className.contains("Contact")) {
            return interactionEventsTopic;
        }

        return crmEventsTopic;
    }

    private String determineKey(Object event) {
        try {
            // Use reflection to get ID fields
            if (event.getClass().getSimpleName().contains("Customer")) {
                Object customerId = event.getClass().getMethod("getCustomerId").invoke(event);
                return customerId != null ? customerId.toString() : "unknown";
            } else if (event.getClass().getSimpleName().contains("Contact")) {
                Object contactId = event.getClass().getMethod("getContactId").invoke(event);
                return contactId != null ? contactId.toString() : "unknown";
            } else if (event.getClass().getSimpleName().contains("Interaction")) {
                Object interactionId = event.getClass().getMethod("getInteractionId").invoke(event);
                return interactionId != null ? interactionId.toString() : "unknown";
            }
        } catch (Exception e) {
            log.debug("Could not determine key for event: {}", event.getClass().getSimpleName());
        }

        return "unknown";
    }

    /**
     * Health check method to verify Kafka connectivity
     */
    public boolean healthCheck() {
        try {
            kafkaTemplate.partitionsFor(crmEventsTopic);
            ready = true;
            return true;
        } catch (Exception e) {
            log.warn("Kafka health check failed: {}", e.getMessage());
            ready = false;
            return false;
        }
    }
}
