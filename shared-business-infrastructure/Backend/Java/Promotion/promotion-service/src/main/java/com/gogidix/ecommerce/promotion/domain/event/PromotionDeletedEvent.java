package com.gogidix.ecommerce.promotion.domain.event;

import java.time.Instant;

public record PromotionDeletedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements PromotionDomainEvent {
    public PromotionDeletedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Promotion_DELETED"; }
}
