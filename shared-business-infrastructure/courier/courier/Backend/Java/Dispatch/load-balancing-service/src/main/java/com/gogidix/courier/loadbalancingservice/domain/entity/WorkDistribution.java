package com.gogidix.courier.loadbalancingservice.domain.entity;

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
 * Domain Entity representing a Work Distribution.
 * Tracks how work was distributed among drivers.
 */
@Document(collection = "work_distribution")
@CompoundIndex(name = "idx_distribution_batch", def = "{'tenantId': 1, 'batchId': 1}")
public class WorkDistribution {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("batch_id")
    private String batchId;

    @Field("distribution_type")
    private DistributionType distributionType;

    @Field("strategy_used")
    private String strategyUsed;

    @Field("rule_id")
    private String ruleId;

    @Field("zone_id")
    private String zoneId;

    @Field("assignments")
    private List<DriverAssignment> assignments;

    @Field("total_work_items")
    private Integer totalWorkItems;

    @Field("distributed_count")
    private Integer distributedCount;

    @Field("pending_count")
    private Integer pendingCount;

    @Field("failed_count")
    private Integer failedCount;

    @Field("status")
    private DistributionStatus status;

    @Field("started_at")
    private Instant startedAt;

    @Field("completed_at")
    private Instant completedAt;

    @Field("error_message")
    private String errorMessage;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    /**
     * Default constructor for persistence.
     */
    protected WorkDistribution() {
    }

    /**
     * Create a new WorkDistribution.
     *
     * @param tenantId         the tenant identifier
     * @param batchId          the batch identifier
     * @param distributionType the distribution type
     * @param totalWorkItems   total work items to distribute
     */
    public WorkDistribution(String tenantId, String batchId, DistributionType distributionType, Integer totalWorkItems) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.batchId = Objects.requireNonNull(batchId, "batchId is required");
        this.distributionType = Objects.requireNonNull(distributionType, "distributionType is required");
        this.totalWorkItems = Objects.requireNonNull(totalWorkItems, "totalWorkItems is required");
        this.assignments = new ArrayList<>();
        this.distributedCount = 0;
        this.pendingCount = totalWorkItems;
        this.failedCount = 0;
        this.status = DistributionStatus.PENDING;
        this.startedAt = Instant.now();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Domain Logic Methods

    /**
     * Add an assignment.
     *
     * @param driverId the driver ID
     * @param count    the number of work items
     */
    public void addAssignment(String driverId, Integer count) {
        DriverAssignment assignment = new DriverAssignment(driverId, count);
        this.assignments.add(assignment);
        this.distributedCount += count;
        this.pendingCount = Math.max(0, this.pendingCount - count);
        this.updatedAt = Instant.now();
        updateStatus();
    }

    /**
     * Mark assignment as failed.
     *
     * @param driverId the driver ID
     * @param count    the failed count
     */
    public void markFailed(String driverId, Integer count) {
        this.failedCount += count;
        this.distributedCount = Math.max(0, this.distributedCount - count);
        this.updatedAt = Instant.now();
        updateStatus();
    }

    /**
     * Start the distribution.
     *
     * @param strategy the strategy being used
     */
    public void start(String strategy) {
        this.status = DistributionStatus.IN_PROGRESS;
        this.strategyUsed = strategy;
        this.startedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Complete the distribution.
     */
    public void complete() {
        this.status = DistributionStatus.COMPLETED;
        this.completedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Mark distribution as failed.
     *
     * @param errorMessage the error message
     */
    public void fail(String errorMessage) {
        this.status = DistributionStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Check if distribution is complete.
     *
     * @return true if complete
     */
    public boolean isComplete() {
        return status == DistributionStatus.COMPLETED ||
               status == DistributionStatus.FAILED ||
               pendingCount == 0;
    }

    /**
     * Get progress percentage.
     *
     * @return progress percentage
     */
    public double getProgressPercent() {
        if (totalWorkItems == null || totalWorkItems == 0) {
            return 0.0;
        }
        return (double) distributedCount / totalWorkItems * 100;
    }

    private void updateStatus() {
        if (pendingCount == 0 && distributedCount == totalWorkItems) {
            this.status = DistributionStatus.COMPLETED;
        } else if (distributedCount > 0) {
            this.status = DistributionStatus.IN_PROGRESS;
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getBatchId() {
        return batchId;
    }

    public DistributionType getDistributionType() {
        return distributionType;
    }

    public String getStrategyUsed() {
        return strategyUsed;
    }

    public String getRuleId() {
        return ruleId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public List<DriverAssignment> getAssignments() {
        return assignments;
    }

    public Integer getTotalWorkItems() {
        return totalWorkItems;
    }

    public Integer getDistributedCount() {
        return distributedCount;
    }

    public Integer getPendingCount() {
        return pendingCount;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public DistributionStatus getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
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

    protected void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    protected void setDistributionType(DistributionType distributionType) {
        this.distributionType = distributionType;
    }

    protected void setStrategyUsed(String strategyUsed) {
        this.strategyUsed = strategyUsed;
    }

    protected void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    protected void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    protected void setAssignments(List<DriverAssignment> assignments) {
        this.assignments = assignments;
    }

    protected void setTotalWorkItems(Integer totalWorkItems) {
        this.totalWorkItems = totalWorkItems;
    }

    protected void setDistributedCount(Integer distributedCount) {
        this.distributedCount = distributedCount;
    }

    protected void setPendingCount(Integer pendingCount) {
        this.pendingCount = pendingCount;
    }

    protected void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    protected void setStatus(DistributionStatus status) {
        this.status = status;
    }

    protected void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    protected void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    protected void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Distribution type enum.
     */
    public enum DistributionType {
        IMMEDIATE,
        BATCH,
        SCHEDULED,
        PRIORITY_BASED,
        ZONE_BASED
    }

    /**
     * Distribution status enum.
     */
    public enum DistributionStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED,
        PARTIALLY_COMPLETED
    }

    /**
     * Driver assignment value object.
     */
    public static class DriverAssignment {
        @Field("driver_id")
        private String driverId;

        @Field("assigned_count")
        private Integer assignedCount;

        @Field("assigned_at")
        private Instant assignedAt;

        @Field("status")
        private AssignmentStatus status;

        public DriverAssignment() {
        }

        public DriverAssignment(String driverId, Integer assignedCount) {
            this.driverId = driverId;
            this.assignedCount = assignedCount;
            this.assignedAt = Instant.now();
            this.status = AssignmentStatus.PENDING;
        }

        public String getDriverId() {
            return driverId;
        }

        public void setDriverId(String driverId) {
            this.driverId = driverId;
        }

        public Integer getAssignedCount() {
            return assignedCount;
        }

        public void setAssignedCount(Integer assignedCount) {
            this.assignedCount = assignedCount;
        }

        public Instant getAssignedAt() {
            return assignedAt;
        }

        public void setAssignedAt(Instant assignedAt) {
            this.assignedAt = assignedAt;
        }

        public AssignmentStatus getStatus() {
            return status;
        }

        public void setStatus(AssignmentStatus status) {
            this.status = status;
        }

        /**
         * Assignment status enum.
         */
        public enum AssignmentStatus {
            PENDING,
            ACCEPTED,
            IN_PROGRESS,
            COMPLETED,
            FAILED,
            REJECTED
        }
    }
}
