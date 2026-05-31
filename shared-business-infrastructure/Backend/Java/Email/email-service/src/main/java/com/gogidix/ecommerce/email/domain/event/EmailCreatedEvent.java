package com.gogidix.ecommerce.email.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EmailCreatedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    String name,
    LocalDateTime occurredAt
) implements EmailDomainEvent {

    public EmailCreatedEvent(String aggregateId, String tenantId, String name) {
        this(UUID.randomUUID().toString(), "EmailCreated", aggregateId, tenantId, name, LocalDateTime.now());
    }
}