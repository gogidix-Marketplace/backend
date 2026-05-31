package com.gogidix.ecommerce.wishlist.domain.event;

import java.time.Instant;

public record WishlistUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements WishlistDomainEvent {
    public WishlistUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Wishlist_UPDATED"; }
}
