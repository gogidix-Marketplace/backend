package com.gogidix.ecommerce.analytics.domain.event;

import java.time.Instant;

public record AnalyticsDeletedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements AnalyticsDomainEvent {
    public AnalyticsDeletedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Analytics_DELETED"; }
}
