package com.gogidix.ecommerce.discount.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.discount.domain.event.DiscountDomainEvent;
import com.gogidix.ecommerce.discount.domain.port.out.DiscountEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaDiscountEventPublisher implements DiscountEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaDiscountEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaDiscountEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(DiscountDomainEvent event) {
        try {
            streamBridge.send("discountEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
