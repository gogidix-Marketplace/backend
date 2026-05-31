package com.gogidix.courier.loadbalancingservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing a Driver's Capacity.
 * Tracks current load and capacity for load balancing decisions.
 */
@Document(collection = "driver_capacity")
@CompoundIndex(name = "idx_capacity_driver_zone", def = "{'tenantId': 1, 'driverId': 1, 'zoneId': 1}")
public class DriverCapacity {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Indexed
    @Field("zone_id")
    private String zoneId;

    @Field("max_capacity")
    private Integer maxCapacity;

    @Field("current_load")
    private Integer currentLoad;

    @Field("utilization_percent")
    private Double utilizationPercent;

    @Field("status")
    private CapacityStatus status;

    @Field("last_assignment")
    private Instant lastAssignment;

    @Field("total_assignments_today")
    private Integer totalAssignmentsToday;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    /**
     * Default constructor for persistence.
     */
    protected DriverCapacity() {
    }

    /**
     * Create a new DriverCapacity.
     *
     * @param tenantId   the tenant identifier
     * @param driverId   the driver identifier
     * @param zoneId     the zone identifier
     * @param maxCapacity the maximum capacity
     */
    public DriverCapacity(String tenantId, String driverId, String zoneId, Integer maxCapacity) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.zoneId = Objects.requireNonNull(zoneId, "zoneId is required");
        this.maxCapacity = Objects.requireNonNull(maxCapacity, "maxCapacity is required");
        this.currentLoad = 0;
        this.utilizationPercent = 0.0;
        this.status = CapacityStatus.AVAILABLE;
        this.totalAssignmentsToday = 0;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Domain Logic Methods

    /**
     * Assign work to this driver.
     *
     * @return true if assignment successful
     */
    public boolean assign() {
        if (currentLoad >= maxCapacity) {
            return false;
        }
        currentLoad++;
        totalAssignmentsToday++;
        lastAssignment = Instant.now();
        updateUtilization();
        updateStatus();
        updatedAt = Instant.now();
        return true;
    }

    /**
     * Release work from this driver.
     */
    public void release() {
        if (currentLoad > 0) {
            currentLoad--;
            updateUtilization();
            updateStatus();
            updatedAt = Instant.now();
        }
    }

    /**
     * Update max capacity.
     *
     * @param newCapacity the new capacity
     */
    public void updateCapacity(Integer newCapacity) {
        this.maxCapacity = Objects.requireNonNull(newCapacity, "capacity is required");
        if (currentLoad > maxCapacity) {
            currentLoad = maxCapacity;
        }
        updateUtilization();
        updateStatus();
        updatedAt = Instant.now();
    }

    /**
     * Check if driver is available.
     *
     * @return true if available
     */
    public boolean isAvailable() {
        return status == CapacityStatus.AVAILABLE;
    }

    /**
     * Check if driver is at full capacity.
     *
     * @return true if at capacity
     */
    public boolean isAtCapacity() {
        return currentLoad >= maxCapacity;
    }

    /**
     * Get available slots.
     *
     * @return number of available slots
     */
    public int getAvailableSlots() {
        return Math.max(0, maxCapacity - currentLoad);
    }

    /**
     * Check if driver is overloaded (above threshold).
     *
     * @param threshold the overload threshold (0-1)
     * @return true if overloaded
     */
    public boolean isOverloaded(double threshold) {
        return utilizationPercent != null && utilizationPercent >= threshold * 100;
    }

    private void updateUtilization() {
        this.utilizationPercent = maxCapacity > 0
            ? (double) currentLoad / maxCapacity * 100
            : 0.0;
    }

    private void updateStatus() {
        if (currentLoad >= maxCapacity) {
            this.status = CapacityStatus.FULL;
        } else if (utilizationPercent >= 80) {
            this.status = CapacityStatus.HIGH_LOAD;
        } else if (utilizationPercent >= 50) {
            this.status = CapacityStatus.MEDIUM_LOAD;
        } else {
            this.status = CapacityStatus.AVAILABLE;
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

    public String getZoneId() {
        return zoneId;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Integer getCurrentLoad() {
        return currentLoad;
    }

    public Double getUtilizationPercent() {
        return utilizationPercent;
    }

    public CapacityStatus getStatus() {
        return status;
    }

    public Instant getLastAssignment() {
        return lastAssignment;
    }

    public Integer getTotalAssignmentsToday() {
        return totalAssignmentsToday;
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

    protected void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    protected void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    protected void setCurrentLoad(Integer currentLoad) {
        this.currentLoad = currentLoad;
    }

    protected void setUtilizationPercent(Double utilizationPercent) {
        this.utilizationPercent = utilizationPercent;
    }

    protected void setStatus(CapacityStatus status) {
        this.status = status;
    }

    protected void setLastAssignment(Instant lastAssignment) {
        this.lastAssignment = lastAssignment;
    }

    protected void setTotalAssignmentsToday(Integer totalAssignmentsToday) {
        this.totalAssignmentsToday = totalAssignmentsToday;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Capacity status enum.
     */
    public enum CapacityStatus {
        AVAILABLE,
        MEDIUM_LOAD,
        HIGH_LOAD,
        FULL,
        UNAVAILABLE
    }
}
