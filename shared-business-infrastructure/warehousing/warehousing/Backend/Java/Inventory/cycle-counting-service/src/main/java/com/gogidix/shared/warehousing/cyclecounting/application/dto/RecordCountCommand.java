package com.gogidix.shared.warehousing.cyclecounting.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to record count
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordCountCommand {

    @NotBlank(message = "Cycle count ID is required")
    private String cycleCountId;

    @NotBlank(message = "SKU is required")
    private String sku;

    private String itemName;

    private String location;

    @NotNull(message = "System quantity is required")
    @Min(value = 0, message = "System quantity must be non-negative")
    private Integer systemQuantity;

    @NotNull(message = "Counted quantity is required")
    @Min(value = 0, message = "Counted quantity must be non-negative")
    private Integer countedQuantity;

    private java.math.BigDecimal unitValue;

    private String counterId;

    private String notes;
}
