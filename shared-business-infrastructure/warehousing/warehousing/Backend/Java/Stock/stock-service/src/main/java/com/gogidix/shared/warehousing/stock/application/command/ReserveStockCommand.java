package com.gogidix.shared.warehousing.stock.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to reserve stock for an order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveStockCommand {

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Location ID is required")
    private String locationId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be positive")
    private Integer quantity;

    @NotBlank(message = "Order ID is required")
    private String orderId;

    private String reservedBy;
}
