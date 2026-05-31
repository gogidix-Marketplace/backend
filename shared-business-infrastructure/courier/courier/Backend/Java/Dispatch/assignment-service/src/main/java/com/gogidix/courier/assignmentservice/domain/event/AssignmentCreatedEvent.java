package com.gogidix.courier.assignmentservice.domain.event;

import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event fired when a new driver assignment is created.
 */
public class AssignmentCreatedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String dispatchId;
    private final String driverId;
    private final DriverAssignment.AssignmentStatus status;
    private final DriverAssignment.AssignmentPriority priority;
    private final Instant occurredAt;

    public AssignmentCreatedEvent(
            String aggregateId,
            String tenantId,
            String dispatchId,
            String driverId,
            DriverAssignment.AssignmentStatus status,
            DriverAssignment.AssignmentPriority priority) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.dispatchId = Objects.requireNonNull(dispatchId, "dispatchId cannot be null");
        this.driverId = Objects.requireNonNull(driverId, "driverId cannot be null");
        this.status = status;
        this.priority = priority;
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

    public DriverAssignment.AssignmentStatus getStatus() {
        return status;
    }

    public DriverAssignment.AssignmentPriority getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentCreatedEvent that = (AssignmentCreatedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "AssignmentCreatedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", dispatchId='" + dispatchId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", status=" + status +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
