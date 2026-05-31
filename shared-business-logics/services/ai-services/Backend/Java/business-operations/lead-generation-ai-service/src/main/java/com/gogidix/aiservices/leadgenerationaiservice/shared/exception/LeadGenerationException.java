package com.gogidix.aiservices.leadgenerationaiservice.shared.exception;

public class LeadGenerationException extends RuntimeException {
    public LeadGenerationException(String message) {
        super(message);
    }

    public LeadGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
