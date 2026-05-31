package com.gogidix.ecommerce.giftcard.application.command;

public record CreateGiftCardCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
