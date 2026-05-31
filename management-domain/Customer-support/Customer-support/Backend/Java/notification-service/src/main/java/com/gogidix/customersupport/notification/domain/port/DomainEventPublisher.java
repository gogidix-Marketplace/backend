package com.gogidix.customersupport.notification.domain.port;

import com.gogidix.customersupport.notification.domain.event.*;

public interface DomainEventPublisher {
    void publishNotificationCreated(NotificationCreatedEvent event);
    void publishNotificationUpdated(NotificationUpdatedEvent event);
    void publishNotificationDeleted(NotificationDeletedEvent event);
    void publishNotificationQueueCreated(NotificationQueueCreatedEvent event);
    void publishNotificationQueueUpdated(NotificationQueueUpdatedEvent event);
    void publishNotificationQueueDeleted(NotificationQueueDeletedEvent event);
}
