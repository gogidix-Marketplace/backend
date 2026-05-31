package com.gogidix.shared.courier.notification.infrastructure.messaging;

import com.gogidix.shared.courier.notification.domain.events.NotificationReadEvent;
import com.gogidix.shared.courier.notification.domain.events.NotificationSentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Event publisher for notification events
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_NOTIFICATION_SENT = "notification.sent";
    private static final String TOPIC_NOTIFICATION_READ = "notification.read";

    public void publishNotificationSent(NotificationSentEvent event) {
        try {
            String key = event.getTenantId() + ":" + event.getRecipientId();
            kafkaTemplate.send(TOPIC_NOTIFICATION_SENT, key, event);
            log.info("Published notification sent event for recipient: {}", event.getRecipientId());
        } catch (Exception e) {
            log.error("Failed to publish notification sent event: {}", e.getMessage());
        }
    }

    public void publishNotificationRead(NotificationReadEvent event) {
        try {
            String key = event.getTenantId() + ":" + event.getRecipientId();
            kafkaTemplate.send(TOPIC_NOTIFICATION_READ, key, event);
            log.debug("Published notification read event for notification: {}", event.getNotificationId());
        } catch (Exception e) {
            log.error("Failed to publish notification read event: {}", e.getMessage());
        }
    }
}
