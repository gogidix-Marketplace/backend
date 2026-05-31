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
 * Zone Capacity Entity
 *
 * Tracks capacity of warehouse zones
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "zone_capacities")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'zoneId': 1}", name = "idx_zone_capacity_tenant")
public class ZoneCapacity {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String zoneId;

    private String zoneName;

    private ZoneType zoneType;

    private BigDecimal totalVolume;

    private BigDecimal usedVolume;

    private BigDecimal availableVolume;

    private Integer totalPositions;

    private Integer usedPositions;

    private Integer availablePositions;

    private BigDecimal maxWeight;

    private BigDecimal usedWeight;

    private BigDecimal availableWeight;

    private Double utilizationPercentage;

    private CapacityStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum ZoneType {
        RECEIVING,
        PUTAWAY,
        STORAGE,
        PICKING,
        PACKING,
        SHIPPING,
        STAGING,
        RETURNS,
        DAMAGED_GOODS,
        CLIMATE_CONTROLLED,
        HIGH_SECURITY
    }

    public enum CapacityStatus {
        AVAILABLE,
        LOW_CAPACITY,
        AT_CAPACITY,
        OVER_CAPACITY,
        MAINTENANCE
    }

    /**
     * Update capacity after allocation
     */
    public void allocateCapacity(java.math.BigDecimal volume, java.math.BigDecimal weight) {
        this.usedVolume = this.usedVolume.add(volume);
        this.usedWeight = this.usedWeight.add(weight);
        this.availableVolume = this.totalVolume.subtract(this.usedVolume);
        this.availableWeight = this.maxWeight.subtract(this.usedWeight);
        calculateUtilization();
        updateStatus();
    }

    /**
     * Update capacity after release
     */
    public void releaseCapacity(java.math.BigDecimal volume, java.math.BigDecimal weight) {
        this.usedVolume = this.usedVolume.subtract(volume);
        this.usedWeight = this.usedWeight.subtract(weight);
        this.availableVolume = this.totalVolume.subtract(this.usedVolume);
        this.availableWeight = this.maxWeight.subtract(this.usedWeight);
        calculateUtilization();
        updateStatus();
    }

    /**
     * Calculate utilization percentage
     */
    private void calculateUtilization() {
        if (totalVolume != null && totalVolume.compareTo(java.math.BigDecimal.ZERO) > 0) {
            this.utilizationPercentage = usedVolume.divide(totalVolume, 2, java.math.RoundingMode.HALF_UP)
                .doubleValue() * 100;
        }
    }

    /**
     * Update status based on utilization
     */
    private void updateStatus() {
        if (utilizationPercentage >= 100) {
            this.status = CapacityStatus.AT_CAPACITY;
        } else if (utilizationPercentage >= 90) {
            this.status = CapacityStatus.LOW_CAPACITY;
        } else {
            this.status = CapacityStatus.AVAILABLE;
        }
    }

    /**
     * Check if zone has capacity for item
     */
    public boolean hasCapacityFor(java.math.BigDecimal volume, java.math.BigDecimal weight) {
        return availableVolume.compareTo(volume) >= 0 &&
               (maxWeight == null || availableWeight.compareTo(weight) >= 0);
    }
}
