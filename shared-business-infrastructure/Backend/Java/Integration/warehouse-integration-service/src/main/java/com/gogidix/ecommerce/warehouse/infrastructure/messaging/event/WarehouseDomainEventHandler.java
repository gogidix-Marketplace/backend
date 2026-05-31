package com.gogidix.ecommerce.warehouse.infrastructure.messaging.event;

import com.gogidix.ecommerce.warehouse.domain.event.WarehouseCreatedEvent;
import com.gogidix.ecommerce.warehouse.domain.event.WarehouseUpdatedEvent;
import com.gogidix.ecommerce.warehouse.domain.event.WarehouseDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WarehouseDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(WarehouseDomainEventHandler.class);

    @EventListener
    public void handleCreated(WarehouseCreatedEvent event) {
        log.info("Warehouse created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(WarehouseUpdatedEvent event) {
        log.info("Warehouse updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(WarehouseDeletedEvent event) {
        log.info("Warehouse deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
