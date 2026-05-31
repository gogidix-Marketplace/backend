package com.gogidix.ecommerce.notification.domain.event;

import java.time.Instant;

public record NotificationUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements NotificationDomainEvent {
    public NotificationUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Notification_UPDATED"; }
}
