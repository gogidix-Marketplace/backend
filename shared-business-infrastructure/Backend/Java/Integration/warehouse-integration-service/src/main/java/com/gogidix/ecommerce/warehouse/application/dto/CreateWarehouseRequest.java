package com.gogidix.ecommerce.warehouse.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateWarehouseRequest(
    @NotBlank String name,
    String description,
    String type,
    String warehouseCode,
    String location,
    Integer capacity
) {}
