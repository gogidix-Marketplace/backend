package com.gogidix.shared.infrastructure.services.communication.statusbroadcast.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Request DTO for Status Broadcast operations
 */
public record CreateStatusBroadcastRequestDto(
    @NotBlank(message = "Broadcast type is required")
    @Size(max = 50, message = "Broadcast type must not exceed 50 characters")
    String broadcastType,
    
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title,
    
    @NotBlank(message = "Message is required")
    String message,
    
    @Size(max = 20, message = "Severity must not exceed 20 characters")
    String severity,
    
    LocalDateTime scheduledAt,
    
    LocalDateTime expiresAt,
    
    String targetAudience,
    
    String[] targetTenantIds,
    
    Map<String, Object> metadata
) {
}
