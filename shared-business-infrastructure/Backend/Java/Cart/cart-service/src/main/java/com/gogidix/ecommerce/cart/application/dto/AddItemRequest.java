package com.gogidix.ecommerce.cart.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public record AddItemRequest(
        @NotBlank String sku,
        @NotBlank String productId,
        @NotBlank String productName,
        @NotNull @Min(1) int quantity,
        @NotNull BigDecimal unitPrice,
        String currency,
        Map<String, String> attributes
) {}
