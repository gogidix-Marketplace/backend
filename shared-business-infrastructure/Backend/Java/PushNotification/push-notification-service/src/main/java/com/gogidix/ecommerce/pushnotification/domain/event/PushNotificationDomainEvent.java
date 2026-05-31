package com.gogidix.ecommerce.pushnotification.domain.event;

public interface PushNotificationDomainEvent {

    String getEventId();

    String getEventType();

    String getAggregateId();

    String getTenantId();
}