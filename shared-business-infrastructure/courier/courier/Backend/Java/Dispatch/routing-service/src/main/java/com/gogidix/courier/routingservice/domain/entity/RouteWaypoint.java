package com.gogidix.courier.routingservice.domain.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain Entity representing a waypoint in a route.
 * A waypoint is a location that must be visited during route execution.
 */
public class RouteWaypoint {

    @Field("waypoint_id")
    private String waypointId;

    @Field("sequence_number")
    private Integer sequenceNumber;

    @Field("location")
    private Route.GeoPoint location;

    @Field("order_id")
    private String orderId;

    @Field("customer_id")
    private String customerId;

    @Field("customer_name")
    private String customerName;

    @Field("address")
    private String address;

    @Field("waypoint_type")
    private WaypointType waypointType;

    @Field("status")
    private WaypointStatus status;

    @Field("estimated_arrival")
    private Instant estimatedArrival;

    @Field("actual_arrival")
    private Instant actualArrival;

    @Field("estimated_departure")
    private Instant estimatedDeparture;

    @Field("actual_departure")
    private Instant actualDeparture;

    @Field("service_duration_seconds")
    private Integer serviceDurationSeconds;

    @Field("distance_from_previous_meters")
    private Double distanceFromPreviousMeters;

    @Field("distance_to_next_meters")
    private Double distanceToNextMeters;

    @Field("travel_time_from_previous_seconds")
    private Integer travelTimeFromPreviousSeconds;

    @Field("travel_time_to_next_seconds")
    private Integer travelTimeToNextSeconds;

    @Field("time_window_start")
    private Instant timeWindowStart;

    @Field("time_window_end")
    private Instant timeWindowEnd;

    @Field("priority")
    private Integer priority;

    @Field("notes")
    private String notes;

    @Field("contact_phone")
    private String contactPhone;

    @Field("package_count")
    private Integer packageCount;

    @Field("package_weight_kg")
    private Double packageWeightKg;

    @Field("metadata")
    private WaypointMetadata metadata;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    /**
     * Default constructor for persistence.
     */
    protected RouteWaypoint() {
    }

    /**
     * Create a new waypoint.
     *
     * @param waypointId   the waypoint identifier
     * @param location     the location
     * @param waypointType the waypoint type
     */
    public RouteWaypoint(String waypointId, Route.GeoPoint location, WaypointType waypointType) {
        this.waypointId = waypointId != null ? waypointId : java.util.UUID.randomUUID().toString();
        this.location = Objects.requireNonNull(location, "location is required");
        this.waypointType = waypointType != null ? waypointType : WaypointType.DELIVERY;
        this.status = WaypointStatus.PENDING;
        this.priority = 5;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Create a new waypoint with order information.
     *
     * @param waypointId   the waypoint identifier
     * @param location     the location
     * @param orderId      the order ID
     * @param customerId   the customer ID
     * @param waypointType the waypoint type
     */
    public RouteWaypoint(String waypointId, Route.GeoPoint location, String orderId,
                         String customerId, WaypointType waypointType) {
        this(waypointId, location, waypointType);
        this.orderId = orderId;
        this.customerId = customerId;
    }

    // Domain Logic Methods

    /**
     * Mark the waypoint as arrived.
     */
    public void arrive() {
        if (this.status == WaypointStatus.COMPLETED) {
            throw new IllegalStateException("Cannot mark arrived on completed waypoint");
        }
        this.status = WaypointStatus.ARRIVED;
        this.actualArrival = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Mark the waypoint as in service (being serviced).
     */
    public void startService() {
        if (this.status != WaypointStatus.ARRIVED && this.status != WaypointStatus.PENDING) {
            throw new IllegalStateException("Cannot start service on waypoint with status: " + this.status);
        }
        this.status = WaypointStatus.IN_SERVICE;
        if (this.actualArrival == null) {
            this.actualArrival = Instant.now();
        }
        this.updatedAt = Instant.now();
    }

    /**
     * Complete the waypoint.
     */
    public void complete() {
        if (this.status != WaypointStatus.ARRIVED && this.status != WaypointStatus.IN_SERVICE) {
            throw new IllegalStateException("Cannot complete waypoint with status: " + this.status);
        }
        this.status = WaypointStatus.COMPLETED;
        this.actualDeparture = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Skip the waypoint.
     *
     * @param reason the reason for skipping
     */
    public void skip(String reason) {
        if (this.status == WaypointStatus.COMPLETED) {
            throw new IllegalStateException("Cannot skip completed waypoint");
        }
        this.status = WaypointStatus.SKIPPED;
        if (this.metadata == null) {
            this.metadata = new WaypointMetadata();
        }
        this.metadata.setSkipReason(reason);
        this.updatedAt = Instant.now();
    }

    /**
     * Fail the waypoint.
     *
     * @param reason the reason for failure
     */
    public void fail(String reason) {
        this.status = WaypointStatus.FAILED;
        if (this.metadata == null) {
            this.metadata = new WaypointMetadata();
        }
        this.metadata.setFailureReason(reason);
        this.updatedAt = Instant.now();
    }

    /**
     * Set the sequence number.
     *
     * @param sequenceNumber the sequence number
     */
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        this.updatedAt = Instant.now();
    }

    /**
     * Set time window for delivery.
     *
     * @param start the start of the time window
     * @param end   the end of the time window
     */
    public void setTimeWindow(Instant start, Instant end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new IllegalArgumentException("Time window start must be before end");
        }
        this.timeWindowStart = start;
        this.timeWindowEnd = end;
        this.updatedAt = Instant.now();
    }

    /**
     * Check if the waypoint is within time window.
     *
     * @return true if within or no time window set, false otherwise
     */
    public boolean isWithinTimeWindow() {
        if (timeWindowStart == null && timeWindowEnd == null) {
            return true;
        }
        Instant now = Instant.now();
        if (timeWindowStart != null && now.isBefore(timeWindowStart)) {
            return false;
        }
        return timeWindowEnd == null || !now.isAfter(timeWindowEnd);
    }

    /**
     * Check if the waypoint is late (past time window).
     *
     * @return true if late, false otherwise
     */
    public boolean isLate() {
        if (timeWindowEnd == null) {
            return false;
        }
        Instant now = Instant.now();
        return now.isAfter(timeWindowEnd) &&
                (status == WaypointStatus.PENDING ||
                 status == WaypointStatus.ARRIVED ||
                 status == WaypointStatus.IN_SERVICE);
    }

    /**
     * Update travel information from previous waypoint.
     *
     * @param distance   distance in meters
     * @param travelTime travel time in seconds
     */
    public void updateTravelFromPrevious(Double distance, Integer travelTime) {
        this.distanceFromPreviousMeters = distance;
        this.travelTimeFromPreviousSeconds = travelTime;
        this.updatedAt = Instant.now();
    }

    /**
     * Update travel information to next waypoint.
     *
     * @param distance   distance in meters
     * @param travelTime travel time in seconds
     */
    public void updateTravelToNext(Double distance, Integer travelTime) {
        this.distanceToNextMeters = distance;
        this.travelTimeToNextSeconds = travelTime;
        this.updatedAt = Instant.now();
    }

    /**
     * Calculate time at waypoint (arrival to departure).
     *
     * @return duration in seconds
     */
    public Long calculateTimeAtWaypoint() {
        if (actualArrival != null && actualDeparture != null) {
            return java.time.Duration.between(actualArrival, actualDeparture).getSeconds();
        }
        return null;
    }

    /**
     * Check if the waypoint has been visited.
     *
     * @return true if completed or skipped
     */
    public boolean isVisited() {
        return status == WaypointStatus.COMPLETED || status == WaypointStatus.SKIPPED;
    }

    /**
     * Check if the waypoint is pending.
     *
     * @return true if pending
     */
    public boolean isPending() {
        return status == WaypointStatus.PENDING;
    }

    /**
     * Update package information.
     *
     * @param packageCount    number of packages
     * @param packageWeightKg total weight
     */
    public void updatePackageInfo(Integer packageCount, Double packageWeightKg) {
        this.packageCount = packageCount;
        this.packageWeightKg = packageWeightKg;
        this.updatedAt = Instant.now();
    }

    // Getters
    public String getWaypointId() {
        return waypointId;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public Route.GeoPoint getLocation() {
        return location;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public WaypointType getWaypointType() {
        return waypointType;
    }

    public WaypointStatus getStatus() {
        return status;
    }

    public Instant getEstimatedArrival() {
        return estimatedArrival;
    }

    public Instant getActualArrival() {
        return actualArrival;
    }

    public Instant getEstimatedDeparture() {
        return estimatedDeparture;
    }

    public Instant getActualDeparture() {
        return actualDeparture;
    }

    public Integer getServiceDurationSeconds() {
        return serviceDurationSeconds;
    }

    public Double getDistanceFromPreviousMeters() {
        return distanceFromPreviousMeters;
    }

    public Double getDistanceToNextMeters() {
        return distanceToNextMeters;
    }

    public Integer getTravelTimeFromPreviousSeconds() {
        return travelTimeFromPreviousSeconds;
    }

    public Integer getTravelTimeToNextSeconds() {
        return travelTimeToNextSeconds;
    }

    public Instant getTimeWindowStart() {
        return timeWindowStart;
    }

    public Instant getTimeWindowEnd() {
        return timeWindowEnd;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getNotes() {
        return notes;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public Integer getPackageCount() {
        return packageCount;
    }

    public Double getPackageWeightKg() {
        return packageWeightKg;
    }

    public WaypointMetadata getMetadata() {
        return metadata;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // Setters for persistence
    public void setWaypointId(String waypointId) {
        this.waypointId = waypointId;
    }

    public void setLocation(Route.GeoPoint location) {
        this.location = location;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWaypointType(WaypointType waypointType) {
        this.waypointType = waypointType;
    }

    public void setStatus(WaypointStatus status) {
        this.status = status;
    }

    public void setEstimatedArrival(Instant estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
    }

    public void setActualArrival(Instant actualArrival) {
        this.actualArrival = actualArrival;
    }

    public void setEstimatedDeparture(Instant estimatedDeparture) {
        this.estimatedDeparture = estimatedDeparture;
    }

    public void setActualDeparture(Instant actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    public void setServiceDurationSeconds(Integer serviceDurationSeconds) {
        this.serviceDurationSeconds = serviceDurationSeconds;
    }

    public void setDistanceFromPreviousMeters(Double distanceFromPreviousMeters) {
        this.distanceFromPreviousMeters = distanceFromPreviousMeters;
    }

    public void setDistanceToNextMeters(Double distanceToNextMeters) {
        this.distanceToNextMeters = distanceToNextMeters;
    }

    public void setTravelTimeFromPreviousSeconds(Integer travelTimeFromPreviousSeconds) {
        this.travelTimeFromPreviousSeconds = travelTimeFromPreviousSeconds;
    }

    public void setTravelTimeToNextSeconds(Integer travelTimeToNextSeconds) {
        this.travelTimeToNextSeconds = travelTimeToNextSeconds;
    }

    public void setTimeWindowStart(Instant timeWindowStart) {
        this.timeWindowStart = timeWindowStart;
    }

    public void setTimeWindowEnd(Instant timeWindowEnd) {
        this.timeWindowEnd = timeWindowEnd;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setPackageCount(Integer packageCount) {
        this.packageCount = packageCount;
    }

    public void setPackageWeightKg(Double packageWeightKg) {
        this.packageWeightKg = packageWeightKg;
    }

    public void setMetadata(WaypointMetadata metadata) {
        this.metadata = metadata;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteWaypoint that = (RouteWaypoint) o;
        return Objects.equals(waypointId, that.waypointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(waypointId);
    }

    @Override
    public String toString() {
        return "RouteWaypoint{" +
                "waypointId='" + waypointId + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", orderId='" + orderId + '\'' +
                ", status=" + status +
                ", waypointType=" + waypointType +
                '}';
    }

    /**
     * Waypoint type enum.
     */
    public enum WaypointType {
        PICKUP,
        DELIVERY,
        TRANSFER,
        WAREHOUSE,
        FUEL_STOP,
        REST_STOP,
        CUSTOM
    }

    /**
     * Waypoint status enum.
     */
    public enum WaypointStatus {
        PENDING,
        ARRIVED,
        IN_SERVICE,
        COMPLETED,
        SKIPPED,
        FAILED,
        CANCELLED
    }

    /**
     * Waypoint metadata value object.
     */
    public static class WaypointMetadata {
        private String skipReason;
        private String failureReason;
        private String signature;
        private String photoUrl;
        private Integer attemptCount;
        private Instant firstAttempt;
        private String specialInstructions;

        public String getSkipReason() {
            return skipReason;
        }

        public void setSkipReason(String skipReason) {
            this.skipReason = skipReason;
        }

        public String getFailureReason() {
            return failureReason;
        }

        public void setFailureReason(String failureReason) {
            this.failureReason = failureReason;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public Integer getAttemptCount() {
            return attemptCount;
        }

        public void setAttemptCount(Integer attemptCount) {
            this.attemptCount = attemptCount;
        }

        public Instant getFirstAttempt() {
            return firstAttempt;
        }

        public void setFirstAttempt(Instant firstAttempt) {
            this.firstAttempt = firstAttempt;
        }

        public String getSpecialInstructions() {
            return specialInstructions;
        }

        public void setSpecialInstructions(String specialInstructions) {
            this.specialInstructions = specialInstructions;
        }
    }
}
