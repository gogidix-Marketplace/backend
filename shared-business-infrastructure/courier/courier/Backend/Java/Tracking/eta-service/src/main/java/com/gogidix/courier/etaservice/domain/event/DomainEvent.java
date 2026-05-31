package com.gogidix.courier.etaservice.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for all domain events.
 * Domain events represent something that happened in the domain that
 * other parts of the system may be interested in.
 */
public abstract class DomainEvent {

    private final String eventId;
    private final String eventType;
    private final Instant timestamp;
    private final String correlationId;

    protected DomainEvent(String eventType, String correlationId) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.timestamp = Instant.now();
        this.correlationId = correlationId != null ? correlationId : UUID.randomUUID().toString();
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    @Override
    public String toString() {
        return "DomainEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", timestamp=" + timestamp +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }
}
