package com.gogidix.ecommerce.wishlist.domain.event;

import java.time.Instant;

public interface WishlistDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
