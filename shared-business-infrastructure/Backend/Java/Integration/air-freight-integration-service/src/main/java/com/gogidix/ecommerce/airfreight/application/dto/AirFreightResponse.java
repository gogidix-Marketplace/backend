package com.gogidix.ecommerce.airfreight.application.dto;

import java.time.Instant;

public record AirFreightResponse(
    String id,
    String name,
    String description,
    String type,
    String carrierCode,
    String origin,
    String destination,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
