package com.gogidix.ecommerce.cart.application.dto;

import java.math.BigDecimal;

public record CartSummaryDto(
        int itemCount,
        int totalQuantity,
        BigDecimal subtotal,
        BigDecimal discountAmount,
        BigDecimal taxAmount,
        BigDecimal shippingAmount,
        BigDecimal totalAmount
) {}
