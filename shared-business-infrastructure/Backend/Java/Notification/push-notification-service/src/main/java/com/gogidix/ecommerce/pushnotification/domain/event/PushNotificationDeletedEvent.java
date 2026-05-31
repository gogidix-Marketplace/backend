package com.gogidix.ecommerce.pushnotification.domain.event;

import java.time.Instant;

public record PushNotificationDeletedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements PushNotificationDomainEvent {
    public PushNotificationDeletedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "PushNotification_DELETED"; }
}
