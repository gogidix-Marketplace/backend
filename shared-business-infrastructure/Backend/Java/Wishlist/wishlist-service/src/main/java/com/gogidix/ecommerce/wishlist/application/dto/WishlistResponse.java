package com.gogidix.ecommerce.wishlist.application.dto;

import java.time.Instant;

public record WishlistResponse(
    String id,
    String name,
    String description,
    String type,
    String wishlistName,
    Integer itemCount,
    Boolean isPublic,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
