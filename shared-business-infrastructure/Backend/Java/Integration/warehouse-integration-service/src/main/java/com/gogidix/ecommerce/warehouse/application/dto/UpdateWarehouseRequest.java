package com.gogidix.ecommerce.warehouse.application.dto;

public record UpdateWarehouseRequest(
    String name,
    String description,
    String type,
    String warehouseCode,
    String location,
    Integer capacity,
    Boolean isActive
) {}
