package com.gogidix.universal.tracking.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for creating a tracking event.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a tracking event")
public class CreateEventRequestDto {

    @NotBlank(message = "Event type is required")
    @Schema(description = "Type of event (e.g., PAGE_VIEW, CLICK, PURCHASE)", example = "PAGE_VIEW")
    private String eventType;

    @Schema(description = "Session ID", example = "session-123")
    private String sessionId;

    @Schema(description = "User ID", example = "user-456")
    private String userId;

    @Schema(description = "Source of event (WEB, MOBILE, API)", example = "WEB")
    private String source;

    @NotNull(message = "Timestamp is required")
    @Schema(description = "Event timestamp", example = "2025-01-15T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Event name", example = "Homepage Visit")
    private String eventName;

    @Schema(description = "Event description", example = "User visited the homepage")
    private String description;

    @Schema(description = "Event properties as key-value pairs")
    private Map<String, Object> properties;

    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;

    @Schema(description = "IP address", example = "192.168.1.1")
    private String ipAddress;

    @Schema(description = "User agent", example = "Mozilla/5.0...")
    private String userAgent;

    @Schema(description = "Referrer URL", example = "https://google.com")
    private String referrer;

    @Schema(description = "Page URL", example = "https://example.com/page")
    private String pageUrl;

    @Schema(description = "Page title", example = "Home Page")
    private String pageTitle;

    @Schema(description = "Correlation ID for distributed tracing")
    private String correlationId;

    @Schema(description = "Priority (1-10, higher = more important)", example = "5")
    private Integer priority;
}
