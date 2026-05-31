package com.gogidix.courier.routingservice.application.service;

import com.gogidix.courier.routingservice.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for publishing domain events to Kafka.
 */
@Service
public class RouteEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(RouteEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RouteEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publish a domain event.
     *
     * @param event the event to publish
     */
    public void publish(DomainEvent event) {
        try {
            String topic = "routing-events";
            log.debug("Publishing event: {} to topic: {}", event.getEventType(), topic);
            kafkaTemplate.send(topic, event.getAggregateId(), event);
            log.info("Published event: {} for aggregate: {}", event.getEventType(), event.getAggregateId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", event.getEventType(), e);
        }
    }
}
