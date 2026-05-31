package com.gogidix.shared.warehousing.availability.application.dto;

import com.gogidix.shared.warehousing.availability.domain.entity.CapacityPool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Capacity Pool
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CapacityPoolDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String poolName;
    private CapacityPool.PoolType poolType;
    private Integer totalCapacity;
    private Integer allocatedCapacity;
    private Integer availableCapacity;
    private Integer reservedCapacity;
    private CapacityPool.CapacityUnit capacityUnit;
    private Double reservationThreshold;
    private Boolean overflowEnabled;
    private Integer overflowCapacity;
    private Double utilizationPercentage;
    private LocalDateTime lastReservedAt;
    private LocalDateTime lastReleasedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
