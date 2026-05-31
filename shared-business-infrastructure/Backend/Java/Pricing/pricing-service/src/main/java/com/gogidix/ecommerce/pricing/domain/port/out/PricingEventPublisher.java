package com.gogidix.ecommerce.pricing.domain.port.out;

import com.gogidix.ecommerce.pricing.domain.event.PricingDomainEvent;

public interface PricingEventPublisher {

    void publish(PricingDomainEvent event);
}
