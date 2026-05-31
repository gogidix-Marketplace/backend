package com.gogidix.ecommerce.payment.shared.exception;

public class PaymentNotFoundException extends RuntimeException {
    private final String id;
    public PaymentNotFoundException(String id) { super("Payment not found: " + id); this.id = id; }
    public String getId() { return id; }
}
