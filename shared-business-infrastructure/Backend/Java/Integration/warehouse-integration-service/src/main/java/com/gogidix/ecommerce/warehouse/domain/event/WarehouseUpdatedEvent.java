package com.gogidix.ecommerce.warehouse.domain.event;

import java.time.Instant;

public record WarehouseUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements WarehouseDomainEvent {
    public WarehouseUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Warehouse_UPDATED"; }
}
