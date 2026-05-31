package com.gogidix.courier.assignmentservice.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event fired when an assignment is cancelled.
 */
public class AssignmentCancelledEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String dispatchId;
    private final String driverId;
    private final String cancellationReason;
    private final String cancelledBy;
    private final Instant occurredAt;

    public AssignmentCancelledEvent(
            String aggregateId,
            String tenantId,
            String dispatchId,
            String driverId,
            String cancellationReason,
            String cancelledBy) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.dispatchId = Objects.requireNonNull(dispatchId, "dispatchId cannot be null");
        this.driverId = Objects.requireNonNull(driverId, "driverId cannot be null");
        this.cancellationReason = cancellationReason;
        this.cancelledBy = cancelledBy;
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

    public String getCancellationReason() {
        return cancellationReason;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentCancelledEvent that = (AssignmentCancelledEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "AssignmentCancelledEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", dispatchId='" + dispatchId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", cancellationReason='" + cancellationReason + '\'' +
                ", cancelledBy='" + cancelledBy + '\'' +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
