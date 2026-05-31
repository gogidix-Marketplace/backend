package com.gogidix.ecommerce.discount.domain.event;

import java.time.Instant;

public record DiscountCreatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements DiscountDomainEvent {
    public DiscountCreatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Discount_CREATED"; }
}
