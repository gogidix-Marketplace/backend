package com.gogidix.ecommerce.inventorysync.shared.exception;

public class InvalidInventorySyncException extends RuntimeException {
    public InvalidInventorySyncException(String message) { super(message); }
    public InvalidInventorySyncException(String message, Throwable cause) { super(message, cause); }
}
