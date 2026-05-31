package com.gogidix.courier.publictrackingservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "shipment_tracking")
@CompoundIndex(name = "idx_tenant_tracking", def = "{'tenantId': 1, 'trackingNumber': 1}")
public class ShipmentTracking {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("tracking_number")
    private String trackingNumber;

    @Indexed
    @Field("order_id")
    private String orderId;

    @Field("customer_id")
    private String customerId;

    @Field("status")
    private TrackingStatus status;

    @Field("origin_address")
    private String originAddress;

    @Field("destination_address")
    private String destinationAddress;

    @Field("current_location")
    private String currentLocation;

    @Field("estimated_delivery")
    private Instant estimatedDelivery;

    @Field("actual_delivery")
    private Instant actualDelivery;

    @Field("events")
    private List<TrackingEvent> events;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    protected ShipmentTracking() {
    }

    public ShipmentTracking(String tenantId, String trackingNumber, String orderId) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId);
        this.trackingNumber = Objects.requireNonNull(trackingNumber);
        this.orderId = orderId;
        this.status = TrackingStatus.ORDER_PLACED;
        this.events = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void addEvent(TrackingEvent event) {
        this.events.add(event);
        this.currentLocation = event.getLocation();
        this.status = mapEventToStatus(event.getEventType());
        if (event.getEventType() == TrackingEvent.EventType.DELIVERED) {
            this.actualDelivery = event.getEventTime();
        }
        this.updatedAt = Instant.now();
    }

    private TrackingStatus mapEventToStatus(TrackingEvent.EventType eventType) {
        switch (eventType) {
            case DELIVERED:
                return TrackingStatus.DELIVERED;
            case OUT_FOR_DELIVERY:
                return TrackingStatus.OUT_FOR_DELIVERY;
            case IN_TRANSIT:
                return TrackingStatus.IN_TRANSIT;
            case PICKED_UP:
                return TrackingStatus.IN_TRANSIT;
            case OUT_FOR_PICKUP:
                return TrackingStatus.PICKUP_SCHEDULED;
            case EXCEPTION:
                return TrackingStatus.EXCEPTION;
            case CANCELLED:
                return TrackingStatus.CANCELLED;
            default:
                return this.status;
        }
    }

    public enum TrackingStatus {
        ORDER_PLACED,
        ORDER_CONFIRMED,
        PICKUP_SCHEDULED,
        IN_TRANSIT,
        OUT_FOR_DELIVERY,
        DELIVERED,
        EXCEPTION,
        CANCELLED,
        RETURNED
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getTrackingNumber() { return trackingNumber; }
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public TrackingStatus getStatus() { return status; }
    public String getOriginAddress() { return originAddress; }
    public String getDestinationAddress() { return destinationAddress; }
    public String getCurrentLocation() { return currentLocation; }
    public Instant getEstimatedDelivery() { return estimatedDelivery; }
    public Instant getActualDelivery() { return actualDelivery; }
    public List<TrackingEvent> getEvents() { return events; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setOriginAddress(String originAddress) { this.originAddress = originAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }
    public void setEstimatedDelivery(Instant estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    protected void setOrderId(String orderId) { this.orderId = orderId; }
    protected void setStatus(TrackingStatus status) { this.status = status; }
    protected void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
    protected void setActualDelivery(Instant actualDelivery) { this.actualDelivery = actualDelivery; }
    protected void setEvents(List<TrackingEvent> events) { this.events = events; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    protected void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
