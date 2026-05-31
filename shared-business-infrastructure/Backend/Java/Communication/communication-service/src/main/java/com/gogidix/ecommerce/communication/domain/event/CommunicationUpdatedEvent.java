package com.gogidix.ecommerce.communication.domain.event;

import java.time.Instant;

public record CommunicationUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements CommunicationDomainEvent {
    public CommunicationUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Communication_UPDATED"; }
}
