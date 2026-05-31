package com.gogidix.ecommerce.fulfillment.oceanshipping.application.dto;

public record OceanShippingFulfillmentResponse(String id, String name, boolean active) {
    public static OceanShippingFulfillmentResponse from(OceanShippingFulfillmentDto dto) {
        return new OceanShippingFulfillmentResponse(dto.id(), dto.name(), dto.active());
    }
}
