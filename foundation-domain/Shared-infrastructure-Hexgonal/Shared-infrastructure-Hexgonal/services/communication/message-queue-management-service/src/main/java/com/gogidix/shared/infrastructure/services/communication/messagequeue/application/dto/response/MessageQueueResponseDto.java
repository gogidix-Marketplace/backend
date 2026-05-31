package com.gogidix.shared.infrastructure.services.communication.messagequeue.application.dto.response;
import java.time.LocalDateTime;
public record MessageQueueResponseDto(
        String id,
        String tenantId,
        String name,
        String description,
        String type,
        String status,
        String region,
        Long maxSize,
        Long messageRetentionPeriod,
        Integer maxReceiveCount,
        Long visibilityTimeout,
        Integer deliveryDelay,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
