package com.gogidix.aiservices.aiproductrecommendationservice.domain.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain event fired when customers are added to a segment.
 */
public class ProductsAddedToRecommendationEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String segmentName;
    private final Integer customersAdded;
    private final Long totalProductCount;
    private final Instant occurredAt;

    public ProductsAddedToRecommendationEvent(String aggregateId, String tenantId, String segmentName,
                                        Integer customersAdded, Long totalProductCount, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.segmentName = Objects.requireNonNull(segmentName, "segmentName cannot be null");
        this.customersAdded = Objects.requireNonNull(customersAdded, "customersAdded cannot be null");
        this.totalProductCount = Objects.requireNonNull(totalProductCount, "totalProductCount cannot be null");
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

    public Integer getProductsAdded() {
        return customersAdded;
    }

    public Long getTotalProductCount() {
        return totalProductCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductsAddedToRecommendationEvent that = (ProductsAddedToRecommendationEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "ProductsAddedToRecommendationEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", segmentName='" + segmentName + '\'' +
                ", customersAdded=" + customersAdded +
                ", totalProductCount=" + totalProductCount +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
