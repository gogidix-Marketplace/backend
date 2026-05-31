package com.gogidix.ecommerce.pushnotification.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PushNotificationCreatedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    String name,
    LocalDateTime occurredAt
) implements PushNotificationDomainEvent {

    public PushNotificationCreatedEvent(String aggregateId, String tenantId, String name) {
        this(UUID.randomUUID().toString(), "PushNotificationCreated", aggregateId, tenantId, name, LocalDateTime.now());
    }
}