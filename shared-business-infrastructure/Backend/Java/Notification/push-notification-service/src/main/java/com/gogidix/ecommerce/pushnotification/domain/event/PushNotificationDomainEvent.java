package com.gogidix.ecommerce.pushnotification.domain.event;

import java.time.Instant;

public interface PushNotificationDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
