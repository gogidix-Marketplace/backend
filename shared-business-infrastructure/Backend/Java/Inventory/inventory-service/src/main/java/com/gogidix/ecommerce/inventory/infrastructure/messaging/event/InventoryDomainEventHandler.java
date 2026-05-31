package com.gogidix.ecommerce.inventory.infrastructure.messaging.event;

import com.gogidix.ecommerce.inventory.domain.event.InventoryCreatedEvent;
import com.gogidix.ecommerce.inventory.domain.event.InventoryUpdatedEvent;
import com.gogidix.ecommerce.inventory.domain.event.InventoryDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(InventoryDomainEventHandler.class);

    @EventListener
    public void handleCreated(InventoryCreatedEvent event) {
        log.info("Inventory created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(InventoryUpdatedEvent event) {
        log.info("Inventory updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(InventoryDeletedEvent event) {
        log.info("Inventory deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
