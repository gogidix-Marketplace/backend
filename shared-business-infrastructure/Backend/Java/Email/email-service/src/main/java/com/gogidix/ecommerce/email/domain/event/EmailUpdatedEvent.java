package com.gogidix.ecommerce.email.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EmailUpdatedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    String name,
    LocalDateTime occurredAt
) implements EmailDomainEvent {

    public EmailUpdatedEvent(String aggregateId, String tenantId, String name) {
        this(UUID.randomUUID().toString(), "EmailUpdated", aggregateId, tenantId, name, LocalDateTime.now());
    }
}