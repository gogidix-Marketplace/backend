package com.gogidix.shared.infrastructure.services.communication.eventbus.application.dto.response;
import java.time.LocalDateTime;
import java.util.Map;
/**
 * Response DTO for EventBridge operations
 */
public record EventBridgeResponseDto(
    String id,
    String tenantId,
    String name,
    String sourceType,
    String targetType,
    String sourceTopic,
    String targetTopic,
    String sourceExchange,
    String targetExchange,
    String sourceQueue,
    String targetQueue,
    boolean enabled,
    String filterExpression,
    Map<String, Object> transformationRules,
    int retryCount,
    int maxRetries,
    long messageCount,
    long errorCount,
    String status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
