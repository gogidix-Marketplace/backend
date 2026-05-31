package com.gogidix.ecommerce.communication.domain.port.out;

import com.gogidix.ecommerce.communication.domain.event.CommunicationDomainEvent;

public interface CommunicationEventPublisher {
    void publish(CommunicationDomainEvent event);
}
