package com.gogidix.ecommerce.tracking.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "order_tracking")
public class OrderTracking {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed(unique = true)
    private String trackingId;

    @Indexed
    private String orderId;

    private String orderNumber;

    private TrackingStatus status;
    private String currentLocation;
    private String estimatedDeliveryDate;

    private String carrier;
    private String carrierTrackingNumber;

    private ShipmentType shipmentType;
    private String origin;
    private String destination;

    private List<TrackingEvent> trackingEvents = new ArrayList<>();

    private Instant shippedAt;
    private Instant inTransitAt;
    private Instant deliveredAt;
    private Instant failedAt;

    private String notes;

    @Indexed
    private Boolean isActive;

    private Instant createdAt;
    private Instant updatedAt;

    public enum TrackingStatus {
        PENDING, LABEL_CREATED, SHIPPED, IN_TRANSIT, OUT_FOR_DELIVERY,
        DELIVERED, FAILED, RETURNED, CANCELLED
    }

    public enum ShipmentType {
        STANDARD, EXPRESS, PRIORITY, SAME_DAY, INTERNATIONAL
    }

    @Data
    public static class TrackingEvent {
        private String eventId;
        private TrackingStatus status;
        private String description;
        private String location;
        private Instant timestamp;
        private String source;
    }
}
