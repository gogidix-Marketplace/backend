package com.gogidix.cargo.eventdriven.application.mapper;

import com.gogidix.cargo.eventdriven.application.dto.EventEnvelope;
import com.gogidix.cargo.eventdriven.domain.event.BaseDomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventMapper {
    private static final Logger log = LoggerFactory.getLogger(EventMapper.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static String serialize(BaseDomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event: {}", event.getEventId(), e);
            throw new RuntimeException("Event serialization failed", e);
        }
    }

    public static EventEnvelope toEnvelope(BaseDomainEvent event, String topic) {
        EventEnvelope envelope = new EventEnvelope();
        envelope.setEventId(event.getEventId());
        envelope.setEventType(event.getEventType());
        envelope.setTopic(topic);
        envelope.setKey(event.getAggregateId());
        envelope.setTenantId(event.getTenantId());
        envelope.setCorrelationId(event.getCorrelationId());
        envelope.setPayload(serialize(event));
        return envelope;
    }
}
