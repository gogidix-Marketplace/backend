package com.gogidix.ecommerce.airfreight.domain.port.out;

import com.gogidix.ecommerce.airfreight.domain.event.AirFreightDomainEvent;

public interface AirFreightEventPublisher {
    void publish(AirFreightDomainEvent event);
}
