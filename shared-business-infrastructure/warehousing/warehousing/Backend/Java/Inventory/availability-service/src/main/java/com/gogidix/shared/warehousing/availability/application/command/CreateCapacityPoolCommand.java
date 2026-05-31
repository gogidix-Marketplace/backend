package com.gogidix.shared.warehousing.availability.application.command;

import com.gogidix.shared.warehousing.availability.domain.entity.CapacityPool;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to create a capacity pool
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCapacityPoolCommand {

    @NotBlank(message = "Warehouse ID is required")
    private String warehouseId;

    @NotBlank(message = "Pool name is required")
    private String poolName;

    @NotNull(message = "Pool type is required")
    private CapacityPool.PoolType poolType;

    @NotNull(message = "Total capacity is required")
    @Min(value = 1, message = "Total capacity must be at least 1")
    private Integer totalCapacity;

    @NotNull(message = "Capacity unit is required")
    private CapacityPool.CapacityUnit capacityUnit;

    private Double reservationThreshold;

    @Builder.Default
    private Boolean overflowEnabled = false;

    private Integer overflowCapacity;
}
