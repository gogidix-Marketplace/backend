package com.gogidix.ecommerce.inventory.domain.port.out;

import com.gogidix.ecommerce.inventory.domain.event.InventoryDomainEvent;

public interface InventoryEventPublisher {
    void publish(InventoryDomainEvent event);
}
