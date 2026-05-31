package com.gogidix.ecommerce.notification.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.notification.domain.event.NotificationDomainEvent;
import com.gogidix.ecommerce.notification.domain.port.out.NotificationEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaNotificationEventPublisher implements NotificationEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaNotificationEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaNotificationEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(NotificationDomainEvent event) {
        try {
            streamBridge.send("notificationEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
