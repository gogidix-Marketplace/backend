package com.gogidix.ecommerce.giftcard.application.command;

public record DeleteGiftCardCommand(
    String tenantId,
    String id
) {}
