package com.gogidix.ecommerce.courier.application.dto;

public record CourierIntegrationResponse(String id, String name, boolean active) {
    public static CourierIntegrationResponse from(CourierIntegrationDto dto) {
        return new CourierIntegrationResponse(dto.id(), dto.name(), dto.active());
    }
}
