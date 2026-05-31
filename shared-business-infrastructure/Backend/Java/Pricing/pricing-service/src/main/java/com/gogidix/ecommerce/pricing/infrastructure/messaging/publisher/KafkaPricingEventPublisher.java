package com.gogidix.ecommerce.pricing.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.pricing.domain.event.PricingDomainEvent;
import com.gogidix.ecommerce.pricing.domain.port.out.PricingEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaPricingEventPublisher implements PricingEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaPricingEventPublisher.class);

    private final StreamBridge streamBridge;

    public KafkaPricingEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void publish(PricingDomainEvent event) {
        try {
            streamBridge.send("pricingEvents-out-0", event);
            log.info("Published pricing event: type={}, tenantId={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish pricing event: type={}, tenantId={}, error={}",
                    event.eventType(), event.tenantId(), e.getMessage());
        }
    }
}
