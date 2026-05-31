package com.gogidix.aiservices.aipersonalizationservice.domain.port.out;

public interface EventPublisherPort {
    void publish(String eventType, Object event);
    void publish(String eventType, Object event, String topic);
}
