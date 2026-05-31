package com.gogidix.ecommerce.oceanshipping.domain.port.out;

import com.gogidix.ecommerce.oceanshipping.domain.event.OceanShippingDomainEvent;

public interface OceanShippingEventPublisher {
    void publish(OceanShippingDomainEvent event);
}
