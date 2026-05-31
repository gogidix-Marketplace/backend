package com.gogidix.ecommerce.paymentgateway.application.query;

public record GetPaymentGatewayListQuery(String tenantId, int page, int size) {
}