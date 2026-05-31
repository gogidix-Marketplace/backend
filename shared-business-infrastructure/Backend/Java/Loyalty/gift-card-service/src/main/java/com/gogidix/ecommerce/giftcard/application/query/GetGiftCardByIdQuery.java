package com.gogidix.ecommerce.giftcard.application.query;

public record GetGiftCardByIdQuery(
    String tenantId,
    String id
) {}
