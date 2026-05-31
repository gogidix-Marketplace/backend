package com.gogidix.ecommerce.warehouse.domain.port.out;

import com.gogidix.ecommerce.warehouse.domain.event.WarehouseDomainEvent;

public interface WarehouseEventPublisher {
    void publish(WarehouseDomainEvent event);
}
