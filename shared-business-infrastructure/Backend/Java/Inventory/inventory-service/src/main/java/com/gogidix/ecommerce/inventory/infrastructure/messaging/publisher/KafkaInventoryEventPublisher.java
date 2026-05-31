package com.gogidix.ecommerce.inventory.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.inventory.domain.event.InventoryDomainEvent;
import com.gogidix.ecommerce.inventory.domain.port.out.InventoryEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaInventoryEventPublisher implements InventoryEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaInventoryEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaInventoryEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(InventoryDomainEvent event) {
        try {
            streamBridge.send("inventoryEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
