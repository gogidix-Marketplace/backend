package com.gogidix.shared.infrastructure.services.communication.statusbroadcast.application.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response DTO for Status Broadcast operations
 */
public record StatusBroadcastResponseDto(
    String id,
    String tenantId,
    String broadcastType,
    String status,
    String title,
    String message,
    String severity,
    LocalDateTime scheduledAt,
    LocalDateTime expiresAt,
    LocalDateTime sentAt,
    String targetAudience,
    String[] targetTenantIds,
    Map<String, Object> metadata,
    String createdBy,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
