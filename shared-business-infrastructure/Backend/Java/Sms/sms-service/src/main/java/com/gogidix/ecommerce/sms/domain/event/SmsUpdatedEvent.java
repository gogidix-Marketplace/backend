package com.gogidix.ecommerce.sms.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record SmsUpdatedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    String name,
    LocalDateTime occurredAt
) implements SmsDomainEvent {

    public SmsUpdatedEvent(String aggregateId, String tenantId, String name) {
        this(UUID.randomUUID().toString(), "SmsUpdated", aggregateId, tenantId, name, LocalDateTime.now());
    }
}