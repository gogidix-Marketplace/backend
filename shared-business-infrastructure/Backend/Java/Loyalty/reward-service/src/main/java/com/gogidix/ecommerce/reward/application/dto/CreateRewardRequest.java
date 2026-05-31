package com.gogidix.ecommerce.reward.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRewardRequest(
    @NotBlank String name,
    String description,
    String type,
    String rewardType,
    Integer pointsCost,
    Integer redemptionCount
) {}
