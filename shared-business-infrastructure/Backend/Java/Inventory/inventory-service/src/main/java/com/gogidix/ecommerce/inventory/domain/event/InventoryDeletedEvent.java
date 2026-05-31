package com.gogidix.ecommerce.inventory.domain.event;

import java.time.Instant;

public record InventoryDeletedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements InventoryDomainEvent {
    public InventoryDeletedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Inventory_DELETED"; }
}
