package com.gogidix.ecommerce.paymentmethod.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentMethodUpdatedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    String name,
    LocalDateTime occurredAt
) implements PaymentMethodDomainEvent {

    public PaymentMethodUpdatedEvent(String aggregateId, String tenantId, String name) {
        this(UUID.randomUUID().toString(), "PaymentMethodUpdated", aggregateId, tenantId, name, LocalDateTime.now());
    }
}