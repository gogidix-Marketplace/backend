package com.gogidix.ecommerce.checkout.application.dto;

public record CreateCheckoutRequest(
    String cartId, Address shippingAddress, Address billingAddress,
    String paymentMethod
) {}
