package com.gogidix.hr.benefitsadministration.infrastructure.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.hr.benefitsadministration.domain.event.BenefitEnrollmentCancelledEvent;
import com.gogidix.hr.benefitsadministration.domain.event.BenefitEnrollmentCreatedEvent;
import com.gogidix.hr.benefitsadministration.domain.event.BenefitEnrollmentUpdatedEvent;
import com.gogidix.hr.benefitsadministration.domain.port.out.BenefitEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka implementation of BenefitEventPublisher port
 * Publishes benefit enrollment events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaBenefitEventPublisherAdapter implements BenefitEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String BENEFIT_ENROLLMENT_TOPIC = "benefit.enrollment.events";
    private static final String BENEFIT_PLAN_TOPIC = "benefit.plan.events";

    @Override
    public void publishEnrollmentCreated(BenefitEnrollmentCreatedEvent event) {
        publish(BENEFIT_ENROLLMENT_TOPIC, event, "enrollment-created-" + event.getEnrollmentId());
    }

    @Override
    public void publishEnrollmentUpdated(BenefitEnrollmentUpdatedEvent event) {
        publish(BENEFIT_ENROLLMENT_TOPIC, event, "enrollment-updated-" + event.getEnrollmentId());
    }

    @Override
    public void publishEnrollmentCancelled(BenefitEnrollmentCancelledEvent event) {
        publish(BENEFIT_ENROLLMENT_TOPIC, event, "enrollment-cancelled-" + event.getEnrollmentId());
    }

    @Override
    public void publish(String topic, Object event, String key) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, key, json);
            log.info("Published event to topic: {} with key: {}", topic, key);
        } catch (JsonProcessingException e) {
            log.error("Error serializing event: {}", event, e);
            throw new RuntimeException("Failed to publish event", e);
        }
    }
}
