package com.gogidix.shared.warehousing.storage.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Storage Space Entity
 *
 * Represents a physical storage space within a warehouse facility.
 * Supports multiple storage types with capacity tracking and real-time availability.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "storage_spaces")
@CompoundIndex(def = "{'tenantId': 1, 'spaceCode': 1}", unique = true)
@CompoundIndex(def = "{'tenantId': 1, 'status': 1, 'spaceType': 1}")
@CompoundIndex(def = "{'tenantId': 1, 'facilityZone': 1}")
public class StorageSpace {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String spaceCode; // Unique within tenant

    private String spaceType; // GENERAL, CLIMATE_CONTROLLED, BONDED, HAZARDOUS

    // Dimensions
    private Double lengthMeters;
    private Double widthMeters;
    private Double heightMeters;
    private Double totalCapacityCubicMeters;

    // Availability
    private Double availableCapacityCubicMeters;
    private Integer availableSlots;

    // Pricing
    private BigDecimal basePricePerDay;
    private String currency;

    // Location within facility
    private String facilityZone;
    private String shelfLevel;
    private String binNumber;

    private String status; // AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Calculate utilization percentage
     */
    public double getUtilizationPercentage() {
        if (totalCapacityCubicMeters == null || totalCapacityCubicMeters == 0) {
            return 0.0;
        }
        double used = totalCapacityCubicMeters - (availableCapacityCubicMeters != null ? availableCapacityCubicMeters : 0.0);
        return (used / totalCapacityCubicMeters) * 100;
    }

    /**
     * Check if space is available for allocation
     */
    public boolean isAvailable() {
        return "AVAILABLE".equals(status) &&
               availableCapacityCubicMeters != null &&
               availableCapacityCubicMeters > 0;
    }
}
