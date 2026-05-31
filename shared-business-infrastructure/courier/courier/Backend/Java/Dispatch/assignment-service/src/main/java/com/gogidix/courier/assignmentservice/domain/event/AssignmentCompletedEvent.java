package com.gogidix.courier.assignmentservice.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event fired when a driver assignment is completed.
 */
public class AssignmentCompletedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String dispatchId;
    private final String driverId;
    private final Double actualDistanceKm;
    private final Integer actualDurationMinutes;
    private final Instant occurredAt;

    public AssignmentCompletedEvent(
            String aggregateId,
            String tenantId,
            String dispatchId,
            String driverId,
            Double actualDistanceKm,
            Integer actualDurationMinutes) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.dispatchId = Objects.requireNonNull(dispatchId, "dispatchId cannot be null");
        this.driverId = Objects.requireNonNull(driverId, "driverId cannot be null");
        this.actualDistanceKm = actualDistanceKm;
        this.actualDurationMinutes = actualDurationMinutes;
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

    public String getDispatchId() {
        return dispatchId;
    }

    public String getDriverId() {
        return driverId;
    }

    public Double getActualDistanceKm() {
        return actualDistanceKm;
    }

    public Integer getActualDurationMinutes() {
        return actualDurationMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentCompletedEvent that = (AssignmentCompletedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "AssignmentCompletedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", dispatchId='" + dispatchId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", actualDistanceKm=" + actualDistanceKm +
                ", actualDurationMinutes=" + actualDurationMinutes +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
