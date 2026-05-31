package com.gogidix.aiservices.ainotificationservice.domain.port.out;

import java.util.Map;

public interface EventPublisherPort {
    void publish(String eventType, Map<String, Object> payload);

    void publishNotificationSent(String notificationId, String recipientId);

    void publishNotificationFailed(String notificationId, String recipientId, String error);
}
