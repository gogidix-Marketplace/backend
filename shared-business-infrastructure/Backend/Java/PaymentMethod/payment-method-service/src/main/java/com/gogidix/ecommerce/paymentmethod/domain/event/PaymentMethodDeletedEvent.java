package com.gogidix.ecommerce.paymentmethod.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentMethodDeletedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    LocalDateTime occurredAt
) implements PaymentMethodDomainEvent {

    public PaymentMethodDeletedEvent(String aggregateId, String tenantId) {
        this(UUID.randomUUID().toString(), "PaymentMethodDeleted", aggregateId, tenantId, LocalDateTime.now());
    }
}