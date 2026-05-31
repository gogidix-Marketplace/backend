package com.gogidix.ecommerce.paymentgateway.domain.event;

import java.time.Instant;

public record PaymentGatewayUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements PaymentGatewayDomainEvent {
    public PaymentGatewayUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "PaymentGateway_UPDATED"; }
}
