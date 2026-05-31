package com.gogidix.ecommerce.discount.domain.port.out;

import com.gogidix.ecommerce.discount.domain.event.DiscountDomainEvent;

public interface DiscountEventPublisher {
    void publish(DiscountDomainEvent event);
}
