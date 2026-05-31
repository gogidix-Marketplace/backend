package com.gogidix.ecommerce.customer.shared.exception;

public class CustomerNotFoundException extends RuntimeException {
    private final String id;
    public CustomerNotFoundException(String id) { super("Customer not found: " + id); this.id = id; }
    public String getId() { return id; }
}
