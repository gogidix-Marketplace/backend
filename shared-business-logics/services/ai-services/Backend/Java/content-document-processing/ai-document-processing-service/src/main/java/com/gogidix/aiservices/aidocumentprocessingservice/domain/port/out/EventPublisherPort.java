package com.gogidix.aiservices.aidocumentprocessingservice.domain.port.out;

import java.util.Map;

public interface EventPublisherPort {
    void publish(String topic, Map<String, Object> eventData);
}
