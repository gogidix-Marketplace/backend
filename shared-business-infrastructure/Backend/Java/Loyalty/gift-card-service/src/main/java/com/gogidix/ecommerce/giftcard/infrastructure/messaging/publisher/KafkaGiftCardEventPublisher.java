package com.gogidix.ecommerce.giftcard.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.giftcard.domain.event.GiftCardDomainEvent;
import com.gogidix.ecommerce.giftcard.domain.port.out.GiftCardEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaGiftCardEventPublisher implements GiftCardEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaGiftCardEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaGiftCardEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(GiftCardDomainEvent event) {
        try {
            streamBridge.send("giftcardEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
