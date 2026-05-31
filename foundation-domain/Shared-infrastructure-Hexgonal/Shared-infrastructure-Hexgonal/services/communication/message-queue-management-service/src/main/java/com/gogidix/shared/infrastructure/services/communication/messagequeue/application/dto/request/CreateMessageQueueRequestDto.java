package com.gogidix.shared.infrastructure.services.communication.messagequeue.application.dto.request;
import jakarta.validation.constraints.NotBlank;
public record CreateMessageQueueRequestDto(
        @NotBlank(message = "Name is required")
        String name,
        String description,
        @NotBlank(message = "Type is required")
        String type,
        String region,
        Long maxSize,
        Long messageRetentionPeriod,
        Integer maxReceiveCount,
        Long visibilityTimeout,
        Integer deliveryDelay
) {
}
