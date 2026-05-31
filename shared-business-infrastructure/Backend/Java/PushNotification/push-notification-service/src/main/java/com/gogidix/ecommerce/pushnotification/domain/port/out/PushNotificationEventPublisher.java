package com.gogidix.ecommerce.pushnotification.domain.port.out;

import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;

public interface PushNotificationEventPublisher {

    void publishCreated(PushNotification entity);

    void publishUpdated(PushNotification entity);

    void publishDeleted(PushNotification entity);
}