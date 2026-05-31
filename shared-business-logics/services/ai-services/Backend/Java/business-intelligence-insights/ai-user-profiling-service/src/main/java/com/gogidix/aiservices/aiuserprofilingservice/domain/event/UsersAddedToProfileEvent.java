package com.gogidix.aiservices.aiuserprofilingservice.domain.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain event fired when customers are added to a segment.
 */
public class UsersAddedToProfileEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String segmentName;
    private final Integer customersAdded;
    private final Long totalUserCount;
    private final Instant occurredAt;

    public UsersAddedToProfileEvent(String aggregateId, String tenantId, String segmentName,
                                        Integer customersAdded, Long totalUserCount, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.segmentName = Objects.requireNonNull(segmentName, "segmentName cannot be null");
        this.customersAdded = Objects.requireNonNull(customersAdded, "customersAdded cannot be null");
        this.totalUserCount = Objects.requireNonNull(totalUserCount, "totalUserCount cannot be null");
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

    public String getProfileName() {
        return segmentName;
    }

    public Integer getUsersAdded() {
        return customersAdded;
    }

    public Long getTotalUserCount() {
        return totalUserCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersAddedToProfileEvent that = (UsersAddedToProfileEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "UsersAddedToProfileEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", segmentName='" + segmentName + '\'' +
                ", customersAdded=" + customersAdded +
                ", totalUserCount=" + totalUserCount +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
