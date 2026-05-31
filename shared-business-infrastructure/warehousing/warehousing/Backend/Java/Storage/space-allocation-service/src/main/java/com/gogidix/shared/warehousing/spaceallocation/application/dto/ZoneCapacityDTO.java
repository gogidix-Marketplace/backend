package com.gogidix.shared.warehousing.spaceallocation.application.dto;

import com.gogidix.shared.warehousing.spaceallocation.domain.entity.ZoneCapacity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Zone Capacity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneCapacityDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String zoneId;
    private String zoneName;
    private ZoneCapacity.ZoneType zoneType;
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
    private ZoneCapacity.CapacityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
