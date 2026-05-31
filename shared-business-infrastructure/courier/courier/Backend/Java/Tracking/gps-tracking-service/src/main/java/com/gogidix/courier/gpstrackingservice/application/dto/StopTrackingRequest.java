package com.gogidix.courier.gpstrackingservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for stopping a tracking session.
 */
@Schema(description = "Request DTO for stopping tracking session")
public record StopTrackingRequest(

        @JsonProperty("session_id")
        @Schema(description = "Session ID", example = "session-abc-123", required = true)
        @NotBlank(message = "sessionId is required")
        String sessionId,

        @JsonProperty("end_reason")
        @Schema(description = "Reason for stopping tracking", example = "Order delivered")
        String endReason
) {
}
