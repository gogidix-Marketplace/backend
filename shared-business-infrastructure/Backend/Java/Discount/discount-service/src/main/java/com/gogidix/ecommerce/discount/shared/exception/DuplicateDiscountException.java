package com.gogidix.ecommerce.discount.shared.exception;

public class DuplicateDiscountException extends RuntimeException {
    private final String name;
    public DuplicateDiscountException(String name) { super("Duplicate Discount: " + name); this.name = name; }
    public String getName() { return name; }
}
