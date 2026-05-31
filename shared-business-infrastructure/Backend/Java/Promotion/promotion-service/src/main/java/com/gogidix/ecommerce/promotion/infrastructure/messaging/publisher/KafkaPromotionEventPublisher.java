package com.gogidix.ecommerce.promotion.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.promotion.domain.event.PromotionDomainEvent;
import com.gogidix.ecommerce.promotion.domain.port.out.PromotionEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaPromotionEventPublisher implements PromotionEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaPromotionEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaPromotionEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(PromotionDomainEvent event) {
        try {
            streamBridge.send("promotionEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
