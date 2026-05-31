package com.gogidix.courier.assignmentservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Domain Entity representing a driver assignment.
 * Optimized assignment of drivers to dispatch orders with intelligent routing.
 */
@Document(collection = "driver_assignments")
@CompoundIndex(name = "idx_dispatch_tenant", def = "{'dispatchId': 1, 'tenantId': 1}")
@CompoundIndex(name = "idx_driver_status", def = "{'driverId': 1, 'status': 1}")
@CompoundIndex(name = "idx_tenant_created", def = "{'tenantId': 1, 'createdAt': -1}")
public class DriverAssignment {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("dispatch_id")
    private String dispatchId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Field("status")
    private AssignmentStatus status;

    @Field("priority")
    private AssignmentPriority priority;

    @Field("pickup_location")
    private Location pickupLocation;

    @Field("delivery_location")
    private Location deliveryLocation;

    @Field("estimated_distance_km")
    private Double estimatedDistanceKm;

    @Field("estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Field("actual_distance_km")
    private Double actualDistanceKm;

    @Field("actual_duration_minutes")
    private Integer actualDurationMinutes;

    @Field("assignment_score")
    private Double assignmentScore;

    @Field("assignment_reason")
    private String assignmentReason;

    @Field("assigned_at")
    private Instant assignedAt;

    @Field("accepted_at")
    private Instant acceptedAt;

    @Field("started_at")
    private Instant startedAt;

    @Field("completed_at")
    private Instant completedAt;

    @Field("cancelled_at")
    private Instant cancelledAt;

    @Field("cancellation_reason")
    private String cancellationReason;

    @Field("notes")
    private String notes;

    @Field("metadata")
    private AssignmentMetadata metadata;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("version")
    private Long version;

    /**
     * Default constructor for persistence.
     */
    protected DriverAssignment() {
    }

    /**
     * Create a new driver assignment.
     *
     * @param tenantId        the tenant identifier
     * @param dispatchId      the dispatch order identifier
     * @param driverId        the driver identifier
     * @param pickupLocation  the pickup location
     * @param deliveryLocation the delivery location
     */
    public DriverAssignment(
            String tenantId,
            String dispatchId,
            String driverId,
            Location pickupLocation,
            Location deliveryLocation) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.dispatchId = Objects.requireNonNull(dispatchId, "dispatchId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.pickupLocation = Objects.requireNonNull(pickupLocation, "pickupLocation is required");
        this.deliveryLocation = Objects.requireNonNull(deliveryLocation, "deliveryLocation is required");
        this.status = AssignmentStatus.PENDING;
        this.priority = AssignmentPriority.NORMAL;
        this.assignmentScore = 0.0;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.version = 0L;
        this.metadata = new AssignmentMetadata();
    }

    // Domain Logic Methods

    /**
     * Accept the assignment.
     *
     * @throws IllegalStateException if assignment is not pending
     */
    public void accept() {
        if (this.status != AssignmentStatus.PENDING) {
            throw new IllegalStateException("Assignment can only be accepted in PENDING status. Current: " + this.status);
        }
        this.status = AssignmentStatus.ACCEPTED;
        this.acceptedAt = Instant.now();
        this.updatedAt = Instant.now();
        this.version++;
    }

    /**
     * Start the assignment (driver en route to pickup).
     *
     * @throws IllegalStateException if assignment is not accepted
     */
    public void start() {
        if (this.status != AssignmentStatus.ACCEPTED) {
            throw new IllegalStateException("Assignment can only be started in ACCEPTED status. Current: " + this.status);
        }
        this.status = AssignmentStatus.IN_PROGRESS;
        this.startedAt = Instant.now();
        this.updatedAt = Instant.now();
        this.version++;
    }

    /**
     * Complete the assignment.
     *
     * @param actualDistanceKm     the actual distance traveled in km
     * @param actualDurationMinutes the actual duration in minutes
     * @throws IllegalStateException if assignment is not in progress
     */
    public void complete(Double actualDistanceKm, Integer actualDurationMinutes) {
        if (this.status != AssignmentStatus.IN_PROGRESS) {
            throw new IllegalStateException("Assignment can only be completed in IN_PROGRESS status. Current: " + this.status);
        }
        this.status = AssignmentStatus.COMPLETED;
        this.completedAt = Instant.now();
        this.actualDistanceKm = actualDistanceKm;
        this.actualDurationMinutes = actualDurationMinutes;
        this.updatedAt = Instant.now();
        this.version++;
    }

    /**
     * Cancel the assignment.
     *
     * @param reason the cancellation reason
     * @throws IllegalStateException if assignment is already completed or cancelled
     */
    public void cancel(String reason) {
        if (this.status == AssignmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed assignment");
        }
        if (this.status == AssignmentStatus.CANCELLED) {
            throw new IllegalStateException("Assignment is already cancelled");
        }
        this.status = AssignmentStatus.CANCELLED;
        this.cancelledAt = Instant.now();
        this.cancellationReason = reason;
        this.updatedAt = Instant.now();
        this.version++;
    }

    /**
     * Reassign the assignment to a different driver.
     *
     * @param newDriverId the new driver identifier
     * @param reason      the reassignment reason
     * @throws IllegalStateException if assignment is completed or cancelled
     */
    public void reassign(String newDriverId, String reason) {
        if (this.status == AssignmentStatus.COMPLETED || this.status == AssignmentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot reassign a " + this.status + " assignment");
        }

        String oldDriverId = this.driverId;
        this.driverId = Objects.requireNonNull(newDriverId, "newDriverId is required");
        this.status = AssignmentStatus.PENDING;
        this.assignmentReason = reason;
        this.acceptedAt = null;
        this.startedAt = null;
        this.updatedAt = Instant.now();
        this.version++;

        // Track reassignment in metadata
        if (this.metadata == null) {
            this.metadata = new AssignmentMetadata();
        }
        this.metadata.addReassignment(oldDriverId, newDriverId, Instant.now(), reason);
    }

    /**
     * Update the assignment score for optimization purposes.
     *
     * @param score the optimization score (higher is better)
     */
    public void updateScore(Double score) {
        this.assignmentScore = Objects.requireNonNull(score, "score is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Set estimated distance and duration for the assignment.
     *
     * @param distanceKm         the estimated distance in km
     * @param durationMinutes    the estimated duration in minutes
     */
    public void setEstimates(Double distanceKm, Integer durationMinutes) {
        this.estimatedDistanceKm = distanceKm;
        this.estimatedDurationMinutes = durationMinutes;
        this.updatedAt = Instant.now();
    }

    /**
     * Set assignment priority.
     *
     * @param priority the assignment priority
     */
    public void setPriority(AssignmentPriority priority) {
        this.priority = Objects.requireNonNull(priority, "priority is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Check if the assignment is in a terminal state.
     *
     * @return true if completed or cancelled, false otherwise
     */
    public boolean isTerminal() {
        return this.status == AssignmentStatus.COMPLETED || this.status == AssignmentStatus.CANCELLED;
    }

    /**
     * Check if the assignment can be reassigned.
     *
     * @return true if reassignable, false otherwise
     */
    public boolean isReassignable() {
        return this.status == AssignmentStatus.PENDING || this.status == AssignmentStatus.ACCEPTED;
    }

    /**
     * Calculate the efficiency ratio (estimated vs actual).
     *
     * @return efficiency ratio or null if not yet completed
     */
    public Double calculateEfficiencyRatio() {
        if (this.status != AssignmentStatus.COMPLETED ||
            this.estimatedDistanceKm == null || this.actualDistanceKm == null ||
            this.estimatedDistanceKm == 0) {
            return null;
        }
        return this.estimatedDistanceKm / this.actualDistanceKm;
    }

    /**
     * Validate the assignment state.
     *
     * @throws IllegalArgumentException if validation fails
     */
    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (dispatchId == null || dispatchId.isBlank()) {
            throw new IllegalArgumentException("dispatchId is required");
        }
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("driverId is required");
        }
        if (pickupLocation == null) {
            throw new IllegalArgumentException("pickupLocation is required");
        }
        if (deliveryLocation == null) {
            throw new IllegalArgumentException("deliveryLocation is required");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public String getDriverId() {
        return driverId;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public AssignmentPriority getPriority() {
        return priority;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Location getDeliveryLocation() {
        return deliveryLocation;
    }

    public Double getEstimatedDistanceKm() {
        return estimatedDistanceKm;
    }

    public Integer getEstimatedDurationMinutes() {
        return estimatedDurationMinutes;
    }

    public Double getActualDistanceKm() {
        return actualDistanceKm;
    }

    public Integer getActualDurationMinutes() {
        return actualDurationMinutes;
    }

    public Double getAssignmentScore() {
        return assignmentScore;
    }

    public String getAssignmentReason() {
        return assignmentReason;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public Instant getAcceptedAt() {
        return acceptedAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public String getNotes() {
        return notes;
    }

    public AssignmentMetadata getMetadata() {
        return metadata;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    // Setters for persistence/MongoDB mapping
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    protected void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    protected void setStatus(AssignmentStatus status) {
        this.status = status;
    }


    protected void setPickupLocation(Location pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    protected void setDeliveryLocation(Location deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public void setEstimatedDistanceKm(Double estimatedDistanceKm) {
        this.estimatedDistanceKm = estimatedDistanceKm;
    }

    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) {
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }

    protected void setActualDistanceKm(Double actualDistanceKm) {
        this.actualDistanceKm = actualDistanceKm;
    }

    protected void setActualDurationMinutes(Integer actualDurationMinutes) {
        this.actualDurationMinutes = actualDurationMinutes;
    }

    protected void setAssignmentScore(Double assignmentScore) {
        this.assignmentScore = assignmentScore;
    }

    public void setAssignmentReason(String assignmentReason) {
        this.assignmentReason = assignmentReason;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    protected void setAcceptedAt(Instant acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    protected void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    protected void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    protected void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    protected void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    protected void setMetadata(AssignmentMetadata metadata) {
        this.metadata = metadata;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DriverAssignment that = (DriverAssignment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DriverAssignment{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", dispatchId='" + dispatchId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Assignment status enum.
     */
    public enum AssignmentStatus {
        PENDING,
        ACCEPTED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        FAILED
    }

    /**
     * Assignment priority enum.
     */
    public enum AssignmentPriority {
        LOW,
        NORMAL,
        HIGH,
        URGENT,
        EMERGENCY
    }

    /**
     * Location value object.
     */
    public static class Location {
        private Double latitude;
        private Double longitude;
        private String address;
        private String city;
        private String postalCode;
        private String country;

        public Location() {
        }

        public Location(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Location(Double latitude, Double longitude, String address) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
        }

        /**
         * Calculate distance to another location in kilometers using Haversine formula.
         */
        public Double distanceTo(Location other) {
            if (other == null || this.latitude == null || this.longitude == null ||
                other.latitude == null || other.longitude == null) {
                return null;
            }

            final int EARTH_RADIUS_KM = 6371;

            double lat1Rad = Math.toRadians(this.latitude);
            double lat2Rad = Math.toRadians(other.latitude);
            double deltaLatRad = Math.toRadians(other.latitude - this.latitude);
            double deltaLonRad = Math.toRadians(other.longitude - this.longitude);

            double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) +
                    Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                            Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return EARTH_RADIUS_KM * c;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    /**
     * Assignment metadata value object.
     */
    public static class AssignmentMetadata {
        private String assignedBy;
        private String assignmentAlgorithm;
        private List<ReassignmentRecord> reassignmentHistory;
        private Integer attemptCount;
        private String previousDriverId;

        public AssignmentMetadata() {
            this.reassignmentHistory = new ArrayList<>();
            this.attemptCount = 1;
        }

        public void addReassignment(String fromDriverId, String toDriverId, Instant timestamp, String reason) {
            if (this.reassignmentHistory == null) {
                this.reassignmentHistory = new ArrayList<>();
            }
            this.reassignmentHistory.add(new ReassignmentRecord(fromDriverId, toDriverId, timestamp, reason));
            this.previousDriverId = fromDriverId;
            this.attemptCount++;
        }

        public String getAssignedBy() {
            return assignedBy;
        }

        public void setAssignedBy(String assignedBy) {
            this.assignedBy = assignedBy;
        }

        public String getAssignmentAlgorithm() {
            return assignmentAlgorithm;
        }

        public void setAssignmentAlgorithm(String assignmentAlgorithm) {
            this.assignmentAlgorithm = assignmentAlgorithm;
        }

        public List<ReassignmentRecord> getReassignmentHistory() {
            return reassignmentHistory;
        }

        public void setReassignmentHistory(List<ReassignmentRecord> reassignmentHistory) {
            this.reassignmentHistory = reassignmentHistory;
        }

        public Integer getAttemptCount() {
            return attemptCount;
        }

        public void setAttemptCount(Integer attemptCount) {
            this.attemptCount = attemptCount;
        }

        public String getPreviousDriverId() {
            return previousDriverId;
        }

        public void setPreviousDriverId(String previousDriverId) {
            this.previousDriverId = previousDriverId;
        }

        /**
         * Reassignment record value object.
         */
        public static class ReassignmentRecord {
            private String fromDriverId;
            private String toDriverId;
            private Instant timestamp;
            private String reason;

            public ReassignmentRecord() {
            }

            public ReassignmentRecord(String fromDriverId, String toDriverId, Instant timestamp, String reason) {
                this.fromDriverId = fromDriverId;
                this.toDriverId = toDriverId;
                this.timestamp = timestamp;
                this.reason = reason;
            }

            public String getFromDriverId() {
                return fromDriverId;
            }

            public void setFromDriverId(String fromDriverId) {
                this.fromDriverId = fromDriverId;
            }

            public String getToDriverId() {
                return toDriverId;
            }

            public void setToDriverId(String toDriverId) {
                this.toDriverId = toDriverId;
            }

            public Instant getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(Instant timestamp) {
                this.timestamp = timestamp;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }
        }
    }
}
