package com.gogidix.courier.routingservice.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event fired when a route is optimized.
 */
public class RouteOptimizedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String routeId;
    private final String optimizedRouteId;
    private final String driverId;
    private final Double distanceSavedMeters;
    private final Integer timeSavedSeconds;
    private final Double distanceImprovementPercent;
    private final Double timeImprovementPercent;
    private final Double optimizationScore;
    private final String algorithm;
    private final Instant occurredAt;

    public RouteOptimizedEvent(
            String aggregateId,
            String tenantId,
            String routeId,
            String optimizedRouteId,
            String driverId,
            Double distanceSavedMeters,
            Integer timeSavedSeconds,
            Double distanceImprovementPercent,
            Double timeImprovementPercent,
            Double optimizationScore,
            String algorithm) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.routeId = Objects.requireNonNull(routeId, "routeId cannot be null");
        this.optimizedRouteId = optimizedRouteId;
        this.driverId = driverId;
        this.distanceSavedMeters = distanceSavedMeters;
        this.timeSavedSeconds = timeSavedSeconds;
        this.distanceImprovementPercent = distanceImprovementPercent;
        this.timeImprovementPercent = timeImprovementPercent;
        this.optimizationScore = optimizationScore;
        this.algorithm = algorithm;
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

    public String getOptimizedRouteId() {
        return optimizedRouteId;
    }

    public String getDriverId() {
        return driverId;
    }

    public Double getDistanceSavedMeters() {
        return distanceSavedMeters;
    }

    public Integer getTimeSavedSeconds() {
        return timeSavedSeconds;
    }

    public Double getDistanceImprovementPercent() {
        return distanceImprovementPercent;
    }

    public Double getTimeImprovementPercent() {
        return timeImprovementPercent;
    }

    public Double getOptimizationScore() {
        return optimizationScore;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteOptimizedEvent that = (RouteOptimizedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "RouteOptimizedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", routeId='" + routeId + '\'' +
                ", optimizedRouteId='" + optimizedRouteId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", optimizationScore=" + optimizationScore +
                ", distanceImprovementPercent=" + distanceImprovementPercent +
                ", timeImprovementPercent=" + timeImprovementPercent +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
