package com.gogidix.courier.discountservice.domain.event;

import java.time.Instant;

public abstract class DomainEvent {

    private final String eventId;
    private final Instant occurredAt;
    private final String eventType;

    protected DomainEvent(String eventType) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.occurredAt = Instant.now();
        this.eventType = eventType;
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getEventType() {
        return eventType;
    }
}
