package com.gogidix.aiservices.aiauthenticationservice.domain.port.out;

public interface EventPublisherPort {
    void publish(String eventType, Object event);
}
