package com.gogidix.courier.gpstrackingservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Domain Entity representing historical location data for a driver.
 * Stores aggregated location history for route tracking.
 */
@Document(collection = "location_history")
@CompoundIndex(name = "idx_driver_date", def = "{'driverId': 1, 'date': -1}")
public class LocationHistory {

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

    @Indexed
    @Field("date")
    private String date; // YYYY-MM-DD format

    @Field("locations")
    private List<LocationPoint> locations;

    @Field("total_distance_meters")
    private Double totalDistanceMeters;

    @Field("duration_seconds")
    private Long durationSeconds;

    @Field("start_time")
    private Instant startTime;

    @Field("end_time")
    private Instant endTime;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    /**
     * Default constructor for persistence.
     */
    protected LocationHistory() {
    }

    /**
     * Create a new location history.
     *
     * @param tenantId the tenant identifier
     * @param driverId the driver identifier
     * @param orderId  the order identifier (optional)
     */
    public LocationHistory(String tenantId, String driverId, String orderId) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.orderId = orderId;
        this.date = java.time.LocalDate.now().toString();
        this.locations = new ArrayList<>();
        this.totalDistanceMeters = 0.0;
        this.durationSeconds = 0L;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Domain Logic Methods

    /**
     * Add a location point to the history.
     *
     * @param location the GPS location to add
     */
    public void addLocation(GpsLocation location) {
        if (location == null) {
            throw new IllegalArgumentException("location cannot be null");
        }

        LocationPoint point = new LocationPoint(
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude(),
                location.getSpeed(),
                location.getHeading(),
                location.getTimestamp()
        );

        // Calculate distance if there's a previous point
        if (!locations.isEmpty()) {
            LocationPoint lastPoint = locations.get(locations.size() - 1);
            double distance = calculateDistance(
                    lastPoint.getLatitude(), lastPoint.getLongitude(),
                    point.getLatitude(), point.getLongitude()
            );
            this.totalDistanceMeters += distance;
        } else {
            this.startTime = location.getTimestamp();
        }

        this.locations.add(point);
        this.endTime = location.getTimestamp();
        this.updatedAt = Instant.now();

        // Update duration
        if (startTime != null && endTime != null) {
            this.durationSeconds = java.time.Duration.between(startTime, endTime).getSeconds();
        }
    }

    /**
     * Get the number of location points.
     *
     * @return the count
     */
    public int getLocationCount() {
        return locations != null ? locations.size() : 0;
    }

    /**
     * Calculate the average speed.
     *
     * @return average speed in m/s, or 0 if no data
     */
    public double getAverageSpeed() {
        if (locations == null || locations.isEmpty() || durationSeconds == null || durationSeconds == 0) {
            return 0.0;
        }
        return totalDistanceMeters / durationSeconds;
    }

    /**
     * Calculate distance between two points using Haversine formula.
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371000; // meters

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * Validate the location history.
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
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("date is required");
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

    public String getDate() {
        return date;
    }

    public List<LocationPoint> getLocations() {
        return locations;
    }

    public Double getTotalDistanceMeters() {
        return totalDistanceMeters;
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
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

    protected void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    protected void setDate(String date) {
        this.date = date;
    }

    protected void setLocations(List<LocationPoint> locations) {
        this.locations = locations;
    }

    protected void setTotalDistanceMeters(Double totalDistanceMeters) {
        this.totalDistanceMeters = totalDistanceMeters;
    }

    protected void setDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    protected void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    protected void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationHistory that = (LocationHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LocationHistory{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", date='" + date + '\'' +
                ", locationCount=" + getLocationCount() +
                ", totalDistanceMeters=" + totalDistanceMeters +
                '}';
    }

    /**
     * Location point value object.
     */
    public static class LocationPoint {
        private final double latitude;
        private final double longitude;
        private final Double altitude;
        private final Double speed;
        private final Double heading;
        private final Instant timestamp;

        public LocationPoint(double latitude, double longitude, Double altitude,
                           Double speed, Double heading, Instant timestamp) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
            this.speed = speed;
            this.heading = heading;
            this.timestamp = timestamp;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public Double getAltitude() {
            return altitude;
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
    }
}
