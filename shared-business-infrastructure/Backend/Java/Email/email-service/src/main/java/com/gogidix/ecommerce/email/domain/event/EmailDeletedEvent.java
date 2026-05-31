package com.gogidix.ecommerce.email.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EmailDeletedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    LocalDateTime occurredAt
) implements EmailDomainEvent {

    public EmailDeletedEvent(String aggregateId, String tenantId) {
        this(UUID.randomUUID().toString(), "EmailDeleted", aggregateId, tenantId, LocalDateTime.now());
    }
}