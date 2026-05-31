package com.gogidix.ecommerce.pushnotification.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PushNotificationDeletedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    LocalDateTime occurredAt
) implements PushNotificationDomainEvent {

    public PushNotificationDeletedEvent(String aggregateId, String tenantId) {
        this(UUID.randomUUID().toString(), "PushNotificationDeleted", aggregateId, tenantId, LocalDateTime.now());
    }
}