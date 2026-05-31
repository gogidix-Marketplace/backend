package com.gogidix.ecommerce.oceanshipping.application.dto;

public record UpdateOceanShippingRequest(
    String name,
    String description,
    String type,
    String carrierName,
    String shippingRoute,
    String estimatedTransitTime,
    Boolean isActive
) {}
