package com.gogidix.ecommerce.reward.application.query;

public record GetRewardByIdQuery(
    String tenantId,
    String id
) {}
