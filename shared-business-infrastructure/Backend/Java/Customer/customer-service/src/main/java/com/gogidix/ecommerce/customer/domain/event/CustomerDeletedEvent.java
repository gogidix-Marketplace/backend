package com.gogidix.ecommerce.customer.domain.event;

import java.time.Instant;

public record CustomerDeletedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements CustomerDomainEvent {
    public CustomerDeletedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Customer_DELETED"; }
}
