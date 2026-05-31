package com.gogidix.ecommerce.airfreight.domain.event;

import java.time.Instant;

public record AirFreightCreatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements AirFreightDomainEvent {
    public AirFreightCreatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "AirFreight_CREATED"; }
}
