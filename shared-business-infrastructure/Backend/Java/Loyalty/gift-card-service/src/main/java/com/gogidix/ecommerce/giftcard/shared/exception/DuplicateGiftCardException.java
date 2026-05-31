package com.gogidix.ecommerce.giftcard.shared.exception;

public class DuplicateGiftCardException extends RuntimeException {
    private final String name;
    public DuplicateGiftCardException(String name) { super("Duplicate GiftCard: " + name); this.name = name; }
    public String getName() { return name; }
}
