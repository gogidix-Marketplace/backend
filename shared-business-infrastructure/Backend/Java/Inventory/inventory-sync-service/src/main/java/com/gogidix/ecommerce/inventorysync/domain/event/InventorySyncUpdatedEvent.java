package com.gogidix.ecommerce.inventorysync.domain.event;

import java.time.Instant;

public record InventorySyncUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements InventorySyncDomainEvent {
    public InventorySyncUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "InventorySync_UPDATED"; }
}
