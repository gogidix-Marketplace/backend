package com.gogidix.aiservices.intelligenceanalysisservice.domain.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain event fired when customers are added to a segment.
 */
public class IntelligenceReportsAddedToAnalysisEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String segmentName;
    private final Integer customersAdded;
    private final Long totalIntelligenceReportCount;
    private final Instant occurredAt;

    public IntelligenceReportsAddedToAnalysisEvent(String aggregateId, String tenantId, String segmentName,
                                        Integer customersAdded, Long totalIntelligenceReportCount, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.segmentName = Objects.requireNonNull(segmentName, "segmentName cannot be null");
        this.customersAdded = Objects.requireNonNull(customersAdded, "customersAdded cannot be null");
        this.totalIntelligenceReportCount = Objects.requireNonNull(totalIntelligenceReportCount, "totalIntelligenceReportCount cannot be null");
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

    public String getAnalysisName() {
        return segmentName;
    }

    public Integer getIntelligenceReportsAdded() {
        return customersAdded;
    }

    public Long getTotalIntelligenceReportCount() {
        return totalIntelligenceReportCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntelligenceReportsAddedToAnalysisEvent that = (IntelligenceReportsAddedToAnalysisEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "IntelligenceReportsAddedToAnalysisEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", segmentName='" + segmentName + '\'' +
                ", customersAdded=" + customersAdded +
                ", totalIntelligenceReportCount=" + totalIntelligenceReportCount +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
