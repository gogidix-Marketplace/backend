package com.gogidix.ecommerce.pushnotification.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.pushnotification.domain.event.PushNotificationDomainEvent;
import com.gogidix.ecommerce.pushnotification.domain.port.out.PushNotificationEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaPushNotificationEventPublisher implements PushNotificationEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaPushNotificationEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaPushNotificationEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(PushNotificationDomainEvent event) {
        try {
            streamBridge.send("pushnotificationEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
