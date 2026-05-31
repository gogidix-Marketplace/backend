package com.gogidix.aiservices.multimodalprocessingservice.shared.exception;

public class MultimodalProcessingException extends RuntimeException {
    public MultimodalProcessingException(String message) {
        super(message);
    }

    public MultimodalProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
