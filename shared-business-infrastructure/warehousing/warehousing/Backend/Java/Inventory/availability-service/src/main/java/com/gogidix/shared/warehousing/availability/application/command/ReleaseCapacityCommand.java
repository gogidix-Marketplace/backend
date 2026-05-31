package com.gogidix.shared.warehousing.availability.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to release storage capacity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseCapacityCommand {

    @NotBlank(message = "Reservation ID is required")
    private String reservationId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String warehouseId;

    private String zoneId;

    private String poolId;
}
