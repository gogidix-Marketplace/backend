package com.gogidix.ecommerce.promotion.application.command;

public record UpdatePromotionCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
