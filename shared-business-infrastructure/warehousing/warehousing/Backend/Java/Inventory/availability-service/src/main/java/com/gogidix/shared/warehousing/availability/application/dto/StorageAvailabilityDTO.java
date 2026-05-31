package com.gogidix.shared.warehousing.availability.application.dto;

import com.gogidix.shared.warehousing.availability.domain.entity.StorageAvailability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Storage Availability
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageAvailabilityDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String zoneId;
    private String zoneName;
    private StorageAvailability.ZoneType zoneType;
    private Integer totalCapacity;
    private Integer usedCapacity;
    private Integer reservedCapacity;
    private Integer availableCapacity;
    private StorageAvailability.CapacityUnit capacityUnit;
    private Double utilizationPercentage;
    private StorageAvailability.AvailabilityStatus status;
    private LocalDateTime lastCalculatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
