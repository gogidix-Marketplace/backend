package com.gogidix.shared.warehousing.availability.application.command;

import com.gogidix.shared.warehousing.availability.domain.entity.AvailabilitySlot;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Command to check availability
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckAvailabilityCommand {

    @NotBlank(message = "Warehouse ID is required")
    private String warehouseId;

    private String zoneId;

    private String poolId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private AvailabilitySlot.SlotType slotType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
