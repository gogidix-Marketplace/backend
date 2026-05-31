package com.gogidix.ecommerce.loyalty.shared.exception;

public class LoyaltyNotFoundException extends RuntimeException {
    private final String id;
    public LoyaltyNotFoundException(String id) { super("Loyalty not found: " + id); this.id = id; }
    public String getId() { return id; }
}
