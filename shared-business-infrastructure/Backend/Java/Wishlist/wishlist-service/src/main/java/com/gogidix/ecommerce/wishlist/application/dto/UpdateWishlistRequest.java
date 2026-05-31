package com.gogidix.ecommerce.wishlist.application.dto;

public record UpdateWishlistRequest(
    String name,
    String description,
    String type,
    String wishlistName,
    Integer itemCount,
    Boolean isPublic,
    Boolean isActive
) {}
