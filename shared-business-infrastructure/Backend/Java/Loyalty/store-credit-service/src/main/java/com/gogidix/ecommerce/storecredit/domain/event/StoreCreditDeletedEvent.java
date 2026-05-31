package com.gogidix.ecommerce.storecredit.domain.event;

import java.time.Instant;

public record StoreCreditDeletedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements StoreCreditDomainEvent {
    public StoreCreditDeletedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "StoreCredit_DELETED"; }
}
