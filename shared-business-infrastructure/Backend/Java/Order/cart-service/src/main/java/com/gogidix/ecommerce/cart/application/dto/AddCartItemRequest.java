package com.gogidix.ecommerce.cart.application.dto;

import java.math.BigDecimal;

public record AddCartItemRequest(
    String productId,
    String sku,
    String name,
    String imageUrl,
    BigDecimal unitPrice,
    int quantity,
    String vendorId
) {}
