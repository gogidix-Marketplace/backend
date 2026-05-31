package com.gogidix.ecommerce.pushnotification.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import com.gogidix.ecommerce.pushnotification.domain.event.PushNotificationCreatedEvent;
import com.gogidix.ecommerce.pushnotification.domain.event.PushNotificationUpdatedEvent;
import com.gogidix.ecommerce.pushnotification.domain.event.PushNotificationDeletedEvent;
import com.gogidix.ecommerce.pushnotification.domain.port.out.PushNotificationEventPublisher;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaPushNotificationEventPublisher implements PushNotificationEventPublisher {

    private final StreamBridge streamBridge;

    public KafkaPushNotificationEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void publishCreated(PushNotification entity) {
        PushNotificationCreatedEvent event = new PushNotificationCreatedEvent(entity.getId(), entity.getTenantId(), entity.getName());
        streamBridge.send("pushnotificationEvents-out-0", event);
    }

    @Override
    public void publishUpdated(PushNotification entity) {
        PushNotificationUpdatedEvent event = new PushNotificationUpdatedEvent(entity.getId(), entity.getTenantId(), entity.getName());
        streamBridge.send("pushnotificationEvents-out-0", event);
    }

    @Override
    public void publishDeleted(PushNotification entity) {
        PushNotificationDeletedEvent event = new PushNotificationDeletedEvent(entity.getId(), entity.getTenantId());
        streamBridge.send("pushnotificationEvents-out-0", event);
    }
}