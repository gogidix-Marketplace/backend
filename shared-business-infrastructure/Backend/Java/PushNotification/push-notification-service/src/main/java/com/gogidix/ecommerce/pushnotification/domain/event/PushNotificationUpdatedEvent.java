package com.gogidix.ecommerce.pushnotification.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PushNotificationUpdatedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    String name,
    LocalDateTime occurredAt
) implements PushNotificationDomainEvent {

    public PushNotificationUpdatedEvent(String aggregateId, String tenantId, String name) {
        this(UUID.randomUUID().toString(), "PushNotificationUpdated", aggregateId, tenantId, name, LocalDateTime.now());
    }
}