package com.gogidix.cargo.eventdriven.shared.exception;

public class DeadLetterException extends RuntimeException {
    private final String originalTopic;

    public DeadLetterException(String originalTopic, String message, Throwable cause) {
        super(message, cause);
        this.originalTopic = originalTopic;
    }

    public String getOriginalTopic() { return originalTopic; }
}
