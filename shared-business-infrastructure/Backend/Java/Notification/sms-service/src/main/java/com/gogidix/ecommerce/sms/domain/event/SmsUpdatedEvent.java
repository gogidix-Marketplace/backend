package com.gogidix.ecommerce.sms.domain.event;

import java.time.Instant;

public record SmsUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements SmsDomainEvent {
    public SmsUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Sms_UPDATED"; }
}
