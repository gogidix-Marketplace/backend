package com.gogidix.shared.warehousing.availability.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to reserve storage capacity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveCapacityCommand {

    @NotBlank(message = "Warehouse ID is required")
    private String warehouseId;

    private String zoneId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String poolId;

    private String reservationId;

    private String referenceType;

    private String referenceId;

    private Integer priority;
}
