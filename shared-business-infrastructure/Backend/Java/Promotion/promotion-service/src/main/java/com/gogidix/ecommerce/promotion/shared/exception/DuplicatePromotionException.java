package com.gogidix.ecommerce.promotion.shared.exception;

public class DuplicatePromotionException extends RuntimeException {
    private final String name;
    public DuplicatePromotionException(String name) { super("Duplicate Promotion: " + name); this.name = name; }
    public String getName() { return name; }
}
