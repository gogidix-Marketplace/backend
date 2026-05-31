package com.gogidix.ecommerce.reward.application.query;

public record GetRewardListQuery(
    String tenantId,
    int page,
    int size
) {}
