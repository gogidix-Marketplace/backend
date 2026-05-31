package com.gogidix.ecommerce.loyalty.domain.event;

import java.time.Instant;

public record LoyaltyCreatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements LoyaltyDomainEvent {
    public LoyaltyCreatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Loyalty_CREATED"; }
}
