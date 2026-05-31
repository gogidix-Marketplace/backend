package com.gogidix.ecommerce.paymentgateway.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentGatewayDeletedEvent(
    String eventId,
    String eventType,
    String aggregateId,
    String tenantId,
    LocalDateTime occurredAt
) implements PaymentGatewayDomainEvent {

    public PaymentGatewayDeletedEvent(String aggregateId, String tenantId) {
        this(UUID.randomUUID().toString(), "PaymentGatewayDeleted", aggregateId, tenantId, LocalDateTime.now());
    }
}