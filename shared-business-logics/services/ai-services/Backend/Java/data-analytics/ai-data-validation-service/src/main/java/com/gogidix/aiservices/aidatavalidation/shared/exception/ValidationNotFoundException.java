package com.gogidix.aiservices.aidatavalidation.shared.exception;

public class ValidationNotFoundException extends RuntimeException {
    public ValidationNotFoundException(String id) {
        super("Validation not found: " + id);
    }
}
