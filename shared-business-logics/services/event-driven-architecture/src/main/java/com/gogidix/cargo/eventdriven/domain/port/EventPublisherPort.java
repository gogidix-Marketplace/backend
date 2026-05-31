package com.gogidix.cargo.eventdriven.domain.port;

import com.gogidix.cargo.eventdriven.domain.event.BaseDomainEvent;

public interface EventPublisherPort {
    void publish(String topic, String key, BaseDomainEvent event);
    void publish(String topic, BaseDomainEvent event);
}
