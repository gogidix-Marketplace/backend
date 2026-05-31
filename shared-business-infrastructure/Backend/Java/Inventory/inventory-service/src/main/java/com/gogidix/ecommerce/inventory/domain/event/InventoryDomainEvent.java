package com.gogidix.ecommerce.inventory.domain.event;

import java.time.Instant;

public interface InventoryDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
