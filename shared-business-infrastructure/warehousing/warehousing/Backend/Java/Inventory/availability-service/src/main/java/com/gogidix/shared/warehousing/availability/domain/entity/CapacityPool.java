package com.gogidix.shared.warehousing.availability.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Capacity Pool Entity
 *
 * Represents a pool of capacity that can be reserved
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "capacity_pools")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'poolType': 1}", name = "idx_pool_tenant_type")
public class CapacityPool {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String poolName;

    private PoolType poolType;

    private Integer totalCapacity;

    private Integer allocatedCapacity;

    private Integer availableCapacity;

    private Integer reservedCapacity;

    private CapacityUnit capacityUnit;

    private Double reservationThreshold;

    private Boolean overflowEnabled;

    private Integer overflowCapacity;

    private LocalDateTime lastReservedAt;

    private LocalDateTime lastReleasedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum PoolType {
        PICKING,
        RECEIVING,
        STAGING,
        SHIPPING,
        STORAGE,
        TEMPORARY
    }

    public enum CapacityUnit {
        SQUARE_FEET,
        SQUARE_METERS,
        PALLETS,
        BINS,
        UNITS,
        KILOGRAMS,
        VOLUME
    }

    /**
     * Reserve capacity from pool
     */
    public boolean reserveCapacity(Integer quantity) {
        Integer available = totalCapacity - allocatedCapacity - reservedCapacity;
        if (available >= quantity) {
            this.reservedCapacity += quantity;
            this.availableCapacity = totalCapacity - allocatedCapacity - reservedCapacity;
            this.lastReservedAt = LocalDateTime.now();
            return true;
        }
        // Check overflow
        if (overflowEnabled && overflowCapacity != null && overflowCapacity >= quantity - available) {
            this.reservedCapacity += quantity;
            this.overflowCapacity -= (quantity - available);
            this.availableCapacity = totalCapacity - allocatedCapacity - reservedCapacity;
            this.lastReservedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

    /**
     * Release capacity back to pool
     */
    public void releaseCapacity(Integer quantity) {
        this.reservedCapacity = Math.max(0, reservedCapacity - quantity);
        this.availableCapacity = totalCapacity - allocatedCapacity - reservedCapacity;
        this.lastReleasedAt = LocalDateTime.now();
    }

    /**
     * Allocate capacity (confirm reservation)
     */
    public boolean allocateCapacity(Integer quantity) {
        if (reservedCapacity >= quantity) {
            this.reservedCapacity -= quantity;
            this.allocatedCapacity += quantity;
            this.availableCapacity = totalCapacity - allocatedCapacity - reservedCapacity;
            return true;
        }
        return false;
    }

    /**
     * Calculate utilization percentage
     */
    public Double calculateUtilization() {
        if (totalCapacity != null && totalCapacity > 0) {
            return ((double) (allocatedCapacity + reservedCapacity) / totalCapacity) * 100;
        }
        return 0.0;
    }
}
