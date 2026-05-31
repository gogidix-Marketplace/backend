package com.gogidix.ecommerce.wishlist.shared.exception;

public class DuplicateWishlistException extends RuntimeException {
    private final String name;
    public DuplicateWishlistException(String name) { super("Duplicate Wishlist: " + name); this.name = name; }
    public String getName() { return name; }
}
