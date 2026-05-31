package com.gogidix.courier.routingservice.domain.event;

import com.gogidix.courier.routingservice.domain.entity.Route;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Domain event fired when a route is created.
 */
public class RouteCreatedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String routeId;
    private final String driverId;
    private final List<String> orderIds;
    private final Route.RouteStatus status;
    private final Route.RoutePriority priority;
    private final Route.VehicleType vehicleType;
    private final int waypointCount;
    private final Instant occurredAt;

    public RouteCreatedEvent(
            String aggregateId,
            String tenantId,
            String routeId,
            String driverId,
            List<String> orderIds,
            Route.RouteStatus status,
            Route.RoutePriority priority,
            Route.VehicleType vehicleType,
            int waypointCount) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.routeId = Objects.requireNonNull(routeId, "routeId cannot be null");
        this.driverId = Objects.requireNonNull(driverId, "driverId cannot be null");
        this.orderIds = orderIds;
        this.status = status;
        this.priority = priority;
        this.vehicleType = vehicleType;
        this.waypointCount = waypointCount;
        this.occurredAt = Instant.now();
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getDriverId() {
        return driverId;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public Route.RouteStatus getStatus() {
        return status;
    }

    public Route.RoutePriority getPriority() {
        return priority;
    }

    public Route.VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getWaypointCount() {
        return waypointCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteCreatedEvent that = (RouteCreatedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "RouteCreatedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", routeId='" + routeId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", status=" + status +
                ", waypointCount=" + waypointCount +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
