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
 * Domain Entity representing an optimized route.
 * Contains the result of route optimization calculations.
 */
@Document(collection = "optimized_routes")
@CompoundIndex(name = "idx_original_route", def = "{'originalRouteId': 1}")
@CompoundIndex(name = "idx_tenant_created", def = "{'tenantId': 1, 'createdAt': -1}")
public class OptimizedRoute {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("optimized_route_id")
    private String optimizedRouteId;

    @Field("original_route_id")
    private String originalRouteId;

    @Field("driver_id")
    private String driverId;

    @Field("optimized_waypoints")
    private List<OptimizedWaypoint> optimizedWaypoints;

    @Field("optimization_algorithm")
    private OptimizationAlgorithm optimizationAlgorithm;

    @Field("original_distance_meters")
    private Double originalDistanceMeters;

    @Field("original_duration_seconds")
    private Integer originalDurationSeconds;

    @Field("optimized_distance_meters")
    private Double optimizedDistanceMeters;

    @Field("optimized_duration_seconds")
    private Integer optimizedDurationSeconds;

    @Field("distance_saved_meters")
    private Double distanceSavedMeters;

    @Field("time_saved_seconds")
    private Integer timeSavedSeconds;

    @Field("distance_improvement_percent")
    private Double distanceImprovementPercent;

    @Field("time_improvement_percent")
    private Double timeImprovementPercent;

    @Field("optimization_score")
    private Double optimizationScore;

    @Field("constraints")
    private OptimizationConstraints constraints;

    @Field("status")
    private OptimizationStatus status;

    @Field("execution_time_ms")
    private Long executionTimeMs;

    @Field("iteration_count")
    private Integer iterationCount;

    @Field("alternative_routes")
    private List<AlternativeRoute> alternativeRoutes;

    @Field("warnings")
    private List<String> warnings;

    @Field("metadata")
    private OptimizationMetadata metadata;

    @Field("created_at")
    private Instant createdAt;

    @Field("applied_at")
    private Instant appliedAt;

    /**
     * Default constructor for persistence.
     */
    protected OptimizedRoute() {
    }

    /**
     * Create a new optimized route.
     *
     * @param tenantId          the tenant identifier
     * @param optimizedRouteId  the optimized route identifier
     * @param originalRouteId   the original route identifier
     * @param driverId          the driver identifier
     * @param algorithm         the optimization algorithm used
     */
    public OptimizedRoute(String tenantId, String optimizedRouteId, String originalRouteId,
                          String driverId, OptimizationAlgorithm algorithm) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.optimizedRouteId = Objects.requireNonNull(optimizedRouteId, "optimizedRouteId is required");
        this.originalRouteId = Objects.requireNonNull(originalRouteId, "originalRouteId is required");
        this.driverId = driverId;
        this.optimizationAlgorithm = algorithm != null ? algorithm : OptimizationAlgorithm.NEAREST_NEIGHBOR;
        this.status = OptimizationStatus.PENDING;
        this.optimizedWaypoints = new ArrayList<>();
        this.alternativeRoutes = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.metadata = new OptimizationMetadata();
        this.createdAt = Instant.now();
    }

    // Domain Logic Methods

    /**
     * Add an optimized waypoint.
     *
     * @param waypoint the optimized waypoint
     */
    public void addOptimizedWaypoint(OptimizedWaypoint waypoint) {
        if (waypoint == null) {
            throw new IllegalArgumentException("waypoint cannot be null");
        }
        if (this.optimizedWaypoints == null) {
            this.optimizedWaypoints = new ArrayList<>();
        }
        waypoint.setSequenceNumber(this.optimizedWaypoints.size());
        this.optimizedWaypoints.add(waypoint);
    }

    /**
     * Set the optimization results.
     *
     * @param optimizedDistance   the optimized distance in meters
     * @param optimizedDuration   the optimized duration in seconds
     * @param originalDistance    the original distance in meters
     * @param originalDuration    the original duration in seconds
     */
    public void setOptimizationResults(Double optimizedDistance, Integer optimizedDuration,
                                       Double originalDistance, Integer originalDuration) {
        this.optimizedDistanceMeters = optimizedDistance;
        this.optimizedDurationSeconds = optimizedDuration;
        this.originalDistanceMeters = originalDistance;
        this.originalDurationSeconds = originalDuration;

        // Calculate improvements
        if (originalDistance != null && originalDistance > 0 && optimizedDistance != null) {
            this.distanceSavedMeters = originalDistance - optimizedDistance;
            this.distanceImprovementPercent = (distanceSavedMeters / originalDistance) * 100;
        }

        if (originalDuration != null && originalDuration > 0 && optimizedDuration != null) {
            this.timeSavedSeconds = originalDuration - optimizedDuration;
            this.timeImprovementPercent = ((double) timeSavedSeconds / originalDuration) * 100;
        }

        // Calculate overall optimization score
        this.optimizationScore = calculateOptimizationScore();
    }

    /**
     * Calculate the optimization score (0-100).
     *
     * @return the optimization score
     */
    public double calculateOptimizationScore() {
        double score = 0.0;

        if (distanceImprovementPercent != null) {
            score += Math.min(distanceImprovementPercent, 30); // Max 30 points for distance
        }

        if (timeImprovementPercent != null) {
            score += Math.min(timeImprovementPercent, 30); // Max 30 points for time
        }

        // Add points for constraints satisfaction
        if (constraints != null) {
            if (Boolean.TRUE.equals(constraints.getTimeWindowsSatisfied())) {
                score += 20; // Max 20 points for time windows
            }
            if (Boolean.TRUE.equals(constraints.getCapacityConstraintsSatisfied())) {
                score += 10; // Max 10 points for capacity
            }
            if (Boolean.TRUE.equals(constraints.getDriverBreaksSatisfied())) {
                score += 10; // Max 10 points for breaks
            }
        }

        return Math.min(score, 100.0);
    }

    /**
     * Mark the optimization as completed.
     *
     * @param executionTime the execution time in milliseconds
     * @param iterations    the number of iterations
     */
    public void markCompleted(Long executionTime, Integer iterations) {
        this.status = OptimizationStatus.COMPLETED;
        this.executionTimeMs = executionTime;
        this.iterationCount = iterations;
    }

    /**
     * Mark the optimization as failed.
     *
     * @param reason the failure reason
     */
    public void markFailed(String reason) {
        this.status = OptimizationStatus.FAILED;
        if (this.metadata == null) {
            this.metadata = new OptimizationMetadata();
        }
        this.metadata.setFailureReason(reason);
    }

    /**
     * Apply the optimization to the original route.
     */
    public void apply() {
        if (this.status != OptimizationStatus.COMPLETED) {
            throw new IllegalStateException("Cannot apply optimization with status: " + this.status);
        }
        this.status = OptimizationStatus.APPLIED;
        this.appliedAt = Instant.now();
    }

    /**
     * Add a warning.
     *
     * @param warning the warning message
     */
    public void addWarning(String warning) {
        if (this.warnings == null) {
            this.warnings = new ArrayList<>();
        }
        this.warnings.add(warning);
    }

    /**
     * Add an alternative route.
     *
     * @param alternative the alternative route
     */
    public void addAlternativeRoute(AlternativeRoute alternative) {
        if (this.alternativeRoutes == null) {
            this.alternativeRoutes = new ArrayList<>();
        }
        this.alternativeRoutes.add(alternative);
    }

    /**
     * Set optimization constraints.
     *
     * @param constraints the constraints
     */
    public void setConstraints(OptimizationConstraints constraints) {
        this.constraints = constraints;
    }

    /**
     * Check if the optimization is significant (>5% improvement).
     *
     * @return true if significant
     */
    public boolean isSignificantImprovement() {
        return (distanceImprovementPercent != null && distanceImprovementPercent > 5) ||
                (timeImprovementPercent != null && timeImprovementPercent > 5);
    }

    /**
     * Check if the optimization has warnings.
     *
     * @return true if has warnings
     */
    public boolean hasWarnings() {
        return warnings != null && !warnings.isEmpty();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getOptimizedRouteId() {
        return optimizedRouteId;
    }

    public String getOriginalRouteId() {
        return originalRouteId;
    }

    public String getDriverId() {
        return driverId;
    }

    public List<OptimizedWaypoint> getOptimizedWaypoints() {
        return optimizedWaypoints;
    }

    public OptimizationAlgorithm getOptimizationAlgorithm() {
        return optimizationAlgorithm;
    }

    public Double getOriginalDistanceMeters() {
        return originalDistanceMeters;
    }

    public Integer getOriginalDurationSeconds() {
        return originalDurationSeconds;
    }

    public Double getOptimizedDistanceMeters() {
        return optimizedDistanceMeters;
    }

    public Integer getOptimizedDurationSeconds() {
        return optimizedDurationSeconds;
    }

    public Double getDistanceSavedMeters() {
        return distanceSavedMeters;
    }

    public Integer getTimeSavedSeconds() {
        return timeSavedSeconds;
    }

    public Double getDistanceImprovementPercent() {
        return distanceImprovementPercent;
    }

    public Double getTimeImprovementPercent() {
        return timeImprovementPercent;
    }

    public Double getOptimizationScore() {
        return optimizationScore;
    }

    public OptimizationConstraints getConstraints() {
        return constraints;
    }

    public OptimizationStatus getStatus() {
        return status;
    }

    public Long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public Integer getIterationCount() {
        return iterationCount;
    }

    public List<AlternativeRoute> getAlternativeRoutes() {
        return alternativeRoutes;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public OptimizationMetadata getMetadata() {
        return metadata;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    // Setters for persistence
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setOptimizedRouteId(String optimizedRouteId) {
        this.optimizedRouteId = optimizedRouteId;
    }

    protected void setOriginalRouteId(String originalRouteId) {
        this.originalRouteId = originalRouteId;
    }

    protected void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    protected void setOptimizedWaypoints(List<OptimizedWaypoint> optimizedWaypoints) {
        this.optimizedWaypoints = optimizedWaypoints;
    }

    protected void setOptimizationAlgorithm(OptimizationAlgorithm optimizationAlgorithm) {
        this.optimizationAlgorithm = optimizationAlgorithm;
    }

    protected void setOriginalDistanceMeters(Double originalDistanceMeters) {
        this.originalDistanceMeters = originalDistanceMeters;
    }

    protected void setOriginalDurationSeconds(Integer originalDurationSeconds) {
        this.originalDurationSeconds = originalDurationSeconds;
    }

    protected void setOptimizedDistanceMeters(Double optimizedDistanceMeters) {
        this.optimizedDistanceMeters = optimizedDistanceMeters;
    }

    protected void setOptimizedDurationSeconds(Integer optimizedDurationSeconds) {
        this.optimizedDurationSeconds = optimizedDurationSeconds;
    }

    protected void setDistanceSavedMeters(Double distanceSavedMeters) {
        this.distanceSavedMeters = distanceSavedMeters;
    }

    protected void setTimeSavedSeconds(Integer timeSavedSeconds) {
        this.timeSavedSeconds = timeSavedSeconds;
    }

    protected void setDistanceImprovementPercent(Double distanceImprovementPercent) {
        this.distanceImprovementPercent = distanceImprovementPercent;
    }

    protected void setTimeImprovementPercent(Double timeImprovementPercent) {
        this.timeImprovementPercent = timeImprovementPercent;
    }

    protected void setOptimizationScore(Double optimizationScore) {
        this.optimizationScore = optimizationScore;
    }

    protected void setStatus(OptimizationStatus status) {
        this.status = status;
    }

    protected void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    protected void setIterationCount(Integer iterationCount) {
        this.iterationCount = iterationCount;
    }

    protected void setAlternativeRoutes(List<AlternativeRoute> alternativeRoutes) {
        this.alternativeRoutes = alternativeRoutes;
    }

    protected void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    protected void setMetadata(OptimizationMetadata metadata) {
        this.metadata = metadata;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setAppliedAt(Instant appliedAt) {
        this.appliedAt = appliedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptimizedRoute that = (OptimizedRoute) o;
        return Objects.equals(optimizedRouteId, that.optimizedRouteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(optimizedRouteId);
    }

    @Override
    public String toString() {
        return "OptimizedRoute{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", optimizedRouteId='" + optimizedRouteId + '\'' +
                ", originalRouteId='" + originalRouteId + '\'' +
                ", status=" + status +
                ", optimizationScore=" + optimizationScore +
                ", distanceImprovementPercent=" + distanceImprovementPercent +
                '}';
    }

    /**
     * Optimization algorithm enum.
     */
    public enum OptimizationAlgorithm {
        NEAREST_NEIGHBOR,
        GENETIC_ALGORITHM,
        SIMULATED_ANNEALING,
        ANT_COLONY,
        TABU_SEARCH,
        OR_OPT,
        TWO_OPT,
        THREE_OPT,
        INSERTION_HEURISTIC,
        SAVINGS_ALGORITHM,
        CLUSTER_FIRST_ROUTE_SECOND,
        HYBRID
    }

    /**
     * Optimization status enum.
     */
    public enum OptimizationStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        APPLIED,
        FAILED,
        CANCELLED
    }

    /**
     * Optimized waypoint value object.
     */
    public static class OptimizedWaypoint {
        @Field("waypoint_id")
        private String waypointId;

        @Field("sequence_number")
        private Integer sequenceNumber;

        @Field("estimated_arrival")
        private Instant estimatedArrival;

        @Field("estimated_departure")
        private Instant estimatedDeparture;

        @Field("cumulative_distance_meters")
        private Double cumulativeDistanceMeters;

        @Field("cumulative_time_seconds")
        private Integer cumulativeTimeSeconds;

        public OptimizedWaypoint(String waypointId, Integer sequenceNumber) {
            this.waypointId = waypointId;
            this.sequenceNumber = sequenceNumber;
        }

        public String getWaypointId() {
            return waypointId;
        }

        public void setWaypointId(String waypointId) {
            this.waypointId = waypointId;
        }

        public Integer getSequenceNumber() {
            return sequenceNumber;
        }

        public void setSequenceNumber(Integer sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
        }

        public Instant getEstimatedArrival() {
            return estimatedArrival;
        }

        public void setEstimatedArrival(Instant estimatedArrival) {
            this.estimatedArrival = estimatedArrival;
        }

        public Instant getEstimatedDeparture() {
            return estimatedDeparture;
        }

        public void setEstimatedDeparture(Instant estimatedDeparture) {
            this.estimatedDeparture = estimatedDeparture;
        }

        public Double getCumulativeDistanceMeters() {
            return cumulativeDistanceMeters;
        }

        public void setCumulativeDistanceMeters(Double cumulativeDistanceMeters) {
            this.cumulativeDistanceMeters = cumulativeDistanceMeters;
        }

        public Integer getCumulativeTimeSeconds() {
            return cumulativeTimeSeconds;
        }

        public void setCumulativeTimeSeconds(Integer cumulativeTimeSeconds) {
            this.cumulativeTimeSeconds = cumulativeTimeSeconds;
        }
    }

    /**
     * Optimization constraints value object.
     */
    public static class OptimizationConstraints {
        @Field("max_route_duration_seconds")
        private Integer maxRouteDurationSeconds;

        @Field("max_route_distance_meters")
        private Double maxRouteDistanceMeters;

        @Field("time_windows_satisfied")
        private Boolean timeWindowsSatisfied;

        @Field("capacity_constraints_satisfied")
        private Boolean capacityConstraintsSatisfied;

        @Field("driver_breaks_satisfied")
        private Boolean driverBreaksSatisfied;

        @Field("vehicle_constraints_satisfied")
        private Boolean vehicleConstraintsSatisfied;

        public Integer getMaxRouteDurationSeconds() {
            return maxRouteDurationSeconds;
        }

        public void setMaxRouteDurationSeconds(Integer maxRouteDurationSeconds) {
            this.maxRouteDurationSeconds = maxRouteDurationSeconds;
        }

        public Double getMaxRouteDistanceMeters() {
            return maxRouteDistanceMeters;
        }

        public void setMaxRouteDistanceMeters(Double maxRouteDistanceMeters) {
            this.maxRouteDistanceMeters = maxRouteDistanceMeters;
        }

        public Boolean getTimeWindowsSatisfied() {
            return timeWindowsSatisfied;
        }

        public void setTimeWindowsSatisfied(Boolean timeWindowsSatisfied) {
            this.timeWindowsSatisfied = timeWindowsSatisfied;
        }

        public Boolean getCapacityConstraintsSatisfied() {
            return capacityConstraintsSatisfied;
        }

        public void setCapacityConstraintsSatisfied(Boolean capacityConstraintsSatisfied) {
            this.capacityConstraintsSatisfied = capacityConstraintsSatisfied;
        }

        public Boolean getDriverBreaksSatisfied() {
            return driverBreaksSatisfied;
        }

        public void setDriverBreaksSatisfied(Boolean driverBreaksSatisfied) {
            this.driverBreaksSatisfied = driverBreaksSatisfied;
        }

        public Boolean getVehicleConstraintsSatisfied() {
            return vehicleConstraintsSatisfied;
        }

        public void setVehicleConstraintsSatisfied(Boolean vehicleConstraintsSatisfied) {
            this.vehicleConstraintsSatisfied = vehicleConstraintsSatisfied;
        }
    }

    /**
     * Alternative route value object.
     */
    public static class AlternativeRoute {
        @Field("route_id")
        private String routeId;

        @Field("distance_meters")
        private Double distanceMeters;

        @Field("duration_seconds")
        private Integer durationSeconds;

        @Field("score")
        private Double score;

        public String getRouteId() {
            return routeId;
        }

        public void setRouteId(String routeId) {
            this.routeId = routeId;
        }

        public Double getDistanceMeters() {
            return distanceMeters;
        }

        public void setDistanceMeters(Double distanceMeters) {
            this.distanceMeters = distanceMeters;
        }

        public Integer getDurationSeconds() {
            return durationSeconds;
        }

        public void setDurationSeconds(Integer durationSeconds) {
            this.durationSeconds = durationSeconds;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }
    }

    /**
     * Optimization metadata value object.
     */
    public static class OptimizationMetadata {
        @Field("failure_reason")
        private String failureReason;

        @Field("algorithm_version")
        private String algorithmVersion;

        @Field("parameters_used")
        private String parametersUsed;

        @Field("computed_by")
        private String computedBy;

        public String getFailureReason() {
            return failureReason;
        }

        public void setFailureReason(String failureReason) {
            this.failureReason = failureReason;
        }

        public String getAlgorithmVersion() {
            return algorithmVersion;
        }

        public void setAlgorithmVersion(String algorithmVersion) {
            this.algorithmVersion = algorithmVersion;
        }

        public String getParametersUsed() {
            return parametersUsed;
        }

        public void setParametersUsed(String parametersUsed) {
            this.parametersUsed = parametersUsed;
        }

        public String getComputedBy() {
            return computedBy;
        }

        public void setComputedBy(String computedBy) {
            this.computedBy = computedBy;
        }
    }
}
