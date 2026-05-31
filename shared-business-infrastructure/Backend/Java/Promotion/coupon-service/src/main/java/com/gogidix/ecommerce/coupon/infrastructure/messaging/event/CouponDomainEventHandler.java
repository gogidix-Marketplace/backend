package com.gogidix.ecommerce.coupon.infrastructure.messaging.event;

import com.gogidix.ecommerce.coupon.domain.event.CouponCreatedEvent;
import com.gogidix.ecommerce.coupon.domain.event.CouponUpdatedEvent;
import com.gogidix.ecommerce.coupon.domain.event.CouponDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CouponDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(CouponDomainEventHandler.class);

    @EventListener
    public void handleCreated(CouponCreatedEvent event) {
        log.info("Coupon created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(CouponUpdatedEvent event) {
        log.info("Coupon updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(CouponDeletedEvent event) {
        log.info("Coupon deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
