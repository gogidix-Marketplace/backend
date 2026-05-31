package com.gogidix.customersupport.notification.infrastructure.messaging;

import com.gogidix.customersupport.notification.domain.event.*;
import com.gogidix.customersupport.notification.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishNotificationCreated(NotificationCreatedEvent event) {
        log.info("Notification created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishNotificationUpdated(NotificationUpdatedEvent event) {
        log.info("Notification updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishNotificationDeleted(NotificationDeletedEvent event) {
        log.info("Notification deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishNotificationQueueCreated(NotificationQueueCreatedEvent event) {
        log.info("NotificationQueue created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishNotificationQueueUpdated(NotificationQueueUpdatedEvent event) {
        log.info("NotificationQueue updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishNotificationQueueDeleted(NotificationQueueDeletedEvent event) {
        log.info("NotificationQueue deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
