package com.gogidix.ecommerce.reward.application.command;

public record CreateRewardCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
