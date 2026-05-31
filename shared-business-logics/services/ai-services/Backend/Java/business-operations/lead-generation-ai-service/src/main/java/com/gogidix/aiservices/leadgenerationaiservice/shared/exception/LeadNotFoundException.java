package com.gogidix.aiservices.leadgenerationaiservice.shared.exception;

public class LeadNotFoundException extends LeadGenerationException {
    public LeadNotFoundException(String leadId) {
        super("Lead not found: " + leadId);
    }

    public LeadNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
