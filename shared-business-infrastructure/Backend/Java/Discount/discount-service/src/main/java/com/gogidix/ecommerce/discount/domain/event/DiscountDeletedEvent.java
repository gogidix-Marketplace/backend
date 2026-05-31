package com.gogidix.ecommerce.discount.domain.event;

import java.time.Instant;

public record DiscountDeletedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements DiscountDomainEvent {
    public DiscountDeletedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Discount_DELETED"; }
}
