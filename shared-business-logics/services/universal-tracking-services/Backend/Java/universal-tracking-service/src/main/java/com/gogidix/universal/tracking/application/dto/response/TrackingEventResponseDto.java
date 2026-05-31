package com.gogidix.universal.tracking.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for tracking event response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tracking event response")
public class TrackingEventResponseDto {

    @Schema(description = "Event ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Event type", example = "PAGE_VIEW")
    private String eventType;

    @Schema(description = "Session ID", example = "session-123")
    private String sessionId;

    @Schema(description = "User ID", example = "user-456")
    private String userId;

    @Schema(description = "Source", example = "WEB")
    private String source;

    @Schema(description = "Event timestamp", example = "2025-01-15T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Event name", example = "Homepage Visit")
    private String eventName;

    @Schema(description = "Event description")
    private String description;

    @Schema(description = "Event properties")
    private Map<String, Object> properties;

    @Schema(description = "Metadata")
    private Map<String, Object> metadata;

    @Schema(description = "IP address", example = "192.168.1.1")
    private String ipAddress;

    @Schema(description = "User agent")
    private String userAgent;

    @Schema(description = "Referrer URL")
    private String referrer;

    @Schema(description = "Page URL")
    private String pageUrl;

    @Schema(description = "Page title")
    private String pageTitle;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "Correlation ID")
    private String correlationId;

    @Schema(description = "Priority", example = "5")
    private Integer priority;

    @Schema(description = "Whether event is processed")
    private Boolean processed;

    @Schema(description = "Processed timestamp")
    private LocalDateTime processedAt;

    @Schema(description = "Error message")
    private String errorMessage;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
