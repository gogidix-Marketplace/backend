package com.gogidix.courier.discountservice.application.service;

import com.gogidix.courier.discountservice.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DiscountEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DiscountEventPublisher.class);

    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;

    public DiscountEventPublisher(KafkaTemplate<String, DomainEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(DomainEvent event) {
        try {
            kafkaTemplate.send("discount-events", event.getEventId(), event);
            log.debug("Published event: {}", event.getEventType());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", event.getEventType(), e);
        }
    }
}
