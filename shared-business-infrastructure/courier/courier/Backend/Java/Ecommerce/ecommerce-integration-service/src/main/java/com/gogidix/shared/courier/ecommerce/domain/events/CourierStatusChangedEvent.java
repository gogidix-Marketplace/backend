package com.gogidix.shared.courier.ecommerce.domain.events;

import java.time.Instant;
import java.util.UUID;

public class CourierStatusChangedEvent implements EcommerceDomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String orderId;
    private final String subOrderId;
    private final String courierId;
    private final String trackingId;
    private final String previousStatus;
    private final String newStatus;
    private final Instant changedAt;
    private final String reason;
    private final Instant occurredAt;

    public CourierStatusChangedEvent(String aggregateId, String orderId, String subOrderId,
                                     String courierId, String trackingId,
                                     String previousStatus, String newStatus, String reason) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.orderId = orderId;
        this.subOrderId = subOrderId;
        this.courierId = courierId;
        this.trackingId = trackingId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedAt = Instant.now();
        this.reason = reason;
        this.occurredAt = Instant.now();
    }

    @Override public String getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public String getOrderId() { return orderId; }
    @Override public Instant getOccurredAt() { return occurredAt; }
    public String getSubOrderId() { return subOrderId; }
    public String getCourierId() { return courierId; }
    public String getTrackingId() { return trackingId; }
    public String getPreviousStatus() { return previousStatus; }
    public String getNewStatus() { return newStatus; }
    public Instant getChangedAt() { return changedAt; }
    public String getReason() { return reason; }
}
