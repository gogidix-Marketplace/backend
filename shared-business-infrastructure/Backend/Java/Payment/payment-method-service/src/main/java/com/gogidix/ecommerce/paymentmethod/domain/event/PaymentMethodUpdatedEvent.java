package com.gogidix.ecommerce.paymentmethod.domain.event;

import java.time.Instant;

public record PaymentMethodUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements PaymentMethodDomainEvent {
    public PaymentMethodUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "PaymentMethod_UPDATED"; }
}
