package com.gogidix.ecommerce.cart.application.dto;

public record UpdateCartItemRequest(
    String productId,
    int quantity
) {}
