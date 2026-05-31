package com.gogidix.ecommerce.paymentmethod.application.command;

public record UpdatePaymentMethodCommand(String tenantId, String id, String name) {
}