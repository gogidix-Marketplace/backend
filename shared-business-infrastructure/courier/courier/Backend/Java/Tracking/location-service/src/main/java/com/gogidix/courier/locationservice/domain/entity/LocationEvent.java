package com.gogidix.courier.locationservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing a Location Event.
 * Tracks individual location updates from drivers.
 */
@Document(collection = "location_events")
@CompoundIndex(name = "idx_location_driver_time", def = "{'tenantId': 1, 'driverId': 1, 'timestamp': -1}")
public class LocationEvent {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Field("tracking_id")
    private String trackingId;

    @Indexed
    @Field("timestamp")
    private Instant timestamp;

    @Field("location")
    private GeoLocation location;

    @Field("accuracy")
    private Double accuracy; // in meters

    @Field("speed")
    private Double speed; // in km/h

    @Field("heading")
    private Double heading; // in degrees

    @Field("altitude")
    private Double altitude; // in meters

    @Field("battery_level")
    private Integer batteryLevel; // 0-100

    @Field("location_source")
    private LocationSource locationSource;

    @Field("event_type")
    private EventType eventType;

    @Field("created_at")
    private Instant createdAt;

    /**
     * Default constructor for persistence.
     */
    protected LocationEvent() {
    }

    /**
     * Create a new LocationEvent.
     *
     * @param tenantId the tenant identifier
     * @param driverId the driver identifier
     * @param location the geo location
     */
    public LocationEvent(String tenantId, String driverId, GeoLocation location) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.location = Objects.requireNonNull(location, "location is required");
        this.timestamp = Instant.now();
        this.eventType = EventType.LOCATION_UPDATE;
        this.locationSource = LocationSource.GPS;
        this.createdAt = Instant.now();
    }

    // Domain Logic Methods

    /**
     * Update location.
     *
     * @param location the new location
     */
    public void updateLocation(GeoLocation location) {
        this.location = Objects.requireNonNull(location, "location is required");
        this.timestamp = Instant.now();
    }

    /**
     * Set movement data.
     *
     * @param speed   the speed in km/h
     * @param heading the heading in degrees
     */
    public void setMovementData(Double speed, Double heading) {
        this.speed = speed;
        this.heading = heading;
    }

    /**
     * Mark as geofence entry event.
     */
    public void markAsGeofenceEntry() {
        this.eventType = EventType.GEOFENCE_ENTRY;
    }

    /**
     * Mark as geofence exit event.
     */
    public void markAsGeofenceExit() {
        this.eventType = EventType.GEOFENCE_EXIT;
    }

    /**
     * Check if location is recent.
     *
     * @param thresholdSeconds the threshold in seconds
     * @return true if recent
     */
    public boolean isRecent(long thresholdSeconds) {
        return timestamp.isAfter(Instant.now().minusSeconds(thresholdSeconds));
    }

    /**
     * Calculate distance to another location.
     *
     * @param other the other location
     * @return distance in meters
     */
    public double distanceTo(GeoLocation other) {
        return calculateDistance(this.location, other);
    }

    private double calculateDistance(GeoLocation from, GeoLocation to) {
        final int R = 6371000; // Earth radius in meters

        double lat1 = Math.toRadians(from.latitude());
        double lat2 = Math.toRadians(to.latitude());
        double deltaLat = Math.toRadians(to.latitude() - from.latitude());
        double deltaLon = Math.toRadians(to.longitude() - from.longitude());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public Double getSpeed() {
        return speed;
    }

    public Double getHeading() {
        return heading;
    }

    public Double getAltitude() {
        return altitude;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public LocationSource getLocationSource() {
        return locationSource;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // Setters for persistence
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    protected void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    protected void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    protected void setLocation(GeoLocation location) {
        this.location = location;
    }

    protected void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    protected void setSpeed(Double speed) {
        this.speed = speed;
    }

    protected void setHeading(Double heading) {
        this.heading = heading;
    }

    protected void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    protected void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    protected void setLocationSource(LocationSource locationSource) {
        this.locationSource = locationSource;
    }

    protected void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Event type enum.
     */
    public enum EventType {
        LOCATION_UPDATE,
        GEOFENCE_ENTRY,
        GEOFENCE_EXIT,
        IDLE_DETECTED,
        MOVEMENT_RESUMED,
        OFFLINE,
        ONLINE
    }

    /**
     * Location source enum.
     */
    public enum LocationSource {
        GPS,
        NETWORK,
        PASSIVE,
        MANUAL,
        BEACON
    }

    /**
     * Geo location value object.
     */
    public static class GeoLocation {
        @Field("latitude")
        private Double latitude;

        @Field("longitude")
        private Double longitude;

        public GeoLocation() {
        }

        public GeoLocation(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Double latitude() {
            return latitude;
        }

        public Double longitude() {
            return longitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public boolean isValid() {
            return latitude != null && longitude != null &&
                   latitude >= -90 && latitude <= 90 &&
                   longitude >= -180 && longitude <= 180;
        }
    }
}
