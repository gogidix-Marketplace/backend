package com.gogidix.ecommerce.giftcard.application.command;

public record UpdateGiftCardCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
