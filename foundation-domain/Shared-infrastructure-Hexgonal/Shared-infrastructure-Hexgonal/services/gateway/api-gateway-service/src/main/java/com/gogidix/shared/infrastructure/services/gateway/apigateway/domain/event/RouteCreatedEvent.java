package com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.event;

import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.Route;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a new route is created.
 */
public class RouteCreatedEvent {

    private final String routeId;
    private final String path;
    private final String serviceId;
    private final Instant occurredAt;

    public RouteCreatedEvent(Route route) {
        this.routeId = route.getRouteId();
        this.path = route.getPath();
        this.serviceId = route.getServiceId();
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
        RouteCreatedEvent that = (RouteCreatedEvent) o;
        return Objects.equals(routeId, that.routeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId);
    }

    @Override
    public String toString() {
        return "RouteCreatedEvent{" +
                "routeId='" + routeId + '\'' +
                ", path='" + path + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
