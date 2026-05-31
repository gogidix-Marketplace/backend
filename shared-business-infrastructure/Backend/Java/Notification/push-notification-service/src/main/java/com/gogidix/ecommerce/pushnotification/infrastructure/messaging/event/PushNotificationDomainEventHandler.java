package com.gogidix.ecommerce.pushnotification.infrastructure.messaging.event;

import com.gogidix.ecommerce.pushnotification.domain.event.PushNotificationCreatedEvent;
import com.gogidix.ecommerce.pushnotification.domain.event.PushNotificationUpdatedEvent;
import com.gogidix.ecommerce.pushnotification.domain.event.PushNotificationDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationDomainEventHandler.class);

    @EventListener
    public void handleCreated(PushNotificationCreatedEvent event) {
        log.info("PushNotification created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(PushNotificationUpdatedEvent event) {
        log.info("PushNotification updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(PushNotificationDeletedEvent event) {
        log.info("PushNotification deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
