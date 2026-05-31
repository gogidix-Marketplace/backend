package com.gogidix.ecommerce.loyalty.application.dto;

public record UpdateLoyaltyRequest(
    String name,
    String description,
    String type,
    Integer pointsBalance,
    String tierLevel,
    String memberSince,
    Boolean isActive
) {}
