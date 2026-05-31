package com.gogidix.ecommerce.discount.domain.event;

import java.time.Instant;

public interface DiscountDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
