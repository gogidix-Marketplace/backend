package com.gogidix.courier.locationservice.domain.entity;

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
 * Domain Entity representing aggregated location data.
 * Contains aggregated and summarized location information.
 */
@Document(collection = "location_aggregates")
@CompoundIndex(name = "idx_agg_driver_period", def = "{'tenantId': 1, 'driverId': 1, 'periodStart': -1}")
public class LocationAggregate {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Field("period_start")
    private Instant periodStart;

    @Field("period_end")
    private Instant periodEnd;

    @Field("aggregate_type")
    private AggregateType aggregateType;

    @Field("total_distance")
    private Double totalDistance; // in meters

    @Field("total_duration")
    private Long totalDuration; // in seconds

    @Field("average_speed")
    private Double averageSpeed; // in km/h

    @Field("max_speed")
    private Double maxSpeed; // in km/h

    @Field("waypoints")
    private List<Waypoint> waypoints;

    @Field("start_location")
    private LocationEvent.GeoLocation startLocation;

    @Field("end_location")
    private LocationEvent.GeoLocation endLocation;

    @Field("idle_time")
    private Long idleTime; // in seconds

    @Field("battery_drained")
    private Integer batteryDrained;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    /**
     * Default constructor for persistence.
     */
    protected LocationAggregate() {
    }

    /**
     * Create a new LocationAggregate.
     *
     * @param tenantId    the tenant identifier
     * @param driverId    the driver identifier
     * @param periodStart the period start
     * @param periodEnd   the period end
     * @param aggregateType the aggregate type
     */
    public LocationAggregate(String tenantId, String driverId, Instant periodStart,
                            Instant periodEnd, AggregateType aggregateType) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.periodStart = Objects.requireNonNull(periodStart, "periodStart is required");
        this.periodEnd = Objects.requireNonNull(periodEnd, "periodEnd is required");
        this.aggregateType = Objects.requireNonNull(aggregateType, "aggregateType is required");
        this.totalDistance = 0.0;
        this.totalDuration = 0L;
        this.averageSpeed = 0.0;
        this.maxSpeed = 0.0;
        this.waypoints = new ArrayList<>();
        this.idleTime = 0L;
        this.batteryDrained = 0;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Domain Logic Methods

    /**
     * Add a waypoint to the aggregate.
     *
     * @param location the location
     * @param timestamp the timestamp
     */
    public void addWaypoint(LocationEvent.GeoLocation location, Instant timestamp) {
        Waypoint waypoint = new Waypoint(location, timestamp);
        this.waypoints.add(waypoint);

        if (waypoints.size() == 1) {
            this.startLocation = location;
        }
        this.endLocation = location;
        this.updatedAt = Instant.now();
    }

    /**
     * Update distance and duration.
     *
     * @param distance the additional distance
     * @param duration the additional duration
     */
    public void updateMetrics(Double distance, Long duration) {
        this.totalDistance = (this.totalDistance != null ? this.totalDistance : 0) + distance;
        this.totalDuration = (this.totalDuration != null ? this.totalDuration : 0) + duration;

        // Recalculate average speed
        if (totalDuration > 0) {
            // Convert m/s to km/h
            this.averageSpeed = (totalDistance / totalDuration) * 3.6;
        }
        this.updatedAt = Instant.now();
    }

    /**
     * Update max speed.
     *
     * @param speed the speed to check
     */
    public void updateMaxSpeed(Double speed) {
        if (maxSpeed == null || speed > maxSpeed) {
            this.maxSpeed = speed;
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Add idle time.
     *
     * @param idleSeconds the idle time in seconds
     */
    public void addIdleTime(Long idleSeconds) {
        this.idleTime = (this.idleTime != null ? this.idleTime : 0) + idleSeconds;
        this.updatedAt = Instant.now();
    }

    /**
     * Set battery drain.
     *
     * @param startBattery start battery level
     * @param endBattery   end battery level
     */
    public void calculateBatteryDrain(Integer startBattery, Integer endBattery) {
        if (startBattery != null && endBattery != null) {
            this.batteryDrained = startBattery - endBattery;
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Get average speed in km/h.
     *
     * @return average speed
     */
    public Double getAverageSpeedKmh() {
        return averageSpeed;
    }

    /**
     * Get total distance in km.
     *
     * @return total distance
     */
    public Double getTotalDistanceKm() {
        return totalDistance != null ? totalDistance / 1000 : 0.0;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getDriverId() {
        return driverId;
    }

    public Instant getPeriodStart() {
        return periodStart;
    }

    public Instant getPeriodEnd() {
        return periodEnd;
    }

    public AggregateType getAggregateType() {
        return aggregateType;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public Long getTotalDuration() {
        return totalDuration;
    }

    public Double getAverageSpeed() {
        return averageSpeed;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public LocationEvent.GeoLocation getStartLocation() {
        return startLocation;
    }

    public LocationEvent.GeoLocation getEndLocation() {
        return endLocation;
    }

    public Long getIdleTime() {
        return idleTime;
    }

    public Integer getBatteryDrained() {
        return batteryDrained;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    protected void setPeriodStart(Instant periodStart) {
        this.periodStart = periodStart;
    }

    protected void setPeriodEnd(Instant periodEnd) {
        this.periodEnd = periodEnd;
    }

    protected void setAggregateType(AggregateType aggregateType) {
        this.aggregateType = aggregateType;
    }

    protected void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    protected void setTotalDuration(Long totalDuration) {
        this.totalDuration = totalDuration;
    }

    protected void setAverageSpeed(Double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    protected void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    protected void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    protected void setStartLocation(LocationEvent.GeoLocation startLocation) {
        this.startLocation = startLocation;
    }

    protected void setEndLocation(LocationEvent.GeoLocation endLocation) {
        this.endLocation = endLocation;
    }

    protected void setIdleTime(Long idleTime) {
        this.idleTime = idleTime;
    }

    protected void setBatteryDrained(Integer batteryDrained) {
        this.batteryDrained = batteryDrained;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Aggregate type enum.
     */
    public enum AggregateType {
        HOURLY,
        DAILY,
        TRIP,
        SHIFT,
        CUSTOM
    }

    /**
     * Waypoint value object.
     */
    public static class Waypoint {
        @Field("location")
        private LocationEvent.GeoLocation location;

        @Field("timestamp")
        private Instant timestamp;

        @Field("sequence")
        private Integer sequence;

        public Waypoint() {
        }

        public Waypoint(LocationEvent.GeoLocation location, Instant timestamp) {
            this.location = location;
            this.timestamp = timestamp;
        }

        public LocationEvent.GeoLocation getLocation() {
            return location;
        }

        public void setLocation(LocationEvent.GeoLocation location) {
            this.location = location;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
        }

        public Integer getSequence() {
            return sequence;
        }

        public void setSequence(Integer sequence) {
            this.sequence = sequence;
        }
    }
}
