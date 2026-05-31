package com.gogidix.shared.courier.ecommerce.interfaces.rest.dto;

public record CustomerDeliveryDto(
    String address,
    Double latitude,
    Double longitude,
    String contactName,
    String contactPhone
) {}
