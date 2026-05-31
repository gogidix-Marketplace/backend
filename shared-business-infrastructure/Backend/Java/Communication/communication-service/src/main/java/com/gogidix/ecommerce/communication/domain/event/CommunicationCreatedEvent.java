package com.gogidix.ecommerce.communication.domain.event;

import java.time.Instant;

public record CommunicationCreatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements CommunicationDomainEvent {
    public CommunicationCreatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Communication_CREATED"; }
}
