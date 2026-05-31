package com.gogidix.ecommerce.paymentmethod.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentMethodCreatedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    String name,
    LocalDateTime occurredAt
) implements PaymentMethodDomainEvent {

    public PaymentMethodCreatedEvent(String aggregateId, String tenantId, String name) {
        this(UUID.randomUUID().toString(), "PaymentMethodCreated", aggregateId, tenantId, name, LocalDateTime.now());
    }
}