package com.gogidix.ecommerce.wishlist.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateWishlistRequest(
    @NotBlank String name,
    String description,
    String type,
    String wishlistName,
    Integer itemCount,
    Boolean isPublic
) {}
