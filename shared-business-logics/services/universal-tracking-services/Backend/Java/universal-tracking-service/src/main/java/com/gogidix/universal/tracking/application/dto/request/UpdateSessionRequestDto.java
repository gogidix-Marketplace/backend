package com.gogidix.universal.tracking.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for updating a tracking session.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update a tracking session")
public class UpdateSessionRequestDto {

    @Schema(description = "Referrer URL", example = "https://google.com")
    private String referrer;

    @Schema(description = "Landing page URL", example = "https://example.com/home")
    private String landingPage;

    @Schema(description = "Campaign identifier", example = "campaign-winter-2025")
    private String campaign;

    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;

    @Schema(description = "Whether to end the session", example = "false")
    private Boolean endSession;
}
