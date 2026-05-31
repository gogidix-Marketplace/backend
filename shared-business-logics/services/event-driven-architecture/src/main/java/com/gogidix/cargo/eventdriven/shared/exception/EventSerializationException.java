package com.gogidix.cargo.eventdriven.shared.exception;

public class EventSerializationException extends RuntimeException {
    public EventSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
