package com.gogidix.courier.tenantservice.domain.event;

import com.gogidix.courier.tenantservice.domain.entity.Tenant;
import java.time.Instant;
import java.util.Objects;

/**
 * Domain event fired when a tenant is updated.
 */
public class TenantUpdatedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String tenantName;
    private final Tenant.TenantStatus status;
    private final Instant occurredAt;

    public TenantUpdatedEvent(String aggregateId, String tenantId, String tenantName, Tenant.TenantStatus status) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.tenantName = Objects.requireNonNull(tenantName, "tenantName cannot be null");
        this.status = status;
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

    public Tenant.TenantStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantUpdatedEvent that = (TenantUpdatedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "TenantUpdatedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", status=" + status +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
