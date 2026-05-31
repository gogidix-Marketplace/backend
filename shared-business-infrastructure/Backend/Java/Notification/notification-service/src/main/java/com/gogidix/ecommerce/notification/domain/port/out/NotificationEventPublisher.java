package com.gogidix.ecommerce.notification.domain.port.out;

import com.gogidix.ecommerce.notification.domain.event.NotificationDomainEvent;

public interface NotificationEventPublisher {
    void publish(NotificationDomainEvent event);
}
