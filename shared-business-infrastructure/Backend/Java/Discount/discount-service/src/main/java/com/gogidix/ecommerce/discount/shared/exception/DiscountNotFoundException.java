package com.gogidix.ecommerce.discount.shared.exception;

public class DiscountNotFoundException extends RuntimeException {
    private final String id;
    public DiscountNotFoundException(String id) { super("Discount not found: " + id); this.id = id; }
    public String getId() { return id; }
}
