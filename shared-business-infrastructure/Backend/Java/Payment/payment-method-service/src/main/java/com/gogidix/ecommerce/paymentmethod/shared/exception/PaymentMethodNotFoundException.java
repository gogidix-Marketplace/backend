package com.gogidix.ecommerce.paymentmethod.shared.exception;

public class PaymentMethodNotFoundException extends RuntimeException {
    private final String id;
    public PaymentMethodNotFoundException(String id) { super("PaymentMethod not found: " + id); this.id = id; }
    public String getId() { return id; }
}
