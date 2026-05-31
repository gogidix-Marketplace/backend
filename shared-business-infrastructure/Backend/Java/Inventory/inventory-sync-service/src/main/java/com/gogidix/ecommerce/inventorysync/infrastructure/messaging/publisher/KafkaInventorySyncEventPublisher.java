package com.gogidix.ecommerce.inventorysync.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.inventorysync.domain.event.InventorySyncDomainEvent;
import com.gogidix.ecommerce.inventorysync.domain.port.out.InventorySyncEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaInventorySyncEventPublisher implements InventorySyncEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaInventorySyncEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaInventorySyncEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(InventorySyncDomainEvent event) {
        try {
            streamBridge.send("inventorysyncEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
