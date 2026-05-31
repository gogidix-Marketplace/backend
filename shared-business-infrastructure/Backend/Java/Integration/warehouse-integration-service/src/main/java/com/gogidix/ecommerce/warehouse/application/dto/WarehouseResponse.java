package com.gogidix.ecommerce.warehouse.application.dto;

import java.time.Instant;

public record WarehouseResponse(
    String id,
    String name,
    String description,
    String type,
    String warehouseCode,
    String location,
    Integer capacity,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
