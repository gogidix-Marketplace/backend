package com.gogidix.courier.tenantservice.domain.event;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Domain event fired when a tenant configuration is changed.
 */
public class TenantConfigChangedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String tenantName;
    private final Map<String, Object> changedSettings;
    private final Instant occurredAt;

    public TenantConfigChangedEvent(String aggregateId, String tenantId, String tenantName,
                                    Map<String, Object> changedSettings) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.tenantName = Objects.requireNonNull(tenantName, "tenantName cannot be null");
        this.changedSettings = changedSettings;
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

    public Map<String, Object> getChangedSettings() {
        return changedSettings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantConfigChangedEvent that = (TenantConfigChangedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "TenantConfigChangedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", changedSettings=" + changedSettings +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
