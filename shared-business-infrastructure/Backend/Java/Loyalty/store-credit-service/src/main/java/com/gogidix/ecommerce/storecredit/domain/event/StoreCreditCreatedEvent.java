package com.gogidix.ecommerce.storecredit.domain.event;

import java.time.Instant;

public record StoreCreditCreatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements StoreCreditDomainEvent {
    public StoreCreditCreatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "StoreCredit_CREATED"; }
}
