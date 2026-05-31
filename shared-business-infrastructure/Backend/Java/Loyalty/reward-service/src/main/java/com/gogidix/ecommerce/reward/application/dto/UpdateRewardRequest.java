package com.gogidix.ecommerce.reward.application.dto;

public record UpdateRewardRequest(
    String name,
    String description,
    String type,
    String rewardType,
    Integer pointsCost,
    Integer redemptionCount,
    Boolean isActive
) {}
