package com.gogidix.ecommerce.fulfillment.warehouse.application.dto;

public record WarehouseFulfillmentResponse(String id, String name, boolean active) {
    public static WarehouseFulfillmentResponse from(WarehouseFulfillmentDto dto) {
        return new WarehouseFulfillmentResponse(dto.id(), dto.name(), dto.active());
    }
}
