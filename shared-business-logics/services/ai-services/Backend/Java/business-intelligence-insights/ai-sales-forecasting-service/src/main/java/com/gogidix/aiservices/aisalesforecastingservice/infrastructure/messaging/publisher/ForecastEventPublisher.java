package com.gogidix.aiservices.aisalesforecastingservice.infrastructure.messaging.publisher;

import com.gogidix.aiservices.aisalesforecastingservice.infrastructure.messaging.event.ForecastEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka publisher for segment events.
 * Publishes domain events to the configured Kafka topic.
 */
@Component
public class ForecastEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ForecastEventPublisher.class);

    private final KafkaTemplate<String, ForecastEvent> kafkaTemplate;
    private final String topicName;

    public ForecastEventPublisher(
            KafkaTemplate<String, ForecastEvent> kafkaTemplate,
            @Value("${spring.kafka.topic.forecast-events:forecast-events}") String topicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    /**
     * Publish a segment event to Kafka.
     * The publish will occur after the current transaction commits.
     *
     * @param event the event to publish
     */
    public void publish(ForecastEvent event) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            doPublish(event);
                        }
                    });
        } else {
            doPublish(event);
        }
    }

    /**
     * Perform the actual publish to Kafka.
     */
    private void doPublish(ForecastEvent event) {
        log.debug("Publishing event: {} for aggregate: {}", event.eventType(), event.aggregateId());

        String key = event.tenantId() + ":" + event.aggregateId();

        CompletableFuture<SendResult<String, ForecastEvent>> future =
                kafkaTemplate.send(topicName, key, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.debug("Event published successfully: {} to partition: {}",
                        event.eventId(), result.getRecordMetadata().partition());
            } else {
                log.error("Failed to publish event: {} - {}", event.eventId(), ex.getMessage());
            }
        });
    }

    /**
     * Get the topic name.
     */
    public String getTopicName() {
        return topicName;
    }
}
