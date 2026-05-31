package com.gogidix.courier.availabilityservice.application.service;

import com.gogidix.courier.availabilityservice.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for publishing availability-related domain events.
 */
@Service
public class AvailabilityEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AvailabilityEventPublisher.class);

    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;

    public AvailabilityEventPublisher(KafkaTemplate<String, DomainEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publish a domain event to Kafka.
     *
     * @param event the domain event
     */
    public void publish(DomainEvent event) {
        try {
            String topic = "availability-events";
            kafkaTemplate.send(topic, event.getAggregateId(), event);
            log.debug("Published event: {} for aggregate: {}", event.getEventType(), event.getAggregateId());
        } catch (Exception e) {
            log.error("Failed to publish event: {} for aggregate: {}",
                    event.getEventType(), event.getAggregateId(), e);
        }
    }
}
