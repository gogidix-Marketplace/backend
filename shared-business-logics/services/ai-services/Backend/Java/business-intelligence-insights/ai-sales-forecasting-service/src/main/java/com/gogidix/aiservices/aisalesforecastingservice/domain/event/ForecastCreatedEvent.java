package com.gogidix.aiservices.aisalesforecastingservice.domain.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain event fired when a new sales forecast is created.
 */
public class ForecastCreatedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String segmentName;
    private final String segmentType;
    private final Instant occurredAt;

    public ForecastCreatedEvent(String aggregateId, String tenantId, String segmentName,
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

    public String getForecastName() {
        return segmentName;
    }

    public String getForecastType() {
        return segmentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForecastCreatedEvent that = (ForecastCreatedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "ForecastCreatedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", segmentName='" + segmentName + '\'' +
                ", segmentType='" + segmentType + '\'' +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
