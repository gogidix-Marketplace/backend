package com.gogidix.aiservices.leadgenerationaiservice.domain.port.out;

import java.util.Map;

public interface EventPublisherPort {
    void publish(String eventType, Map<String, Object> payload);
    void publish(String eventType, Object payload);
}
