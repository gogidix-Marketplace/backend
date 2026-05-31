package com.gogidix.ecommerce.oceanshipping.domain.event;

import java.time.Instant;

public record OceanShippingUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements OceanShippingDomainEvent {
    public OceanShippingUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "OceanShipping_UPDATED"; }
}
