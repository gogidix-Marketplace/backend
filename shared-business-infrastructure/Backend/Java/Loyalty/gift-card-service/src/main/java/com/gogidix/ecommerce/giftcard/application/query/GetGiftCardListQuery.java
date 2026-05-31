package com.gogidix.ecommerce.giftcard.application.query;

public record GetGiftCardListQuery(
    String tenantId,
    int page,
    int size
) {}
