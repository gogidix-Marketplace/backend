package com.gogidix.ecommerce.paymentgateway.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentGatewayUpdatedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    String name,
    LocalDateTime occurredAt
) implements PaymentGatewayDomainEvent {

    public PaymentGatewayUpdatedEvent(String aggregateId, String tenantId, String name) {
        this(UUID.randomUUID().toString(), "PaymentGatewayUpdated", aggregateId, tenantId, name, LocalDateTime.now());
    }
}