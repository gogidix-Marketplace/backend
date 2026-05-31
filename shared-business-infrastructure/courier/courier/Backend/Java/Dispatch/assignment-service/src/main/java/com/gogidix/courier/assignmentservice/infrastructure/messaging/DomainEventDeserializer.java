package com.gogidix.courier.assignmentservice.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.courier.assignmentservice.domain.event.DomainEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DomainEventDeserializer implements Deserializer<DomainEvent> {

    private static final Logger log = LoggerFactory.getLogger(DomainEventDeserializer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public DomainEvent deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.readValue(data, DomainEvent.class);
        } catch (Exception e) {
            log.error("Failed to deserialize domain event from topic: {}", topic, e);
            return null;
        }
    }

    @Override
    public void close() {
    }
}
