package com.gogidix.ecommerce.wishlist.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.wishlist.domain.event.WishlistDomainEvent;
import com.gogidix.ecommerce.wishlist.domain.port.out.WishlistEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaWishlistEventPublisher implements WishlistEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaWishlistEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaWishlistEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(WishlistDomainEvent event) {
        try {
            streamBridge.send("wishlistEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
