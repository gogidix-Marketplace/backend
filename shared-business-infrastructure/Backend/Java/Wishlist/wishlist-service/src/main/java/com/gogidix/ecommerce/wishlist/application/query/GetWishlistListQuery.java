package com.gogidix.ecommerce.wishlist.application.query;

public record GetWishlistListQuery(
    String tenantId,
    int page,
    int size
) {}
