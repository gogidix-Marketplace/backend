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

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockRegistrationRequest {

    @NotBlank(message = "vendorId is required")
    private String vendorId;

    @NotBlank(message = "warehouseId is required")
    private String warehouseId;

    @NotBlank(message = "zoneId is required")
    private String zoneId;

    @NotEmpty(message = "items cannot be empty")
    @Valid
    private List<StockItem> items;

    private String sellingRadius;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockItem {
        @NotBlank(message = "sku is required")
        private String sku;
        @NotBlank(message = "productName is required")
        private String productName;
        @NotNull(message = "quantity is required")
        @Positive(message = "quantity must be positive")
        private Integer quantity;
        private String unitOfMeasure;
        private String category;
        private Double weight;
        private String weightUnit;
        private Dimensions dimensions;
        private Map<String, String> attributes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dimensions {
        private Double length;
        private Double width;
        private Double height;
    }
}
