package com.gogidix.ecommerce.customer.domain.port.out;

import com.gogidix.ecommerce.customer.domain.event.CustomerDomainEvent;

public interface CustomerEventPublisher {
    void publish(CustomerDomainEvent event);
}
