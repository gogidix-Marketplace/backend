package com.gogidix.ecommerce.coupon.domain.port.out;

import com.gogidix.ecommerce.coupon.domain.event.CouponDomainEvent;

public interface CouponEventPublisher {
    void publish(CouponDomainEvent event);
}
