package com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateWebhookRequestDto(
        @NotBlank(message = "Name is required")
        String name,
        String description,
        @NotBlank(message = "URL is required")
        String url,
        String httpMethod,
        String secret
) {
}
