package com.gogidix.ecommerce.giftcard.domain.event;

import java.time.Instant;

public record GiftCardCreatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements GiftCardDomainEvent {
    public GiftCardCreatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "GiftCard_CREATED"; }
}
