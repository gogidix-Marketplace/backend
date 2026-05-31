package com.gogidix.ecommerce.loyalty.shared.exception;

public class InvalidLoyaltyException extends RuntimeException {
    public InvalidLoyaltyException(String message) { super(message); }
    public InvalidLoyaltyException(String message, Throwable cause) { super(message, cause); }
}
