package com.gogidix.courier.tenantservice.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event fired when a new tenant is created.
 */
public class TenantCreatedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String tenantName;
    private final Instant occurredAt;

    public TenantCreatedEvent(String aggregateId, String tenantId, String tenantName) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.tenantName = Objects.requireNonNull(tenantName, "tenantName cannot be null");
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

    public String getTenantName() {
        return tenantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantCreatedEvent that = (TenantCreatedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "TenantCreatedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
