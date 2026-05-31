package com.gogidix.aiservices.ainotificationservice.infrastructure.messaging;

import com.gogidix.aiservices.ainotificationservice.domain.port.out.EventPublisherPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class EventPublisherImpl implements EventPublisherPort {

    @Override
    public void publish(String eventType, Map<String, Object> payload) {
        log.info("Publishing event: {} with payload: {}", eventType, payload);
    }

    @Override
    public void publishNotificationSent(String notificationId, String recipientId) {
        Map<String, Object> payload = Map.of(
                "notificationId", notificationId,
                "recipientId", recipientId,
                "timestamp", System.currentTimeMillis()
        );
        publish("notification.sent", payload);
    }

    @Override
    public void publishNotificationFailed(String notificationId, String recipientId, String error) {
        Map<String, Object> payload = Map.of(
                "notificationId", notificationId,
                "recipientId", recipientId,
                "error", error,
                "timestamp", System.currentTimeMillis()
        );
        publish("notification.failed", payload);
    }
}
