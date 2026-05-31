package com.gogidix.shared.infrastructure.services.communication.webhook.domain.exception;

public class WebhookNotFoundException extends RuntimeException {
    public WebhookNotFoundException(String id) {
        super("Webhook not found with id: " + id);
    }
}
