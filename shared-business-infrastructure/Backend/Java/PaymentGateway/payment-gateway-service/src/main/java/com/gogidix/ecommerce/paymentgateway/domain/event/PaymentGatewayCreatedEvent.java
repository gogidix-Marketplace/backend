package com.gogidix.ecommerce.paymentgateway.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentGatewayCreatedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    String name,
    LocalDateTime occurredAt
) implements PaymentGatewayDomainEvent {

    public PaymentGatewayCreatedEvent(String aggregateId, String tenantId, String name) {
        this(UUID.randomUUID().toString(), "PaymentGatewayCreated", aggregateId, tenantId, name, LocalDateTime.now());
    }
}