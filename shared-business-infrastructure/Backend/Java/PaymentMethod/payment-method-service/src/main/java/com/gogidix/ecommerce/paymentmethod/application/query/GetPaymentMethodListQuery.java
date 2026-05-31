package com.gogidix.ecommerce.paymentmethod.application.query;

public record GetPaymentMethodListQuery(String tenantId, int page, int size) {
}