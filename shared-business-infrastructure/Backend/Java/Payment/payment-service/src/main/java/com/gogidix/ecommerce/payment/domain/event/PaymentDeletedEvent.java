package com.gogidix.ecommerce.payment.domain.event;

import java.time.Instant;

public record PaymentDeletedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements PaymentDomainEvent {
    public PaymentDeletedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Payment_DELETED"; }
}
