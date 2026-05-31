package com.gogidix.shared.courier.ecommerce.domain.events;

import java.time.Instant;
import java.util.UUID;

import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryLeg;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.VehicleType;

public class CourierAssignedEvent implements EcommerceDomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String orderId;
    private final String subOrderId;
    private final String courierId;
    private final String courierName;
    private final String courierPhone;
    private final VehicleType vehicleType;
    private final String trackingId;
    private final Instant estimatedPickupTime;
    private final Instant estimatedDeliveryTime;
    private final DeliveryType deliveryType;
    private final DeliveryLeg leg;
    private final Instant assignedAt;
    private final Instant occurredAt;

    public CourierAssignedEvent(String aggregateId, String orderId, String subOrderId,
                                String courierId, String courierName, String courierPhone,
                                VehicleType vehicleType, String trackingId,
                                Instant estimatedPickupTime, Instant estimatedDeliveryTime,
                                DeliveryType deliveryType, DeliveryLeg leg) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.orderId = orderId;
        this.subOrderId = subOrderId;
        this.courierId = courierId;
        this.courierName = courierName;
        this.courierPhone = courierPhone;
        this.vehicleType = vehicleType;
        this.trackingId = trackingId;
        this.estimatedPickupTime = estimatedPickupTime;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.deliveryType = deliveryType;
        this.leg = leg;
        this.assignedAt = Instant.now();
        this.occurredAt = Instant.now();
    }

    @Override public String getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public String getOrderId() { return orderId; }
    @Override public Instant getOccurredAt() { return occurredAt; }
    public String getSubOrderId() { return subOrderId; }
    public String getCourierId() { return courierId; }
    public String getCourierName() { return courierName; }
    public String getCourierPhone() { return courierPhone; }
    public VehicleType getVehicleType() { return vehicleType; }
    public String getTrackingId() { return trackingId; }
    public Instant getEstimatedPickupTime() { return estimatedPickupTime; }
    public Instant getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
    public DeliveryType getDeliveryType() { return deliveryType; }
    public DeliveryLeg getLeg() { return leg; }
    public Instant getAssignedAt() { return assignedAt; }
}
