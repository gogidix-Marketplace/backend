package com.gogidix.ecommerce.wishlist.application.query;

public record GetWishlistByIdQuery(
    String tenantId,
    String id
) {}
