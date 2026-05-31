package com.gogidix.universal.tracking.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for creating a tracking session.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a tracking session")
public class CreateSessionRequestDto {

    @NotBlank(message = "Session ID is required")
    @Schema(description = "Unique session identifier", example = "session-abc-123")
    private String sessionId;

    @Schema(description = "User ID", example = "user-456")
    private String userId;

    @Schema(description = "Source of session (WEB, MOBILE, API)", example = "WEB")
    private String source;

    @Schema(description = "IP address", example = "192.168.1.1")
    private String ipAddress;

    @Schema(description = "User agent string", example = "Mozilla/5.0...")
    private String userAgent;

    @Schema(description = "Device type (DESKTOP, MOBILE, TABLET)", example = "DESKTOP")
    private String deviceType;

    @Schema(description = "Browser name", example = "Chrome")
    private String browser;

    @Schema(description = "Operating system", example = "Windows 10")
    private String os;

    @Schema(description = "Country code", example = "US")
    private String country;

    @Schema(description = "City", example = "New York")
    private String city;

    @Schema(description = "Referrer URL", example = "https://google.com")
    private String referrer;

    @Schema(description = "Landing page URL", example = "https://example.com/home")
    private String landingPage;

    @Schema(description = "Campaign identifier", example = "campaign-winter-2025")
    private String campaign;

    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;
}
