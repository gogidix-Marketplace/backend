package com.gogidix.ecommerce.giftcard.domain.event;

import java.time.Instant;

public interface GiftCardDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
