package com.gogidix.aiservices.aicontentgenerationservice.shared.exception;

public class ContentGenerationException extends RuntimeException {

    public ContentGenerationException(String message) {
        super(message);
    }

    public ContentGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
