package com.gogidix.courier.routingservice.domain.entity;

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
 * Domain Entity representing a delivery route.
 * A route consists of waypoints that need to be visited for order fulfillment.
 */
@Document(collection = "routes")
@CompoundIndex(name = "idx_driver_status", def = "{'driverId': 1, 'status': 1}")
@CompoundIndex(name = "idx_tenant_status", def = "{'tenantId': 1, 'status': 1}")
@CompoundIndex(name = "idx_tenant_dates", def = "{'tenantId': 1, 'startDate': -1, 'endDate': -1}")
public class Route {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("route_id")
    private String routeId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Field("order_ids")
    private List<String> orderIds;

    @Field("waypoints")
    private List<RouteWaypoint> waypoints;

    @Field("status")
    private RouteStatus status;

    @Field("start_location")
    private GeoPoint startLocation;

    @Field("end_location")
    private GeoPoint endLocation;

    @Field("estimated_distance_meters")
    private Double estimatedDistanceMeters;

    @Field("estimated_duration_seconds")
    private Integer estimatedDurationSeconds;

    @Field("actual_distance_meters")
    private Double actualDistanceMeters;

    @Field("actual_duration_seconds")
    private Integer actualDurationSeconds;

    @Field("priority")
    private RoutePriority priority;

    @Field("vehicle_type")
    private VehicleType vehicleType;

    @Field("start_time")
    private Instant startTime;

    @Field("end_time")
    private Instant endTime;

    @Field("start_date")
    private Instant startDate;

    @Field("end_date")
    private Instant endDate;

    @Field("notes")
    private String notes;

    @Field("metadata")
    private RouteMetadata metadata;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("completed_at")
    private Instant completedAt;

    /**
     * Default constructor for persistence.
     */
    protected Route() {
    }

    /**
     * Create a new route.
     *
     * @param tenantId      the tenant identifier
     * @param routeId       the route identifier
     * @param driverId      the driver identifier
     * @param startLocation the starting location
     * @param vehicleType   the type of vehicle
     */
    public Route(String tenantId, String routeId, String driverId, GeoPoint startLocation, VehicleType vehicleType) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.routeId = Objects.requireNonNull(routeId, "routeId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.startLocation = startLocation;
        this.vehicleType = vehicleType != null ? vehicleType : VehicleType.CAR;
        this.status = RouteStatus.PENDING;
        this.priority = RoutePriority.NORMAL;
        this.waypoints = new ArrayList<>();
        this.orderIds = new ArrayList<>();
        this.metadata = new RouteMetadata();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Domain Logic Methods

    /**
     * Add a waypoint to the route.
     *
     * @param waypoint the waypoint to add
     */
    public void addWaypoint(RouteWaypoint waypoint) {
        if (waypoint == null) {
            throw new IllegalArgumentException("waypoint cannot be null");
        }
        if (this.waypoints == null) {
            this.waypoints = new ArrayList<>();
        }
        waypoint.setSequenceNumber(this.waypoints.size());
        this.waypoints.add(waypoint);
        this.updatedAt = Instant.now();

        if (waypoint.getOrderId() != null && !this.orderIds.contains(waypoint.getOrderId())) {
            this.orderIds.add(waypoint.getOrderId());
        }
    }

    /**
     * Add multiple waypoints to the route.
     *
     * @param newWaypoints the waypoints to add
     */
    public void addWaypoints(List<RouteWaypoint> newWaypoints) {
        if (newWaypoints == null || newWaypoints.isEmpty()) {
            throw new IllegalArgumentException("waypoints cannot be null or empty");
        }
        if (this.waypoints == null) {
            this.waypoints = new ArrayList<>();
        }
        for (RouteWaypoint waypoint : newWaypoints) {
            waypoint.setSequenceNumber(this.waypoints.size());
            this.waypoints.add(waypoint);
            if (waypoint.getOrderId() != null && !this.orderIds.contains(waypoint.getOrderId())) {
                this.orderIds.add(waypoint.getOrderId());
            }
        }
        this.updatedAt = Instant.now();
    }

    /**
     * Optimize the route by reordering waypoints.
     *
     * @param optimizedWaypoints the reordered waypoints
     */
    public void optimizeRoute(List<RouteWaypoint> optimizedWaypoints) {
        if (optimizedWaypoints == null || optimizedWaypoints.isEmpty()) {
            throw new IllegalArgumentException("optimized waypoints cannot be null or empty");
        }

        // Update sequence numbers
        for (int i = 0; i < optimizedWaypoints.size(); i++) {
            optimizedWaypoints.get(i).setSequenceNumber(i);
        }

        this.waypoints = new ArrayList<>(optimizedWaypoints);
        this.status = RouteStatus.OPTIMIZED;
        this.updatedAt = Instant.now();

        // Update optimization metadata
        if (this.metadata == null) {
            this.metadata = new RouteMetadata();
        }
        this.metadata.setLastOptimizationTime(Instant.now());
        this.metadata.setOptimizationCount((this.metadata.getOptimizationCount() != null ? this.metadata.getOptimizationCount() : 0) + 1);
    }

    /**
     * Start the route.
     */
    public void start() {
        if (this.status != RouteStatus.PENDING && this.status != RouteStatus.OPTIMIZED) {
            throw new IllegalStateException("Cannot start route with status: " + this.status);
        }
        this.status = RouteStatus.IN_PROGRESS;
        this.startTime = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Complete the route.
     *
     * @param actualDistance   the actual distance traveled
     * @param actualDuration   the actual duration in seconds
     */
    public void complete(Double actualDistance, Integer actualDuration) {
        if (this.status != RouteStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot complete route with status: " + this.status);
        }
        this.status = RouteStatus.COMPLETED;
        this.endTime = Instant.now();
        this.completedAt = Instant.now();
        this.actualDistanceMeters = actualDistance;
        this.actualDurationSeconds = actualDuration;
        this.updatedAt = Instant.now();
    }

    /**
     * Cancel the route.
     *
     * @param reason the reason for cancellation
     */
    public void cancel(String reason) {
        if (this.status == RouteStatus.COMPLETED || this.status == RouteStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel route with status: " + this.status);
        }
        this.status = RouteStatus.CANCELLED;
        this.endTime = Instant.now();
        if (this.metadata == null) {
            this.metadata = new RouteMetadata();
        }
        this.metadata.setCancellationReason(reason);
        this.updatedAt = Instant.now();
    }

    /**
     * Pause the route.
     */
    public void pause() {
        if (this.status != RouteStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot pause route with status: " + this.status);
        }
        this.status = RouteStatus.PAUSED;
        this.updatedAt = Instant.now();
    }

    /**
     * Resume the route.
     */
    public void resume() {
        if (this.status != RouteStatus.PAUSED) {
            throw new IllegalStateException("Cannot resume route with status: " + this.status);
        }
        this.status = RouteStatus.IN_PROGRESS;
        this.updatedAt = Instant.now();
    }

    /**
     * Update estimated distance and duration.
     *
     * @param distance   the estimated distance in meters
     * @param duration   the estimated duration in seconds
     */
    public void updateEstimates(Double distance, Integer duration) {
        this.estimatedDistanceMeters = distance;
        this.estimatedDurationSeconds = duration;
        this.updatedAt = Instant.now();
    }

    /**
     * Set the route priority.
     *
     * @param priority the priority level
     */
    public void setPriority(RoutePriority priority) {
        this.priority = Objects.requireNonNull(priority, "priority cannot be null");
        this.updatedAt = Instant.now();
    }

    /**
     * Update the driver assigned to this route.
     *
     * @param driverId the new driver ID
     */
    public void reassignDriver(String driverId) {
        this.driverId = Objects.requireNonNull(driverId, "driverId cannot be null");
        this.updatedAt = Instant.now();
    }

    /**
     * Check if the route is active.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return this.status == RouteStatus.IN_PROGRESS || this.status == RouteStatus.PAUSED;
    }

    /**
     * Check if the route is completed.
     *
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return this.status == RouteStatus.COMPLETED;
    }

    /**
     * Get the number of waypoints.
     *
     * @return the waypoint count
     */
    public int getWaypointCount() {
        return this.waypoints != null ? this.waypoints.size() : 0;
    }

    /**
     * Get the number of orders.
     *
     * @return the order count
     */
    public int getOrderCount() {
        return this.orderIds != null ? this.orderIds.size() : 0;
    }

    /**
     * Calculate route efficiency (actual vs estimated).
     *
     * @return efficiency ratio (1.0 = perfect, >1 = took longer than estimated)
     */
    public double calculateEfficiency() {
        if (this.estimatedDurationSeconds == null || this.estimatedDurationSeconds == 0 ||
                this.actualDurationSeconds == null || this.actualDurationSeconds == 0) {
            return 0.0;
        }
        return (double) this.actualDurationSeconds / this.estimatedDurationSeconds;
    }

    /**
     * Update notes.
     *
     * @param notes the notes to set
     */
    public void updateNotes(String notes) {
        this.notes = notes;
        this.updatedAt = Instant.now();
    }

    /**
     * Validate the route.
     *
     * @throws IllegalArgumentException if validation fails
     */
    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("routeId is required");
        }
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("driverId is required");
        }
        if (waypoints == null || waypoints.isEmpty()) {
            throw new IllegalArgumentException("Route must have at least one waypoint");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getDriverId() {
        return driverId;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public List<RouteWaypoint> getWaypoints() {
        return waypoints;
    }

    public RouteStatus getStatus() {
        return status;
    }

    public GeoPoint getStartLocation() {
        return startLocation;
    }

    public GeoPoint getEndLocation() {
        return endLocation;
    }

    public Double getEstimatedDistanceMeters() {
        return estimatedDistanceMeters;
    }

    public Integer getEstimatedDurationSeconds() {
        return estimatedDurationSeconds;
    }

    public Double getActualDistanceMeters() {
        return actualDistanceMeters;
    }

    public Integer getActualDurationSeconds() {
        return actualDurationSeconds;
    }

    public RoutePriority getPriority() {
        return priority;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public String getNotes() {
        return notes;
    }

    public RouteMetadata getMetadata() {
        return metadata;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    // Setters for persistence
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    protected void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    protected void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }

    protected void setWaypoints(List<RouteWaypoint> waypoints) {
        this.waypoints = waypoints;
    }

    protected void setStatus(RouteStatus status) {
        this.status = status;
    }

    protected void setStartLocation(GeoPoint startLocation) {
        this.startLocation = startLocation;
    }

    public void setEndLocation(GeoPoint endLocation) {
        this.endLocation = endLocation;
    }

    protected void setEstimatedDistanceMeters(Double estimatedDistanceMeters) {
        this.estimatedDistanceMeters = estimatedDistanceMeters;
    }

    protected void setEstimatedDurationSeconds(Integer estimatedDurationSeconds) {
        this.estimatedDurationSeconds = estimatedDurationSeconds;
    }

    protected void setActualDistanceMeters(Double actualDistanceMeters) {
        this.actualDistanceMeters = actualDistanceMeters;
    }

    protected void setActualDurationSeconds(Integer actualDurationSeconds) {
        this.actualDurationSeconds = actualDurationSeconds;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    protected void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    protected void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    protected void setMetadata(RouteMetadata metadata) {
        this.metadata = metadata;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route that = (Route) o;
        return Objects.equals(routeId, that.routeId) &&
                Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId, tenantId);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", routeId='" + routeId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", waypointCount=" + getWaypointCount() +
                ", orderCount=" + getOrderCount() +
                '}';
    }

    /**
     * Route status enum.
     */
    public enum RouteStatus {
        PENDING,
        OPTIMIZED,
        IN_PROGRESS,
        PAUSED,
        COMPLETED,
        CANCELLED,
        FAILED
    }

    /**
     * Route priority enum.
     */
    public enum RoutePriority {
        LOW,
        NORMAL,
        HIGH,
        URGENT
    }

    /**
     * Vehicle type enum.
     */
    public enum VehicleType {
        CAR,
        VAN,
        TRUCK,
        MOTORCYCLE,
        BICYCLE,
        WALKING
    }

    /**
     * GeoPoint value object for GPS coordinates.
     */
    public static class GeoPoint {
        private final double latitude;
        private final double longitude;
        private String address;
        private String name;

        public GeoPoint(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public GeoPoint(double latitude, double longitude, String address) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    /**
     * Route metadata value object.
     */
    public static class RouteMetadata {
        private String createdBy;
        private String lastModifiedBy;
        private Integer optimizationCount;
        private Instant lastOptimizationTime;
        private String cancellationReason;
        private String version;

        public RouteMetadata() {
            this.optimizationCount = 0;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

        public Integer getOptimizationCount() {
            return optimizationCount;
        }

        public void setOptimizationCount(Integer optimizationCount) {
            this.optimizationCount = optimizationCount;
        }

        public Instant getLastOptimizationTime() {
            return lastOptimizationTime;
        }

        public void setLastOptimizationTime(Instant lastOptimizationTime) {
            this.lastOptimizationTime = lastOptimizationTime;
        }

        public String getCancellationReason() {
            return cancellationReason;
        }

        public void setCancellationReason(String cancellationReason) {
            this.cancellationReason = cancellationReason;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
