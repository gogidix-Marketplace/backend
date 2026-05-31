package com.gogidix.shared.warehousing.ecommerce.interfaces.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EcommerceFulfillmentRequest {

    @NotBlank(message = "orderId is required")
    private String orderId;

    @NotBlank(message = "subOrderId is required")
    private String subOrderId;

    @NotBlank(message = "vendorId is required")
    private String vendorId;

    @NotBlank(message = "warehouseId is required")
    private String warehouseId;

    @NotEmpty(message = "items cannot be empty")
    @Valid
    private List<OrderItem> items;

    private String priority;
    private LocalDateTime deliveryDeadline;
    private String deliveryType;

    @Valid
    private CustomerAddress customerAddress;

    private String specialInstructions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        @NotBlank(message = "sku is required")
        private String sku;
        @NotBlank(message = "productName is required")
        private String productName;
        @NotNull(message = "quantity is required")
        @Positive(message = "quantity must be positive")
        private Integer quantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerAddress {
        private String address;
        private Double latitude;
        private Double longitude;
    }
}
