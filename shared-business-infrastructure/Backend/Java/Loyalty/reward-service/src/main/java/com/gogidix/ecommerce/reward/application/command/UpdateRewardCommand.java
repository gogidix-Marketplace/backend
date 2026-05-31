package com.gogidix.ecommerce.reward.application.command;

public record UpdateRewardCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
