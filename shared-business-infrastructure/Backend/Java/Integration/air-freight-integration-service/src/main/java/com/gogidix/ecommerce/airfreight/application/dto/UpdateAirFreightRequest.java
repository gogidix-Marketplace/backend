package com.gogidix.ecommerce.airfreight.application.dto;

public record UpdateAirFreightRequest(
    String name,
    String description,
    String type,
    String carrierCode,
    String origin,
    String destination,
    Boolean isActive
) {}
