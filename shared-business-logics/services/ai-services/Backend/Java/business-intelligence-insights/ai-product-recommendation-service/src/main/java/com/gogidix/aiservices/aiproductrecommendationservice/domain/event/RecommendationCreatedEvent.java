package com.gogidix.aiservices.aiproductrecommendationservice.domain.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain event fired when a new product recommendation is created.
 */
public class RecommendationCreatedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String segmentName;
    private final String segmentType;
    private final Instant occurredAt;

    public RecommendationCreatedEvent(String aggregateId, String tenantId, String segmentName,
                               String segmentType, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.segmentName = Objects.requireNonNull(segmentName, "segmentName cannot be null");
        this.segmentType = Objects.requireNonNull(segmentType, "segmentType cannot be null");
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

    public String getRecommendationName() {
        return segmentName;
    }

    public String getRecommendationType() {
        return segmentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecommendationCreatedEvent that = (RecommendationCreatedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "RecommendationCreatedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", segmentName='" + segmentName + '\'' +
                ", segmentType='" + segmentType + '\'' +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
