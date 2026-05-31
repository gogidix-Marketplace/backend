package com.gogidix.ecommerce.oceanshipping.domain.event;

import java.time.Instant;

public record OceanShippingCreatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements OceanShippingDomainEvent {
    public OceanShippingCreatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "OceanShipping_CREATED"; }
}
