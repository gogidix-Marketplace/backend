package com.gogidix.ecommerce.wishlist.shared.exception;

public class WishlistNotFoundException extends RuntimeException {
    private final String id;
    public WishlistNotFoundException(String id) { super("Wishlist not found: " + id); this.id = id; }
    public String getId() { return id; }
}
