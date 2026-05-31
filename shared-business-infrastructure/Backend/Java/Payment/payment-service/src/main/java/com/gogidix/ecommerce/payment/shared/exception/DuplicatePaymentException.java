package com.gogidix.ecommerce.payment.shared.exception;

public class DuplicatePaymentException extends RuntimeException {
    private final String name;
    public DuplicatePaymentException(String name) { super("Duplicate Payment: " + name); this.name = name; }
    public String getName() { return name; }
}
