package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain event fired when customers are added to a segment.
 */
public class CustomersAddedToBasketEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String segmentName;
    private final Integer customersAdded;
    private final Long totalCustomerCount;
    private final Instant occurredAt;

    public CustomersAddedToBasketEvent(String aggregateId, String tenantId, String segmentName,
                                        Integer customersAdded, Long totalCustomerCount, Instant occurredAt) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.segmentName = Objects.requireNonNull(segmentName, "segmentName cannot be null");
        this.customersAdded = Objects.requireNonNull(customersAdded, "customersAdded cannot be null");
        this.totalCustomerCount = Objects.requireNonNull(totalCustomerCount, "totalCustomerCount cannot be null");
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

    public Integer getCustomersAdded() {
        return customersAdded;
    }

    public Long getTotalCustomerCount() {
        return totalCustomerCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomersAddedToBasketEvent that = (CustomersAddedToBasketEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "CustomersAddedToBasketEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", segmentName='" + segmentName + '\'' +
                ", customersAdded=" + customersAdded +
                ", totalCustomerCount=" + totalCustomerCount +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
