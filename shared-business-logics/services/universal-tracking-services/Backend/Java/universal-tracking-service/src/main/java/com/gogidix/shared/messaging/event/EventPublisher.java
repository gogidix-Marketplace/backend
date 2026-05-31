package com.gogidix.shared.messaging.event;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Event publisher interface for publishing events to message brokers.
 */
@Component
public interface EventPublisher {

    /**
     * Publish an event to a topic
     */
    void publish(String topic, Map<String, Object> payload);

    /**
     * Publish an event with a key
     */
    void publish(String topic, String key, Map<String, Object> payload);
}
