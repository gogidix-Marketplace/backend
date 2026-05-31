package com.gogidix.ecommerce.search.shared.exception;

public class InvalidSearchException extends RuntimeException {
    public InvalidSearchException(String message) { super(message); }
    public InvalidSearchException(String message, Throwable cause) { super(message, cause); }
}
