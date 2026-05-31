package com.gogidix.ecommerce.oceanshipping.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOceanShippingRequest(
    @NotBlank String name,
    String description,
    String type,
    String carrierName,
    String shippingRoute,
    String estimatedTransitTime
) {}
