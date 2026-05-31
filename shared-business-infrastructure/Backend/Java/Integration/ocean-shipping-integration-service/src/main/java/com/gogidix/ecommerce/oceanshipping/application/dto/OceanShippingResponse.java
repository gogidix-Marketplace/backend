package com.gogidix.ecommerce.oceanshipping.application.dto;

import java.time.Instant;

public record OceanShippingResponse(
    String id,
    String name,
    String description,
    String type,
    String carrierName,
    String shippingRoute,
    String estimatedTransitTime,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
