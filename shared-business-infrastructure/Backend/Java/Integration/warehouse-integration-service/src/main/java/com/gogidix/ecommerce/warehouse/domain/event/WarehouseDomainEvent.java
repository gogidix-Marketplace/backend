package com.gogidix.ecommerce.warehouse.domain.event;

import java.time.Instant;

public interface WarehouseDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
