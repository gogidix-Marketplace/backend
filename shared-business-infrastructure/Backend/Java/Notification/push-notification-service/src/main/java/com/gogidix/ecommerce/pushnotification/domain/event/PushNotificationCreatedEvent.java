package com.gogidix.ecommerce.pushnotification.domain.event;

import java.time.Instant;

public record PushNotificationCreatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements PushNotificationDomainEvent {
    public PushNotificationCreatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "PushNotification_CREATED"; }
}
