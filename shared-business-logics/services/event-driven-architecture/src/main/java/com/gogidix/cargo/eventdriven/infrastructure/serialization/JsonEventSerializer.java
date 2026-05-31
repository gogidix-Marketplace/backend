package com.gogidix.cargo.eventdriven.infrastructure.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonEventSerializer {
    private static final Logger log = LoggerFactory.getLogger(JsonEventSerializer.class);
    private final ObjectMapper objectMapper;

    public JsonEventSerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public String serialize(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Serialization failed for event", e);
            throw new RuntimeException("Event serialization failed", e);
        }
    }

    public <T> T deserialize(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error("Deserialization failed for type: {}", type.getSimpleName(), e);
            throw new RuntimeException("Event deserialization failed", e);
        }
    }
}
