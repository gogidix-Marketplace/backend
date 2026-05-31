package com.gogidix.ecommerce.fulfillment.airfreight.application.dto;
public record AirFreightFulfillmentResponse(String id, String name, boolean active) {
    public static AirFreightFulfillmentResponse from(AirFreightFulfillmentDto dto) {
        return new AirFreightFulfillmentResponse(dto.id(), dto.name(), dto.active());
    }
}
