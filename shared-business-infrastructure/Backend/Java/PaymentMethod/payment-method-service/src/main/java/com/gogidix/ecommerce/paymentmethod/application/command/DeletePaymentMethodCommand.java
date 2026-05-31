package com.gogidix.ecommerce.paymentmethod.application.command;

public record DeletePaymentMethodCommand(String tenantId, String id) {
}