package com.gogidix.ecommerce.paymentmethod.shared.exception;

public class DuplicatePaymentMethodException extends RuntimeException {
    private final String name;
    public DuplicatePaymentMethodException(String name) { super("Duplicate PaymentMethod: " + name); this.name = name; }
    public String getName() { return name; }
}
