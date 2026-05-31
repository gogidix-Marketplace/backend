package com.gogidix.shared.warehousing.stock.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Command to allocate stock for an order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllocateStockCommand {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotBlank(message = "Warehouse ID is required")
    private String warehouseId;

    @NotEmpty(message = "At least one item is required")
    private List<OrderItem> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {

        @NotBlank(message = "SKU is required")
        private String sku;

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        private Integer quantity;

        private String batchNumber;  // Optional: specific batch to allocate
    }
}
