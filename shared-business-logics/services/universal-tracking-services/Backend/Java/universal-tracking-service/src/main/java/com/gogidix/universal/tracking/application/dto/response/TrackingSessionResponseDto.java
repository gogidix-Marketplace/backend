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
 * DTO for tracking session response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tracking session response")
public class TrackingSessionResponseDto {

    @Schema(description = "Session ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Session identifier", example = "session-abc-123")
    private String sessionId;

    @Schema(description = "User ID", example = "user-456")
    private String userId;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "Source", example = "WEB")
    private String source;

    @Schema(description = "IP address", example = "192.168.1.1")
    private String ipAddress;

    @Schema(description = "User agent")
    private String userAgent;

    @Schema(description = "Device type", example = "DESKTOP")
    private String deviceType;

    @Schema(description = "Browser", example = "Chrome")
    private String browser;

    @Schema(description = "Operating system", example = "Windows 10")
    private String os;

    @Schema(description = "Country", example = "US")
    private String country;

    @Schema(description = "City", example = "New York")
    private String city;

    @Schema(description = "Referrer URL")
    private String referrer;

    @Schema(description = "Landing page URL")
    private String landingPage;

    @Schema(description = "Campaign")
    private String campaign;

    @Schema(description = "Session start timestamp")
    private LocalDateTime startedAt;

    @Schema(description = "Last activity timestamp")
    private LocalDateTime lastActivityAt;

    @Schema(description = "Session end timestamp")
    private LocalDateTime endedAt;

    @Schema(description = "Duration in seconds", example = "300")
    private Integer durationSeconds;

    @Schema(description = "Event count", example = "15")
    private Integer eventCount;

    @Schema(description = "Page view count", example = "8")
    private Integer pageViewCount;

    @Schema(description = "Whether session is active")
    private Boolean isActive;

    @Schema(description = "Metadata")
    private Map<String, Object> metadata;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
