package com.gogidix.shared.warehousing.fulfillment.application.dto;

import com.gogidix.shared.warehousing.fulfillment.domain.entity.FulfillmentOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * DTO for creating a new fulfillment order
 */
@Data
public class CreateOrderRequest {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    private String warehouseId;

    @NotNull(message = "Items are required")
    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<OrderItemDto> items;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    private String shippingMethod = "STANDARD";
    private String priority = "NORMAL";

    @Data
    public static class OrderItemDto {
        @NotBlank(message = "SKU is required")
        private String sku;

        private String productName;

        @NotNull(message = "Quantity is required")
        private Integer quantity;

        private Double weight;
        private Double volume;
        private Double unitPrice;
    }
}
