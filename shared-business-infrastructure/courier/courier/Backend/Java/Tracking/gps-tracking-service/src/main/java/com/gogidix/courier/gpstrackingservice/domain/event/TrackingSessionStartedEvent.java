package com.gogidix.courier.gpstrackingservice.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event fired when a tracking session is started.
 */
public class TrackingSessionStartedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String sessionId;
    private final String driverId;
    private final String orderId;
    private final Instant occurredAt;

    public TrackingSessionStartedEvent(
            String aggregateId,
            String tenantId,
            String sessionId,
            String driverId,
            String orderId) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.sessionId = Objects.requireNonNull(sessionId, "sessionId cannot be null");
        this.driverId = Objects.requireNonNull(driverId, "driverId cannot be null");
        this.orderId = orderId;
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

    public String getSessionId() {
        return sessionId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackingSessionStartedEvent that = (TrackingSessionStartedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "TrackingSessionStartedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
