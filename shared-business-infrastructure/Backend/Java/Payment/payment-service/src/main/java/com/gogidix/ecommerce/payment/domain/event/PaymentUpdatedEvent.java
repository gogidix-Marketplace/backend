package com.gogidix.ecommerce.payment.domain.event;

import java.time.Instant;

public record PaymentUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements PaymentDomainEvent {
    public PaymentUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Payment_UPDATED"; }
}
