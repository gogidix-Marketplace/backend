package com.gogidix.ecommerce.inventorysync.domain.event;

import java.time.Instant;

public interface InventorySyncDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
