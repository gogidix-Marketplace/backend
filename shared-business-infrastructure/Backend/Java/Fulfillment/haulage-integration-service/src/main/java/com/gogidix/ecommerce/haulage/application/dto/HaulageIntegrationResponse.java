package com.gogidix.ecommerce.haulage.application.dto;

public record HaulageIntegrationResponse(String id, String name, boolean active) {
    public static HaulageIntegrationResponse from(HaulageIntegrationDto dto) {
        return new HaulageIntegrationResponse(dto.id(), dto.name(), dto.active());
    }
}
