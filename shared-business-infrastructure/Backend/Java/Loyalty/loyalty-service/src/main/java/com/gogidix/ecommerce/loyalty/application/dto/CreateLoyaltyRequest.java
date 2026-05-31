package com.gogidix.ecommerce.loyalty.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateLoyaltyRequest(
    @NotBlank String name,
    String description,
    String type,
    Integer pointsBalance,
    String tierLevel,
    String memberSince
) {}
