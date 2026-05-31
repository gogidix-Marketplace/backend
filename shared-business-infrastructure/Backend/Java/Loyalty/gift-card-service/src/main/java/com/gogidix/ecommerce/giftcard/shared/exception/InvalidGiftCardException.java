package com.gogidix.ecommerce.giftcard.shared.exception;

public class InvalidGiftCardException extends RuntimeException {
    public InvalidGiftCardException(String message) { super(message); }
    public InvalidGiftCardException(String message, Throwable cause) { super(message, cause); }
}
