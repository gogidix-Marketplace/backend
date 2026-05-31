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
 * Domain Entity representing a driver tracking session.
 * Tracks the active tracking period for a driver during order fulfillment.
 */
@Document(collection = "driver_tracking_sessions")
@CompoundIndex(name = "idx_driver_status", def = "{'driverId': 1, 'status': 1}")
@CompoundIndex(name = "idx_tenant_status", def = "{'tenantId': 1, 'status': 1}")
public class DriverTrackingSession {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("session_id")
    private String sessionId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Field("order_ids")
    private List<String> orderIds;

    @Field("status")
    private SessionStatus status;

    @Field("start_time")
    private Instant startTime;

    @Field("end_time")
    private Instant endTime;

    @Field("last_location")
    private GpsLocation.GeoPoint lastLocation;

    @Field("last_location_time")
    private Instant lastLocationTime;

    @Field("total_distance_meters")
    private Double totalDistanceMeters;

    @Field("location_update_count")
    private Integer locationUpdateCount;

    @Field("end_reason")
    private String endReason;

    @Field("metadata")
    private SessionMetadata metadata;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    /**
     * Default constructor for persistence.
     */
    protected DriverTrackingSession() {
    }

    /**
     * Create a new tracking session.
     *
     * @param tenantId the tenant identifier
     * @param sessionId the session identifier
     * @param driverId  the driver identifier
     */
    public DriverTrackingSession(String tenantId, String sessionId, String driverId) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.sessionId = Objects.requireNonNull(sessionId, "sessionId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.status = SessionStatus.ACTIVE;
        this.orderIds = new ArrayList<>();
        this.startTime = Instant.now();
        this.totalDistanceMeters = 0.0;
        this.locationUpdateCount = 0;
        this.metadata = new SessionMetadata();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Domain Logic Methods

    /**
     * Start the tracking session.
     */
    public void start() {
        if (this.status != SessionStatus.PENDING) {
            throw new IllegalStateException("Cannot start session with status: " + this.status);
        }
        this.status = SessionStatus.ACTIVE;
        this.startTime = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * End the tracking session.
     *
     * @param reason the reason for ending
     */
    public void end(String reason) {
        if (this.status != SessionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot end session with status: " + this.status);
        }
        this.status = SessionStatus.COMPLETED;
        this.endTime = Instant.now();
        this.endReason = reason;
        this.updatedAt = Instant.now();
    }

    /**
     * Cancel the tracking session.
     *
     * @param reason the reason for cancellation
     */
    public void cancel(String reason) {
        if (this.status == SessionStatus.COMPLETED || this.status == SessionStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel session with status: " + this.status);
        }
        this.status = SessionStatus.CANCELLED;
        this.endTime = Instant.now();
        this.endReason = reason;
        this.updatedAt = Instant.now();
    }

    /**
     * Update the last known location.
     *
     * @param location the new location
     * @param distance distance traveled since last update
     */
    public void updateLocation(GpsLocation.GeoPoint location, double distance) {
        if (location == null) {
            throw new IllegalArgumentException("location cannot be null");
        }

        this.lastLocation = location;
        this.lastLocationTime = Instant.now();
        this.totalDistanceMeters = (this.totalDistanceMeters != null ? this.totalDistanceMeters : 0) + distance;
        this.locationUpdateCount = (this.locationUpdateCount != null ? this.locationUpdateCount : 0) + 1;
        this.updatedAt = Instant.now();
    }

    /**
     * Add an order to the session.
     *
     * @param orderId the order ID
     */
    public void addOrder(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId cannot be blank");
        }
        if (this.orderIds == null) {
            this.orderIds = new ArrayList<>();
        }
        if (!this.orderIds.contains(orderId)) {
            this.orderIds.add(orderId);
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Remove an order from the session.
     *
     * @param orderId the order ID
     */
    public void removeOrder(String orderId) {
        if (this.orderIds != null) {
            this.orderIds.remove(orderId);
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Check if the session is active.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return this.status == SessionStatus.ACTIVE;
    }

    /**
     * Check if the session is completed.
     *
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return this.status == SessionStatus.COMPLETED;
    }

    /**
     * Get the session duration in seconds.
     *
     * @return duration in seconds
     */
    public long getDurationSeconds() {
        Instant end = this.endTime != null ? this.endTime : Instant.now();
        Instant start = this.startTime != null ? this.startTime : end;
        return java.time.Duration.between(start, end).getSeconds();
    }

    /**
     * Check if the location data is stale (older than specified minutes).
     *
     * @param minutes the threshold in minutes
     * @return true if stale, false otherwise
     */
    public boolean isLocationStale(int minutes) {
        if (lastLocationTime == null) {
            return true;
        }
        return lastLocationTime.isBefore(Instant.now().minusSeconds(minutes * 60L));
    }

    /**
     * Update metadata.
     *
     * @param key   the metadata key
     * @param value the metadata value
     */
    public void updateMetadata(String key, String value) {
        if (this.metadata == null) {
            this.metadata = new SessionMetadata();
        }
        this.metadata.put(key, value);
        this.updatedAt = Instant.now();
    }

    /**
     * Validate the session.
     *
     * @throws IllegalArgumentException if validation fails
     */
    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("sessionId is required");
        }
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("driverId is required");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getDriverId() {
        return driverId;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public GpsLocation.GeoPoint getLastLocation() {
        return lastLocation;
    }

    public Instant getLastLocationTime() {
        return lastLocationTime;
    }

    public Double getTotalDistanceMeters() {
        return totalDistanceMeters;
    }

    public Integer getLocationUpdateCount() {
        return locationUpdateCount;
    }

    public String getEndReason() {
        return endReason;
    }

    public SessionMetadata getMetadata() {
        return metadata;
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

    protected void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    protected void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    protected void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }

    protected void setStatus(SessionStatus status) {
        this.status = status;
    }

    protected void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    protected void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    protected void setLastLocation(GpsLocation.GeoPoint lastLocation) {
        this.lastLocation = lastLocation;
    }

    protected void setLastLocationTime(Instant lastLocationTime) {
        this.lastLocationTime = lastLocationTime;
    }

    protected void setTotalDistanceMeters(Double totalDistanceMeters) {
        this.totalDistanceMeters = totalDistanceMeters;
    }

    protected void setLocationUpdateCount(Integer locationUpdateCount) {
        this.locationUpdateCount = locationUpdateCount;
    }

    protected void setEndReason(String endReason) {
        this.endReason = endReason;
    }

    protected void setMetadata(SessionMetadata metadata) {
        this.metadata = metadata;
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
        DriverTrackingSession that = (DriverTrackingSession) o;
        return Objects.equals(sessionId, that.sessionId) &&
                Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, tenantId);
    }

    @Override
    public String toString() {
        return "DriverTrackingSession{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", locationUpdateCount=" + locationUpdateCount +
                '}';
    }

    /**
     * Session status enum.
     */
    public enum SessionStatus {
        PENDING,
        ACTIVE,
        COMPLETED,
        CANCELLED,
        TIMEOUT
    }

    /**
     * Session metadata value object.
     */
    public static class SessionMetadata {
        private String deviceType;
        private String appVersion;
        private String startReason;
        private String notes;

        public SessionMetadata() {
        }

        public void put(String key, String value) {
            switch (key) {
                case "deviceType" -> this.deviceType = value;
                case "appVersion" -> this.appVersion = value;
                case "startReason" -> this.startReason = value;
                case "notes" -> this.notes = value;
            }
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getStartReason() {
            return startReason;
        }

        public void setStartReason(String startReason) {
            this.startReason = startReason;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
}
