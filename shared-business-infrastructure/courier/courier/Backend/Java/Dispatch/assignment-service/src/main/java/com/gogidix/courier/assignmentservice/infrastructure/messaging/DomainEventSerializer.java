package com.gogidix.courier.assignmentservice.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.courier.assignmentservice.domain.event.DomainEvent;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DomainEventSerializer implements Serializer<DomainEvent> {

    private static final Logger log = LoggerFactory.getLogger(DomainEventSerializer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, DomainEvent event) {
        if (event == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(event);
        } catch (Exception e) {
            log.error("Failed to serialize domain event for topic: {}", topic, e);
            throw new RuntimeException("Failed to serialize domain event", e);
        }
    }

    @Override
    public void close() {
    }
}
