package com.gogidix.ecommerce.warehouse.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.warehouse.domain.event.WarehouseDomainEvent;
import com.gogidix.ecommerce.warehouse.domain.port.out.WarehouseEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaWarehouseEventPublisher implements WarehouseEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaWarehouseEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaWarehouseEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(WarehouseDomainEvent event) {
        try {
            streamBridge.send("warehouseEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
