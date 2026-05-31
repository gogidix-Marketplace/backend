package com.gogidix.ecommerce.promotion.shared.exception;

public class PromotionNotFoundException extends RuntimeException {
    private final String id;
    public PromotionNotFoundException(String id) { super("Promotion not found: " + id); this.id = id; }
    public String getId() { return id; }
}
