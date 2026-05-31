package com.gogidix.shared.infrastructure.services.communication.eventbus.application.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;
/**
 * Request DTO for EventBridge operations
 */
public record CreateEventBridgeRequestDto(
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    String name,
    @NotBlank(message = "Source type is required")
    String sourceType,
    @NotBlank(message = "Target type is required")
    String targetType,
    String sourceTopic,
    String targetTopic,
    String sourceExchange,
    String targetExchange,
    String sourceQueue,
    String targetQueue,
    boolean enabled,
    @Size(max = 500, message = "Filter expression must not exceed 500 characters")
    String filterExpression,
    Map<String, Object> transformationRules,
    int maxRetries
) {
}
