package com.gogidix.ecommerce.inventorysync.infrastructure.messaging.event;

import com.gogidix.ecommerce.inventorysync.domain.event.InventorySyncCreatedEvent;
import com.gogidix.ecommerce.inventorysync.domain.event.InventorySyncUpdatedEvent;
import com.gogidix.ecommerce.inventorysync.domain.event.InventorySyncDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InventorySyncDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(InventorySyncDomainEventHandler.class);

    @EventListener
    public void handleCreated(InventorySyncCreatedEvent event) {
        log.info("InventorySync created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(InventorySyncUpdatedEvent event) {
        log.info("InventorySync updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(InventorySyncDeletedEvent event) {
        log.info("InventorySync deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
