package com.gogidix.courier.routingservice.domain.event;

import com.gogidix.courier.routingservice.domain.entity.Route;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Domain event fired when a route is completed.
 */
public class RouteCompletedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String routeId;
    private final String driverId;
    private final List<String> orderIds;
    private final Double actualDistanceMeters;
    private final Integer actualDurationSeconds;
    private final Double estimatedDistanceMeters;
    private final Integer estimatedDurationSeconds;
    private final Double efficiency;
    private final Integer completedWaypoints;
    private final Integer totalWaypoints;
    private final Instant occurredAt;

    public RouteCompletedEvent(
            String aggregateId,
            String tenantId,
            String routeId,
            String driverId,
            List<String> orderIds,
            Double actualDistanceMeters,
            Integer actualDurationSeconds,
            Double estimatedDistanceMeters,
            Integer estimatedDurationSeconds,
            Double efficiency,
            Integer completedWaypoints,
            Integer totalWaypoints) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.routeId = Objects.requireNonNull(routeId, "routeId cannot be null");
        this.driverId = Objects.requireNonNull(driverId, "driverId cannot be null");
        this.orderIds = orderIds;
        this.actualDistanceMeters = actualDistanceMeters;
        this.actualDurationSeconds = actualDurationSeconds;
        this.estimatedDistanceMeters = estimatedDistanceMeters;
        this.estimatedDurationSeconds = estimatedDurationSeconds;
        this.efficiency = efficiency;
        this.completedWaypoints = completedWaypoints;
        this.totalWaypoints = totalWaypoints;
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

    public Double getActualDistanceMeters() {
        return actualDistanceMeters;
    }

    public Integer getActualDurationSeconds() {
        return actualDurationSeconds;
    }

    public Double getEstimatedDistanceMeters() {
        return estimatedDistanceMeters;
    }

    public Integer getEstimatedDurationSeconds() {
        return estimatedDurationSeconds;
    }

    public Double getEfficiency() {
        return efficiency;
    }

    public Integer getCompletedWaypoints() {
        return completedWaypoints;
    }

    public Integer getTotalWaypoints() {
        return totalWaypoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteCompletedEvent that = (RouteCompletedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "RouteCompletedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", routeId='" + routeId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", actualDistanceMeters=" + actualDistanceMeters +
                ", actualDurationSeconds=" + actualDurationSeconds +
                ", efficiency=" + efficiency +
                ", completedWaypoints=" + completedWaypoints +
                ", totalWaypoints=" + totalWaypoints +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
