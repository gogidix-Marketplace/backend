package com.gogidix.ecommerce.promotion.domain.event;

import java.time.Instant;

public interface PromotionDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
