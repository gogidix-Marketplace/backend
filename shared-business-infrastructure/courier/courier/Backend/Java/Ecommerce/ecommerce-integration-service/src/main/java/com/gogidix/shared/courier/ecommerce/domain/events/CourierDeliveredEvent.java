package com.gogidix.shared.courier.ecommerce.domain.events;

import java.time.Instant;
import java.util.UUID;

public class CourierDeliveredEvent implements EcommerceDomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String orderId;
    private final String subOrderId;
    private final String courierId;
    private final String trackingId;
    private final Instant deliveredAt;
    private final String proofOfDelivery;
    private final String recipientName;
    private final Instant occurredAt;

    public CourierDeliveredEvent(String aggregateId, String orderId, String subOrderId,
                                 String courierId, String trackingId,
                                 String proofOfDelivery, String recipientName) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.orderId = orderId;
        this.subOrderId = subOrderId;
        this.courierId = courierId;
        this.trackingId = trackingId;
        this.deliveredAt = Instant.now();
        this.proofOfDelivery = proofOfDelivery;
        this.recipientName = recipientName;
        this.occurredAt = Instant.now();
    }

    @Override public String getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public String getOrderId() { return orderId; }
    @Override public Instant getOccurredAt() { return occurredAt; }
    public String getSubOrderId() { return subOrderId; }
    public String getCourierId() { return courierId; }
    public String getTrackingId() { return trackingId; }
    public Instant getDeliveredAt() { return deliveredAt; }
    public String getProofOfDelivery() { return proofOfDelivery; }
    public String getRecipientName() { return recipientName; }
}
