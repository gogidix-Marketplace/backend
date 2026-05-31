package com.gogidix.courier.assignmentservice.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event fired when a driver assignment fails or is cancelled.
 */
public class AssignmentFailedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String dispatchId;
    private final String driverId;
    private final String failureReason;
    private final FailureType failureType;
    private final Instant occurredAt;

    public AssignmentFailedEvent(
            String aggregateId,
            String tenantId,
            String dispatchId,
            String driverId,
            String failureReason,
            FailureType failureType) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.dispatchId = Objects.requireNonNull(dispatchId, "dispatchId cannot be null");
        this.driverId = Objects.requireNonNull(driverId, "driverId cannot be null");
        this.failureReason = failureReason;
        this.failureType = failureType != null ? failureType : FailureType.OTHER;
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

    public String getFailureReason() {
        return failureReason;
    }

    public FailureType getFailureType() {
        return failureType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentFailedEvent that = (AssignmentFailedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "AssignmentFailedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", dispatchId='" + dispatchId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", failureReason='" + failureReason + '\'' +
                ", failureType=" + failureType +
                ", occurredAt=" + occurredAt +
                '}';
    }

    /**
     * Failure type enum.
     */
    public enum FailureType {
        DRIVER_UNAVAILABLE,
        DRIVER_REJECTED,
        TIMEOUT,
        CANCELLED_BY_USER,
        CANCELLED_BY_DRIVER,
        CANCELLED_BY_SYSTEM,
        TECHNICAL_ERROR,
        LOCATION_UNREACHABLE,
        OTHER
    }
}
