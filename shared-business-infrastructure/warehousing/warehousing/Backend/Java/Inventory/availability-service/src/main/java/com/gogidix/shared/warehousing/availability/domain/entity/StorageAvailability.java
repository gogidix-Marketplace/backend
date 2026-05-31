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
 * Storage Availability Entity
 *
 * Tracks real-time storage availability for warehouses
 * Multi-tenant with MongoDB support
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "storage_availability")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'zoneId': 1}", name = "idx_availability_tenant_warehouse")
public class StorageAvailability {

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

    private Integer totalCapacity;

    private Integer usedCapacity;

    private Integer reservedCapacity;

    private Integer availableCapacity;

    private CapacityUnit capacityUnit;

    private Double utilizationPercentage;

    private AvailabilityStatus status;

    private LocalDateTime lastCalculatedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum ZoneType {
        PALLET_STORAGE,
        SHELF_STORAGE,
        BIN_STORAGE,
        BULK_STORAGE,
        CLIMATE_CONTROLLED,
        FREEZER,
        SECURE,
        HAZMAT,
        VAULT
    }

    public enum CapacityUnit {
        SQUARE_FEET,
        SQUARE_METERS,
        CUBIC_FEET,
        CUBIC_METERS,
        PALLETS,
        BINS,
        UNITS
    }

    public enum AvailabilityStatus {
        AVAILABLE,
        LOW_AVAILABILITY,
        FULL,
        RESERVED,
        MAINTENANCE,
        UNAVAILABLE
    }

    /**
     * Calculate utilization percentage
     */
    public void calculateUtilization() {
        if (totalCapacity != null && totalCapacity > 0) {
            this.utilizationPercentage = ((double) (usedCapacity + reservedCapacity) / totalCapacity) * 100;
        } else {
            this.utilizationPercentage = 0.0;
        }
        updateStatus();
    }

    /**
     * Update status based on availability
     */
    private void updateStatus() {
        this.availableCapacity = totalCapacity - usedCapacity - reservedCapacity;

        if (availableCapacity <= 0) {
            this.status = AvailabilityStatus.FULL;
        } else if (utilizationPercentage >= 90) {
            this.status = AvailabilityStatus.LOW_AVAILABILITY;
        } else {
            this.status = AvailabilityStatus.AVAILABLE;
        }
    }
}
