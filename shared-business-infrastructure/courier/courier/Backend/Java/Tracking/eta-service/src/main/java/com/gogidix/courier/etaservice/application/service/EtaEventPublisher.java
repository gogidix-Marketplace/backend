package com.gogidix.courier.etaservice.application.service;

import com.gogidix.courier.etaservice.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.CompletableFuture;

/**
 * Publisher for ETA domain events.
 * Events are published after transaction commit to ensure consistency.
 */
@Component
public class EtaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EtaEventPublisher.class);

    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;
    private final String etaEventsTopic;

    public EtaEventPublisher(KafkaTemplate<String, DomainEvent> kafkaTemplate,
                             org.springframework.core.env.Environment environment) {
        this.kafkaTemplate = kafkaTemplate;
        this.etaEventsTopic = environment.getProperty("spring.kafka.topic.eta-events", "eta-events");
    }

    /**
     * Publish a domain event.
     * The event will be sent after the current transaction commits.
     *
     * @param event the event to publish
     */
    @Transactional
    public void publish(DomainEvent event) {
        if (event == null) {
            log.warn("Attempted to publish null event, skipping");
            return;
        }

        log.debug("Scheduling event for publishing: {}", event.getEventType());

        // Register transaction synchronization to publish after commit
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    doPublish(event);
                }
            });
        } else {
            // No active transaction, publish immediately
            doPublish(event);
        }
    }

    /**
     * Publish event immediately without waiting for transaction.
     * Use only when not within a transaction or when immediate publishing is required.
     *
     * @param event the event to publish
     */
    public void publishImmediately(DomainEvent event) {
        doPublish(event);
    }

    /**
     * Actually send the event to Kafka.
     */
    private void doPublish(DomainEvent event) {
        try {
            String key = event.getCorrelationId();
            CompletableFuture<SendResult<String, DomainEvent>> future =
                    kafkaTemplate.send(etaEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Event published successfully: {} (eventId: {}, correlationId: {})",
                            event.getEventType(), event.getEventId(), event.getCorrelationId());
                } else {
                    log.error("Failed to publish event: {} (eventId: {}, correlationId: {})",
                            event.getEventType(), event.getEventId(), event.getCorrelationId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing event: {}", event.getEventType(), e);
        }
    }

    /**
     * Check if the publisher is connected to Kafka.
     *
     * @return true if connected
     */
    public boolean isConnected() {
        try {
            return kafkaTemplate != null && kafkaTemplate.getProducerFactory() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
