package com.gogidix.courier.publictrackingservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

@Document(collection = "tracking_events")
public class TrackingEvent {

    @Id
    private String id;

    @Indexed
    @Field("tracking_number")
    private String trackingNumber;

    @Field("event_type")
    private EventType eventType;

    @Field("event_description")
    private String eventDescription;

    @Field("location")
    private String location;

    @Field("latitude")
    private Double latitude;

    @Field("longitude")
    private Double longitude;

    @Field("event_time")
    private Instant eventTime;

    @Field("created_at")
    private Instant createdAt;

    protected TrackingEvent() {
    }

    public TrackingEvent(String trackingNumber, EventType eventType, String eventDescription, String location) {
        this.id = java.util.UUID.randomUUID().toString();
        this.trackingNumber = Objects.requireNonNull(trackingNumber);
        this.eventType = Objects.requireNonNull(eventType);
        this.eventDescription = eventDescription;
        this.location = location;
        this.eventTime = Instant.now();
        this.createdAt = Instant.now();
    }

    public enum EventType {
        ORDER_PLACED,
        ORDER_CONFIRMED,
        PICKUP_SCHEDULED,
        OUT_FOR_PICKUP,
        PICKED_UP,
        IN_TRANSIT,
        OUT_FOR_DELIVERY,
        DELIVERED,
        DELIVERY_ATTEMPTED,
        EXCEPTION,
        CANCELLED,
        RETURNED
    }

    // Getters
    public String getId() { return id; }
    public String getTrackingNumber() { return trackingNumber; }
    public EventType getEventType() { return eventType; }
    public String getEventDescription() { return eventDescription; }
    public String getLocation() { return location; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Instant getEventTime() { return eventTime; }
    public Instant getCreatedAt() { return createdAt; }

    // Setters
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    protected void setId(String id) { this.id = id; }
    protected void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    protected void setEventType(EventType eventType) { this.eventType = eventType; }
    protected void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }
    protected void setLocation(String location) { this.location = location; }
    protected void setEventTime(Instant eventTime) { this.eventTime = eventTime; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
