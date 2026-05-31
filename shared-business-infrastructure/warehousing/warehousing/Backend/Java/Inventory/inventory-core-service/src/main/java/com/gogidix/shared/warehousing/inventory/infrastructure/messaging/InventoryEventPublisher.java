package com.gogidix.shared.warehousing.inventory.infrastructure.messaging;

import com.gogidix.shared.warehousing.inventory.domain.events.InventoryCreatedEvent;
import com.gogidix.shared.warehousing.inventory.domain.events.InventoryUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka event publisher for inventory domain events
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.inventory-created:inventory-created}")
    private String inventoryCreatedTopic;

    @Value("${kafka.topic.inventory-updated:inventory-updated}")
    private String inventoryUpdatedTopic;

    /**
     * Publish inventory created event
     */
    public void publishInventoryCreated(InventoryCreatedEvent event) {
        log.info("Publishing inventory created event: {}", event.getEventId());
        kafkaTemplate.send(inventoryCreatedTopic, event.getInventoryId(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish inventory created event: {}", ex.getMessage());
                } else {
                    log.debug("Inventory created event published successfully: {}", event.getEventId());
                }
            });
    }

    /**
     * Publish inventory updated event
     */
    public void publishInventoryUpdated(InventoryUpdatedEvent event) {
        log.info("Publishing inventory updated event: {}", event.getEventId());
        kafkaTemplate.send(inventoryUpdatedTopic, event.getInventoryId(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish inventory updated event: {}", ex.getMessage());
                } else {
                    log.debug("Inventory updated event published successfully: {}", event.getEventId());
                }
            });
    }
}
