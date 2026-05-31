package com.gogidix.shared.warehousing.stock.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to create initial stock for a SKU at a location
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStockCommand {

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Location ID is required")
    private String locationId;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be non-negative")
    private Integer quantity;

    @Min(value = 0, message = "Reorder point must be non-negative")
    private Integer reorderPoint;

    @Min(value = 1, message = "Reorder quantity must be positive")
    private Integer reorderQuantity;
}
