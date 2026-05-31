package com.gogidix.ecommerce.customer.domain.event;

import java.time.Instant;

public record CustomerCreatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements CustomerDomainEvent {
    public CustomerCreatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Customer_CREATED"; }
}
