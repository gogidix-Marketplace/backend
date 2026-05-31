package com.gogidix.shared.warehousing.spaceallocation.application.dto;

import com.gogidix.shared.warehousing.spaceallocation.domain.entity.SpaceAllocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Space Allocation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpaceAllocationDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String zoneId;
    private String allocationId;
    private String itemId;
    private String itemType;
    private String sku;
    private SpaceAllocation.AllocationStatus status;
    private SpaceAllocation.SpaceType spaceType;
    private BigDecimal volume;
    private BigDecimal weight;
    private Integer quantity;
    private SpaceAllocation.SpaceUnit unit;
    private SpaceAllocation.Location location;
    private String assignedTo;
    private LocalDateTime allocatedAt;
    private LocalDateTime expiresAt;
    private Integer priority;
    private SpaceAllocation.AllocationType allocationType;
    private String referenceId;
    private String referenceType;
    private BigDecimal utilizationPercentage;
    private Boolean optimized;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
