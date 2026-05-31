package com.gogidix.aiservices.aicontentgenerationservice.domain.port.out;

import java.util.Map;

public interface EventPublisherPort {
    void publish(String eventType, Map<String, Object> payload);

    void publishContentGenerated(String contentId, String userId, String contentType);

    void publishContentFailed(String contentId, String userId, String error);
}
