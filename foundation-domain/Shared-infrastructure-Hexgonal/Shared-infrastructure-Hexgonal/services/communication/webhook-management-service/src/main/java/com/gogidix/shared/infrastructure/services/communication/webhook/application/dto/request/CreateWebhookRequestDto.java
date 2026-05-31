package com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateWebhookRequestDto(
        @NotBlank(message = "Name is required")
        String name,
        String description,
        @NotBlank(message = "URL is required")
        String url,
        String eventType,
        List<String> eventTypes,
        String httpMethod,
        String secret,
        @NotBlank(message = "Created by is required")
        String createdBy,
        Integer retryAttempts,
        Long retryDelay,
        Integer timeout,
        Boolean sslVerificationEnabled
) {
}
