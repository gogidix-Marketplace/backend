package com.gogidix.ecommerce.promotion.application.command;

public record DeletePromotionCommand(
    String tenantId,
    String id
) {}
