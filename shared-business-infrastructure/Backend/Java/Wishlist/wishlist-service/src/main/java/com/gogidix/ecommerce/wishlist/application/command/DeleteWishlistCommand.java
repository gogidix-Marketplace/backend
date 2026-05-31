package com.gogidix.ecommerce.wishlist.application.command;

public record DeleteWishlistCommand(
    String tenantId,
    String id
) {}
