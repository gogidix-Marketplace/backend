package com.gogidix.ecommerce.giftcard.shared.exception;

public class GiftCardNotFoundException extends RuntimeException {
    private final String id;
    public GiftCardNotFoundException(String id) { super("GiftCard not found: " + id); this.id = id; }
    public String getId() { return id; }
}
