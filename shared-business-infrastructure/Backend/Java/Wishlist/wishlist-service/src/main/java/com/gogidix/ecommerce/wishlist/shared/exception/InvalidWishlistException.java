package com.gogidix.ecommerce.wishlist.shared.exception;

public class InvalidWishlistException extends RuntimeException {
    public InvalidWishlistException(String message) { super(message); }
    public InvalidWishlistException(String message, Throwable cause) { super(message, cause); }
}
