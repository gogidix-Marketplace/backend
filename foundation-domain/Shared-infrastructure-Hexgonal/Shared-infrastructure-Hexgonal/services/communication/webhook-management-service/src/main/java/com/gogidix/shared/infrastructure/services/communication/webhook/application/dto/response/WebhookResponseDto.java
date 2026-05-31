package com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record WebhookResponseDto(
        String id,
        String tenantId,
        String name,
        String description,
        String url,
        String status,
        String eventType,
        List<String> eventTypes,
        String httpMethod,
        String createdBy,
        Integer retryAttempts,
        Long retryDelay,
        Integer timeout,
        Boolean sslVerificationEnabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
