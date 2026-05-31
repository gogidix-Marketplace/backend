package com.gogidix.ecommerce.paymentgateway.application.command;

public record CreatePaymentGatewayCommand(String tenantId, String name) {
}