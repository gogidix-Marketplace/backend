package com.gogidix.ecommerce.notification.infrastructure.messaging.event;

import com.gogidix.ecommerce.notification.domain.event.NotificationCreatedEvent;
import com.gogidix.ecommerce.notification.domain.event.NotificationUpdatedEvent;
import com.gogidix.ecommerce.notification.domain.event.NotificationDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(NotificationDomainEventHandler.class);

    @EventListener
    public void handleCreated(NotificationCreatedEvent event) {
        log.info("Notification created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(NotificationUpdatedEvent event) {
        log.info("Notification updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(NotificationDeletedEvent event) {
        log.info("Notification deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
