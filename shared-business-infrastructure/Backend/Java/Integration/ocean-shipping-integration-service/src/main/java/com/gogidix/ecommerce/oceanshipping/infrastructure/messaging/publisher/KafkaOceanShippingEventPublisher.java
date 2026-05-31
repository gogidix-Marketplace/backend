package com.gogidix.ecommerce.oceanshipping.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.oceanshipping.domain.event.OceanShippingDomainEvent;
import com.gogidix.ecommerce.oceanshipping.domain.port.out.OceanShippingEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaOceanShippingEventPublisher implements OceanShippingEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaOceanShippingEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaOceanShippingEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(OceanShippingDomainEvent event) {
        try {
            streamBridge.send("oceanshippingEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
