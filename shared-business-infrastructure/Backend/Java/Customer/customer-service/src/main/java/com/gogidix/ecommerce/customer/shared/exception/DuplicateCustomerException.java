package com.gogidix.ecommerce.customer.shared.exception;

public class DuplicateCustomerException extends RuntimeException {
    private final String name;
    public DuplicateCustomerException(String name) { super("Duplicate Customer: " + name); this.name = name; }
    public String getName() { return name; }
}
