package com.gogidix.ecommerce.wishlist.application.command;

public record CreateWishlistCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
