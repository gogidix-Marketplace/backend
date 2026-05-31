package com.gogidix.universal.tracking.infrastructure.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Event publisher for tracking events to Kafka.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TrackingEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Publish event to Kafka topic
     */
    public void publish(String topic, Object payload) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, jsonPayload);
            log.debug("Published event to topic: {}", topic);
        } catch (Exception e) {
            log.error("Failed to publish event to topic: {}", topic, e);
            throw new RuntimeException("Failed to publish event", e);
        }
    }

    /**
     * Publish event with key
     */
    public void publish(String topic, String key, Object payload) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, key, jsonPayload);
            log.debug("Published event to topic: {} with key: {}", topic, key);
        } catch (Exception e) {
            log.error("Failed to publish event to topic: {} with key: {}", topic, key, e);
            throw new RuntimeException("Failed to publish event", e);
        }
    }
}
