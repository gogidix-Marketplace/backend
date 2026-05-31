package com.gogidix.ecommerce.paymentmethod.application.command;

public record CreatePaymentMethodCommand(String tenantId, String name) {
}