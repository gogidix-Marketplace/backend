package com.gogidix.aiservices.nlpprocessingservice.shared.exception;

public class NlpProcessingException extends RuntimeException {
    public NlpProcessingException(String message) {
        super(message);
    }

    public NlpProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
