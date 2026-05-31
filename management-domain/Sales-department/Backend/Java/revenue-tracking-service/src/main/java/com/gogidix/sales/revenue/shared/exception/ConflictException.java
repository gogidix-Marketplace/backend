package com.gogidix.sales.revenue.shared.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String resource, String identifier) {
        super(resource + " with identifier '" + identifier + "' already exists");
    }
}
