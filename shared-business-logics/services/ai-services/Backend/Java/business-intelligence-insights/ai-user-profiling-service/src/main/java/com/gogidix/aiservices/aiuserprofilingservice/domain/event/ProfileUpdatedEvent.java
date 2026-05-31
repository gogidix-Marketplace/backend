package com.gogidix.aiservices.aiuserprofilingservice.domain.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain event fired when a customer segment is updated.
 */
public class ProfileUpdatedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String oldName;
    private final String newName;
    private final String changeType;
    private final Instant occurredAt;

    public ProfileUpdatedEvent(String aggregateId, String tenantId, String oldName,
                               String newName, String changeType, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.oldName = oldName;
        this.newName = Objects.requireNonNull(newName, "newName cannot be null");
        this.changeType = Objects.requireNonNull(changeType, "changeType cannot be null");
        this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt cannot be null");
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

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }

    public String getChangeType() {
        return changeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileUpdatedEvent that = (ProfileUpdatedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "ProfileUpdatedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", oldName='" + oldName + '\'' +
                ", newName='" + newName + '\'' +
                ", changeType='" + changeType + '\'' +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
