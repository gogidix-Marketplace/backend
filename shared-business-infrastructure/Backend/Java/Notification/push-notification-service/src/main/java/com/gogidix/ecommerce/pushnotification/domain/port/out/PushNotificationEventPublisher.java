package com.gogidix.ecommerce.pushnotification.domain.port.out;

import com.gogidix.ecommerce.pushnotification.domain.event.PushNotificationDomainEvent;

public interface PushNotificationEventPublisher {
    void publish(PushNotificationDomainEvent event);
}
