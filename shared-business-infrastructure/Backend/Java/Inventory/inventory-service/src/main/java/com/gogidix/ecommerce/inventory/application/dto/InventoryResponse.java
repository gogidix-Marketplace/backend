package com.gogidix.ecommerce.inventory.application.dto;

import java.time.Instant;

public record InventoryResponse(
    String id,
    String name,
    String description,
    String type,
    String productId,
    String sku,
    Integer quantity,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
