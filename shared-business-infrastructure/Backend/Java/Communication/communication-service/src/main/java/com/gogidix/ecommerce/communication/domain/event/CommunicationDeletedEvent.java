package com.gogidix.ecommerce.communication.domain.event;

import java.time.Instant;

public record CommunicationDeletedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements CommunicationDomainEvent {
    public CommunicationDeletedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Communication_DELETED"; }
}
