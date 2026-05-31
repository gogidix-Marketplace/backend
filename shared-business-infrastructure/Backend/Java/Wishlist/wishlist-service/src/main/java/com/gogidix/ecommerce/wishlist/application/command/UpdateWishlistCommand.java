package com.gogidix.ecommerce.wishlist.application.command;

public record UpdateWishlistCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
