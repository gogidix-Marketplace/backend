package com.gogidix.ecommerce.loyalty.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.loyalty.domain.event.LoyaltyDomainEvent;
import com.gogidix.ecommerce.loyalty.domain.port.out.LoyaltyEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaLoyaltyEventPublisher implements LoyaltyEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaLoyaltyEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaLoyaltyEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(LoyaltyDomainEvent event) {
        try {
            streamBridge.send("loyaltyEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
