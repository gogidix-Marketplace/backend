package com.gogidix.shared.warehousing.spaceallocation.application.dto;

import com.gogidix.shared.warehousing.spaceallocation.domain.entity.SpaceAllocation;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Command to allocate space
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllocateSpaceCommand {

    @NotBlank(message = "Warehouse ID is required")
    private String warehouseId;

    private String zoneId;

    private String itemId;

    private String itemType;

    private String sku;

    @NotNull(message = "Space type is required")
    private SpaceAllocation.SpaceType spaceType;

    @NotNull(message = "Volume is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Volume must be positive")
    private BigDecimal volume;

    @DecimalMin(value = "0.0", inclusive = true, message = "Weight must be positive")
    private BigDecimal weight;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private SpaceAllocation.SpaceUnit unit;

    @NotNull(message = "Allocation type is required")
    private SpaceAllocation.AllocationType allocationType;

    private LocalDateTime expiresAt;

    private Integer priority;

    private String referenceId;

    private String referenceType;

    private String assignedTo;
}
