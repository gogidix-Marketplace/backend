package com.gogidix.ecommerce.reward.application.dto;

import java.time.Instant;

public record RewardResponse(
    String id,
    String name,
    String description,
    String type,
    String rewardType,
    Integer pointsCost,
    Integer redemptionCount,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
