package com.gogidix.ecommerce.promotion.application.command;

public record CreatePromotionCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
