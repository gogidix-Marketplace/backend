package com.gogidix.aiservices.aigatewayservice.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Base event for route-related domain events.
 */
public abstract class RouteEvent {

    private final String eventId;
    private final String routeId;
    private final String tenantId;
    private final String eventType;
    private final Instant timestamp;

    protected RouteEvent(String routeId, String tenantId, String eventType) {
        this.eventId = UUID.randomUUID().toString();
        this.routeId = routeId;
        this.tenantId = tenantId;
        this.eventType = eventType;
        this.timestamp = Instant.now();
    }

    public String getEventId() {
        return eventId;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getEventType() {
        return eventType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
