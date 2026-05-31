package com.gogidix.ecommerce.promotion.domain.event;

import java.time.Instant;

public record PromotionUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements PromotionDomainEvent {
    public PromotionUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Promotion_UPDATED"; }
}
