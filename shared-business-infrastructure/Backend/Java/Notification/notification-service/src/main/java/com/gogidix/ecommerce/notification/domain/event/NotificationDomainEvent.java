package com.gogidix.ecommerce.notification.domain.event;

import java.time.Instant;

public interface NotificationDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
