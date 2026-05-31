package com.gogidix.ecommerce.storecredit.shared.exception;

public class InvalidStoreCreditException extends RuntimeException {
    public InvalidStoreCreditException(String message) { super(message); }
    public InvalidStoreCreditException(String message, Throwable cause) { super(message, cause); }
}
