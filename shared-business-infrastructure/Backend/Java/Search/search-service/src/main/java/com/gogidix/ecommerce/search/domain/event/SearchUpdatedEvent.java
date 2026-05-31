package com.gogidix.ecommerce.search.domain.event;

import java.time.Instant;

public record SearchUpdatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements SearchDomainEvent {
    public SearchUpdatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Search_UPDATED"; }
}
