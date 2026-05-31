package com.gogidix.courier.gpstrackingservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing a GPS location update.
 * Stores real-time location data for drivers.
 */
@Document(collection = "gps_locations")
@CompoundIndex(name = "idx_driver_timestamp", def = "{'driverId': 1, 'timestamp': -1}")
@CompoundIndex(name = "idx_location_geospatial", def = "{'location': '2dsphere'}")
public class GpsLocation {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Field("order_id")
    private String orderId;

    @Field("location")
    private GeoPoint location;

    @Field("altitude")
    private Double altitude;

    @Field("accuracy")
    private Double accuracy;

    @Field("speed")
    private Double speed;

    @Field("heading")
    private Double heading;

    @Field("timestamp")
    private Instant timestamp;

    @Field("battery_level")
    private Integer batteryLevel;

    @Field("location_source")
    private LocationSource locationSource;

    @Field("created_at")
    private Instant createdAt;

    /**
     * Default constructor for persistence.
     */
    protected GpsLocation() {
    }

    /**
     * Create a new GPS location.
     *
     * @param tenantId        the tenant identifier
     * @param driverId        the driver identifier
     * @param orderId         the order identifier (optional)
     * @param latitude        the latitude
     * @param longitude       the longitude
     * @param altitude        the altitude in meters
     * @param accuracy        the accuracy in meters
     * @param speed           the speed in m/s
     * @param heading         the heading in degrees
     * @param locationSource  the location source
     */
    public GpsLocation(
            String tenantId,
            String driverId,
            String orderId,
            double latitude,
            double longitude,
            Double altitude,
            Double accuracy,
            Double speed,
            Double heading,
            LocationSource locationSource) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.orderId = orderId;
        this.location = new GeoPoint(latitude, longitude);
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.speed = speed;
        this.heading = heading;
        this.timestamp = Instant.now();
        this.locationSource = locationSource != null ? locationSource : LocationSource.GPS;
        this.createdAt = Instant.now();
    }

    // Domain Logic Methods

    /**
     * Check if the location is valid.
     *
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        if (location == null) {
            return false;
        }
        return location.getLatitude() >= -90 && location.getLatitude() <= 90 &&
                location.getLongitude() >= -180 && location.getLongitude() <= 180;
    }

    /**
     * Calculate distance to another location in meters using Haversine formula.
     *
     * @param other the other location
     * @return distance in meters
     */
    public double distanceTo(GpsLocation other) {
        if (other == null || other.location == null) {
            throw new IllegalArgumentException("Other location cannot be null");
        }

        final int EARTH_RADIUS = 6371000; // meters

        double lat1 = Math.toRadians(this.location.getLatitude());
        double lat2 = Math.toRadians(other.location.getLatitude());
        double deltaLat = Math.toRadians(other.location.getLatitude() - this.location.getLatitude());
        double deltaLon = Math.toRadians(other.location.getLongitude() - this.location.getLongitude());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * Check if this location is recent (within specified seconds).
     *
     * @param seconds the threshold in seconds
     * @return true if recent, false otherwise
     */
    public boolean isRecent(int seconds) {
        if (timestamp == null) {
            return false;
        }
        return timestamp.isAfter(Instant.now().minusSeconds(seconds));
    }

    /**
     * Check if the driver is moving.
     *
     * @return true if speed > 0.5 m/s, false otherwise
     */
    public boolean isMoving() {
        return speed != null && speed > 0.5;
    }

    /**
     * Validate the location data.
     *
     * @throws IllegalArgumentException if validation fails
     */
    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("driverId is required");
        }
        if (location == null) {
            throw new IllegalArgumentException("location is required");
        }
        if (!isValid()) {
            throw new IllegalArgumentException("Invalid GPS coordinates");
        }
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

    public String getOrderId() {
        return orderId;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public double getLatitude() {
        return location != null ? location.getLatitude() : 0;
    }

    public double getLongitude() {
        return location != null ? location.getLongitude() : 0;
    }

    public Double getAltitude() {
        return altitude;
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public LocationSource getLocationSource() {
        return locationSource;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // Setters for persistence/MongoDB mapping
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    protected void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    protected void setLocation(GeoPoint location) {
        this.location = location;
    }

    protected void setAltitude(Double altitude) {
        this.altitude = altitude;
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

    protected void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    protected void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    protected void setLocationSource(LocationSource locationSource) {
        this.locationSource = locationSource;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GpsLocation that = (GpsLocation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tenantId, that.tenantId) &&
                Objects.equals(driverId, that.driverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, driverId);
    }

    @Override
    public String toString() {
        return "GpsLocation{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", location=" + location +
                ", timestamp=" + timestamp +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * GeoPoint value object for GPS coordinates.
     */
    public static class GeoPoint {
        private final double latitude;
        private final double longitude;

        public GeoPoint(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }

    /**
     * Location source enum.
     */
    public enum LocationSource {
        GPS,
        NETWORK,
        PASSIVE,
        FUSED,
        MANUAL
    }
}
