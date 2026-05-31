package com.gogidix.ecommerce.loyalty.domain.event;

import java.time.Instant;

public interface LoyaltyDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
