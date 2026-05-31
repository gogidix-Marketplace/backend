package com.gogidix.ecommerce.inventory.application.dto;

public record UpdateInventoryRequest(
    String name,
    String description,
    String type,
    String productId,
    String sku,
    Integer quantity,
    Boolean isActive
) {}
