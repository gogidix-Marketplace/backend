package com.gogidix.ecommerce.coupon.domain.event;

import java.time.Instant;

public interface CouponDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
