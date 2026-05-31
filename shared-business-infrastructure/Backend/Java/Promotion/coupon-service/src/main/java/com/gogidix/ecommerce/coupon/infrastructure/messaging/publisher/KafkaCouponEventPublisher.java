package com.gogidix.ecommerce.coupon.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.coupon.domain.event.CouponDomainEvent;
import com.gogidix.ecommerce.coupon.domain.port.out.CouponEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaCouponEventPublisher implements CouponEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaCouponEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaCouponEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(CouponDomainEvent event) {
        try {
            streamBridge.send("couponEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
