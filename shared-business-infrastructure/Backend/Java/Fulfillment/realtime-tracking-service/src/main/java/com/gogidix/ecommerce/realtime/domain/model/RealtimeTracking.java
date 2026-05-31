package com.gogidix.ecommerce.realtime.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "realtime_tracking")
public class RealtimeTracking {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed(unique = true)
    private String trackingId;

    @Indexed
    private String orderId;

    private Location currentLocation;
    private Location destination;

    private Double speed;
    private Double heading;
    private Double altitude;

    private String deviceId;
    private String vehicleId;

    private BatteryStatus batteryStatus;
    private SignalStrength signalStrength;

    @Indexed
    private Boolean isActive;

    private Instant lastUpdateAt;
    private Instant createdAt;
    private Instant updatedAt;

    public enum BatteryStatus {
        FULL, MEDIUM, LOW, CRITICAL
    }

    public enum SignalStrength {
        EXCELLENT, GOOD, FAIR, POOR, NO_SIGNAL
    }

    @Data
    public static class Location {
        private Double latitude;
        private Double longitude;
        private String address;
        private String city;
        private String country;
        private Instant timestamp;
    }
}
