package com.gogidix.ecommerce.inventory.domain.event;

import java.time.Instant;

public record InventoryCreatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements InventoryDomainEvent {
    public InventoryCreatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Inventory_CREATED"; }
}
