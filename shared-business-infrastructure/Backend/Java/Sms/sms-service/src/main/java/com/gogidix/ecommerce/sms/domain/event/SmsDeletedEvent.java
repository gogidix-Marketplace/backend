package com.gogidix.ecommerce.sms.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record SmsDeletedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    LocalDateTime occurredAt
) implements SmsDomainEvent {

    public SmsDeletedEvent(String aggregateId, String tenantId) {
        this(UUID.randomUUID().toString(), "SmsDeleted", aggregateId, tenantId, LocalDateTime.now());
    }
}