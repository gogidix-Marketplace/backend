package com.gogidix.ecommerce.cart.application.dto;

import java.math.BigDecimal;

public record CartItemDto(
    String itemId,
    String productId,
    String sku,
    String name,
    String imageUrl,
    BigDecimal unitPrice,
    int quantity,
    BigDecimal lineTotal,
    String vendorId,
    Boolean inStock
) {}
