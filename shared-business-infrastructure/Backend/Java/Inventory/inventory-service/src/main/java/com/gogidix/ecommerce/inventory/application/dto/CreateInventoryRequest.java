package com.gogidix.ecommerce.inventory.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateInventoryRequest(
    @NotBlank String name,
    String description,
    String type,
    String productId,
    String sku,
    Integer quantity
) {}
