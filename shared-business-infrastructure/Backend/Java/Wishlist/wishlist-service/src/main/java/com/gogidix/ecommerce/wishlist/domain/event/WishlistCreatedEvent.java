package com.gogidix.ecommerce.wishlist.domain.event;

import java.time.Instant;

public record WishlistCreatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements WishlistDomainEvent {
    public WishlistCreatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Wishlist_CREATED"; }
}
