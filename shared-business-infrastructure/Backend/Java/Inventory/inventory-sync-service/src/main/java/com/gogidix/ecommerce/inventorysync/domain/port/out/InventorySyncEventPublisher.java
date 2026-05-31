package com.gogidix.ecommerce.inventorysync.domain.port.out;

import com.gogidix.ecommerce.inventorysync.domain.event.InventorySyncDomainEvent;

public interface InventorySyncEventPublisher {
    void publish(InventorySyncDomainEvent event);
}
