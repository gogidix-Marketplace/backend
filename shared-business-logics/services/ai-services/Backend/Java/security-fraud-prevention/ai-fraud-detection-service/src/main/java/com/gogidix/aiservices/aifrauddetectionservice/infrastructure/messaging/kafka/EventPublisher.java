package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.messaging.kafka;

import com.gogidix.aiservices.aifrauddetectionservice.domain.event.DomainEvent;
import com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext.TenantContext;
import com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.CompletableFuture;

/**
 * Event Publisher for sending domain events to Kafka.
 * Handles tenant-aware event publishing with proper error handling.
 */
@Slf4j
@Component
@Profile("!test")
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.fraud-events:fraud-events}")
    private String fraudEventsTopic;

    @Value("${spring.kafka.topic.compliance-events:compliance-events}")
    private String complianceEventsTopic;

    @Value("${spring.kafka.topic.transaction-events:transaction-events}")
    private String transactionEventsTopic;

    @Autowired
    public EventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publishes a domain event to the appropriate topic based on event type.
     *
     * @param event The domain event to publish
     */
    public void publish(DomainEvent event) {
        String topic = determineTopic(event);
        String key = event.getAggregateId();
        String tenantId = TenantContextHolder.getTenantId();

        log.debug("Publishing event {} to topic {} for tenant {}", event.getEventType(), topic, tenantId);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Successfully published event {} to topic {}", event.getEventType(), topic);
            } else {
                log.error("Failed to publish event {} to topic {}", event.getEventType(), topic, ex);
            }
        });
    }

    /**
     * Publishes a domain event with a specific key.
     *
     * @param event The domain event to publish
     * @param key   The partition key
     */
    public void publish(DomainEvent event, String key) {
        String topic = determineTopic(event);
        kafkaTemplate.send(topic, key, event);
    }

    /**
     * Publishes a domain event to a specific topic.
     *
     * @param event The domain event to publish
     * @param topic The target topic
     */
    public void publishToTopic(DomainEvent event, String topic) {
        String key = event.getAggregateId();
        kafkaTemplate.send(topic, key, event);
    }

    /**
     * Determines the appropriate topic for an event based on its type.
     *
     * @param event The domain event
     * @return The topic name
     */
    private String determineTopic(DomainEvent event) {
        String eventType = event.getEventType();

        if (eventType.contains("Compliance") || eventType.contains("Report")) {
            return complianceEventsTopic;
        } else if (eventType.contains("Transaction")) {
            return transactionEventsTopic;
        } else {
            return fraudEventsTopic;
        }
    }

    /**
     * Gets the fraud events topic name.
     */
    public String getFraudEventsTopic() {
        return fraudEventsTopic;
    }

    /**
     * Gets the compliance events topic name.
     */
    public String getComplianceEventsTopic() {
        return complianceEventsTopic;
    }

    /**
     * Gets the transaction events topic name.
     */
    public String getTransactionEventsTopic() {
        return transactionEventsTopic;
    }
}
