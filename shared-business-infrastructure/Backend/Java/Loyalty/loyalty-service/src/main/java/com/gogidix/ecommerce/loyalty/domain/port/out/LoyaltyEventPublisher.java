package com.gogidix.ecommerce.loyalty.domain.port.out;

import com.gogidix.ecommerce.loyalty.domain.event.LoyaltyDomainEvent;

public interface LoyaltyEventPublisher {
    void publish(LoyaltyDomainEvent event);
}
