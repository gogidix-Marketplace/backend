package com.gogidix.aiservices.aisalesforecastingservice.domain.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain event fired when customers are added to a segment.
 */
public class ForecastModelsAddedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String segmentName;
    private final Integer customersAdded;
    private final Long totalForecastModelCount;
    private final Instant occurredAt;

    public ForecastModelsAddedEvent(String aggregateId, String tenantId, String segmentName,
                                        Integer customersAdded, Long totalForecastModelCount, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.segmentName = Objects.requireNonNull(segmentName, "segmentName cannot be null");
        this.customersAdded = Objects.requireNonNull(customersAdded, "customersAdded cannot be null");
        this.totalForecastModelCount = Objects.requireNonNull(totalForecastModelCount, "totalForecastModelCount cannot be null");
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

    public Integer getForecastModelsAdded() {
        return customersAdded;
    }

    public Long getTotalForecastModelCount() {
        return totalForecastModelCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForecastModelsAddedEvent that = (ForecastModelsAddedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "ForecastModelsAddedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", segmentName='" + segmentName + '\'' +
                ", customersAdded=" + customersAdded +
                ", totalForecastModelCount=" + totalForecastModelCount +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
