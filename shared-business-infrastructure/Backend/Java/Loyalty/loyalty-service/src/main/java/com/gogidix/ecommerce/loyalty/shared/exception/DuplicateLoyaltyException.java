package com.gogidix.ecommerce.loyalty.shared.exception;

public class DuplicateLoyaltyException extends RuntimeException {
    private final String name;
    public DuplicateLoyaltyException(String name) { super("Duplicate Loyalty: " + name); this.name = name; }
    public String getName() { return name; }
}
