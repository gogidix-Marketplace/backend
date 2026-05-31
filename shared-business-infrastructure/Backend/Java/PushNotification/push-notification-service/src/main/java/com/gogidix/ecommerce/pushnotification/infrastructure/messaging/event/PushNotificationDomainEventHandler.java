package com.gogidix.ecommerce.pushnotification.infrastructure.messaging.event;

import com.gogidix.ecommerce.pushnotification.domain.event.PushNotificationCreatedEvent;
import com.gogidix.ecommerce.pushnotification.domain.event.PushNotificationUpdatedEvent;
import com.gogidix.ecommerce.pushnotification.domain.event.PushNotificationDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationDomainEventHandler {

    @EventListener
    public void handleCreated(PushNotificationCreatedEvent event) {
    }

    @EventListener
    public void handleUpdated(PushNotificationUpdatedEvent event) {
    }

    @EventListener
    public void handleDeleted(PushNotificationDeletedEvent event) {
    }
}