package com.gogidix.shared.warehousing.spaceallocation.domain.entity;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Space Allocation Entity
 *
 * Represents allocated space for items in warehouse
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "space_allocations")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'zoneId': 1}", name = "idx_allocation_tenant_warehouse")
public class SpaceAllocation {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String zoneId;

    private String allocationId;

    private String itemId;

    private String itemType;

    private String sku;

    @Indexed
    private AllocationStatus status;

    private SpaceType spaceType;

    private BigDecimal volume;

    private BigDecimal weight;

    private Integer quantity;

    private SpaceUnit unit;

    private Location location;

    private String assignedTo;

    private LocalDateTime allocatedAt;

    private LocalDateTime expiresAt;

    private Integer priority;

    private AllocationType allocationType;

    private String referenceId;

    private String referenceType;

    private BigDecimal utilizationPercentage;

    private Boolean optimized;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum AllocationStatus {
        PENDING,
        ALLOCATED,
        OCCUPIED,
        RELEASED,
        EXPIRED,
        CANCELLED
    }

    public enum SpaceType {
        PALLET,
        SHELF,
        BIN,
        BULK,
        HANGING,
        FLOOR,
        MEZZANINE
    }

    public enum SpaceUnit {
        CUBIC_METERS,
        CUBIC_FEET,
        SQUARE_METERS,
        SQUARE_FEET,
        UNITS
    }

    public enum AllocationType {
        PERMANENT,
        TEMPORARY,
        FLOATING,
        DYNAMIC,
        RESERVED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private String zone;
        private String aisle;
        private String bay;
        private String level;
        private String position;
        private String barcode;
    }

    /**
     * Mark as allocated
     */
    public void markAllocated() {
        this.status = AllocationStatus.ALLOCATED;
        this.allocatedAt = LocalDateTime.now();
    }

    /**
     * Mark as occupied
     */
    public void markOccupied() {
        this.status = AllocationStatus.OCCUPIED;
    }

    /**
     * Release allocation
     */
    public void release() {
        this.status = AllocationStatus.RELEASED;
    }

    /**
     * Check if allocation is expired
     */
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt) &&
               (status == AllocationStatus.ALLOCATED || status == AllocationStatus.OCCUPIED);
    }

    /**
     * Calculate utilization
     */
    public void calculateUtilization(BigDecimal maxVolume) {
        if (maxVolume != null && maxVolume.compareTo(BigDecimal.ZERO) > 0) {
            this.utilizationPercentage = volume.divide(maxVolume, 2, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        }
    }
}
