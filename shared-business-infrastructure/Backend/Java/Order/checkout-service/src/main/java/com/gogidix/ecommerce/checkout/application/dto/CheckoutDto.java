package com.gogidix.ecommerce.checkout.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CheckoutDto(
    String id, String tenantId, String customerId, String cartId,
    Address shippingAddress, Address billingAddress,
    String paymentMethod, BigDecimal subtotal, BigDecimal tax,
    BigDecimal shipping, BigDecimal total, String status,
    Instant createdAt, Instant updatedAt
) {}
