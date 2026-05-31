package com.gogidix.ecommerce.sms.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record SmsCreatedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    String name,
    LocalDateTime occurredAt
) implements SmsDomainEvent {

    public SmsCreatedEvent(String aggregateId, String tenantId, String name) {
        this(UUID.randomUUID().toString(), "SmsCreated", aggregateId, tenantId, name, LocalDateTime.now());
    }
}