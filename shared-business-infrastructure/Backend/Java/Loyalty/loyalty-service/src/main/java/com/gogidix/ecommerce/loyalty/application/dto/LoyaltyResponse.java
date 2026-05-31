package com.gogidix.ecommerce.loyalty.application.dto;

import java.time.Instant;

public record LoyaltyResponse(
    String id,
    String name,
    String description,
    String type,
    Integer pointsBalance,
    String tierLevel,
    String memberSince,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
