package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain event fired when a market basket is deleted or archived.
 */
public class BasketDeletedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String segmentName;
    private final String deletionType;
    private final Long customerCount;
    private final Instant occurredAt;

    public BasketDeletedEvent(String aggregateId, String tenantId, String segmentName,
                               String deletionType, Long customerCount, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.segmentName = Objects.requireNonNull(segmentName, "segmentName cannot be null");
        this.deletionType = Objects.requireNonNull(deletionType, "deletionType cannot be null");
        this.customerCount = customerCount;
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

    public String getBasketName() {
        return segmentName;
    }

    public String getDeletionType() {
        return deletionType;
    }

    public Long getCustomerCount() {
        return customerCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketDeletedEvent that = (BasketDeletedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "BasketDeletedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", segmentName='" + segmentName + '\'' +
                ", deletionType='" + deletionType + '\'' +
                ", customerCount=" + customerCount +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
