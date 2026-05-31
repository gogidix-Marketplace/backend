package com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a route is deleted.
 */
public class RouteDeletedEvent {

    private final String routeId;
    private final String path;
    private final String serviceId;
    private final Instant occurredAt;

    public RouteDeletedEvent(String routeId, String path, String serviceId) {
        this.routeId = routeId;
        this.path = path;
        this.serviceId = serviceId;
        this.occurredAt = Instant.now();
    }

    public String getRouteId() {
        return routeId;
    }

    public String getPath() {
        return path;
    }

    public String getServiceId() {
        return serviceId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteDeletedEvent that = (RouteDeletedEvent) o;
        return Objects.equals(routeId, that.routeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId);
    }

    @Override
    public String toString() {
        return "RouteDeletedEvent{" +
                "routeId='" + routeId + '\'' +
                ", path='" + path + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
