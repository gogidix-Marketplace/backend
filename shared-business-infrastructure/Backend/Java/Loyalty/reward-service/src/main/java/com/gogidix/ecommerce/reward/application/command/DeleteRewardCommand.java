package com.gogidix.ecommerce.reward.application.command;

public record DeleteRewardCommand(
    String tenantId,
    String id
) {}
