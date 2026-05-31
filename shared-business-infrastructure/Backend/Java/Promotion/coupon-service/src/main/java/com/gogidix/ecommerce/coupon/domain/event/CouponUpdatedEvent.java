package com.gogidix.ecommerce.coupon.domain.event;

import java.time.Instant;

public record CouponUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements CouponDomainEvent {
    public CouponUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Coupon_UPDATED"; }
}
