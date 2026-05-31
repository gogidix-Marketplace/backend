package com.gogidix.courier.assignmentservice.infrastructure.config;

import com.gogidix.courier.assignmentservice.application.service.AssignmentEventPublisher;
import com.gogidix.courier.assignmentservice.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Kafka implementation of AssignmentEventPublisher.
 */
@Component
public class KafkaEventPublisherImpl implements AssignmentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisherImpl.class);
    private static final String ASSIGNMENT_EVENTS_TOPIC = "assignment-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisherImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            log.info("Publishing event: {} for aggregate: {}", event.getEventType(), event.getAggregateId());
            kafkaTemplate.send(ASSIGNMENT_EVENTS_TOPIC, event.getAggregateId(), event);
            log.debug("Event published successfully: {}", event.getEventType());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", event.getEventType(), e);
            throw new RuntimeException("Failed to publish event: " + event.getEventType(), e);
        }
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        if (events == null || events.isEmpty()) {
            return;
        }
        log.info("Publishing {} events", events.size());
        events.forEach(this::publish);
    }
}
