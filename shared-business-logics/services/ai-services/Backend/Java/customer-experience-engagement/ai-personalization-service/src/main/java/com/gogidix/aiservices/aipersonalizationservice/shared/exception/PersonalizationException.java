package com.gogidix.aiservices.aipersonalizationservice.shared.exception;

public class PersonalizationException extends RuntimeException {
    public PersonalizationException(String message) {
        super(message);
    }

    public PersonalizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
