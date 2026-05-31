package com.gogidix.ecommerce.airfreight.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAirFreightRequest(
    @NotBlank String name,
    String description,
    String type,
    String carrierCode,
    String origin,
    String destination
) {}
