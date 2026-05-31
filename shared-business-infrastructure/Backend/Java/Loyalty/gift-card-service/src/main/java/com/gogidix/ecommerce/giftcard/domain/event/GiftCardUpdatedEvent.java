package com.gogidix.ecommerce.giftcard.domain.event;

import java.time.Instant;

public record GiftCardUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements GiftCardDomainEvent {
    public GiftCardUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "GiftCard_UPDATED"; }
}
