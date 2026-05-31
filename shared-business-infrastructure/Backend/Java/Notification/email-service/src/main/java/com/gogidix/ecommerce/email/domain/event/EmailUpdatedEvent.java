package com.gogidix.ecommerce.email.domain.event;

import java.time.Instant;

public record EmailUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements EmailDomainEvent {
    public EmailUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Email_UPDATED"; }
}
