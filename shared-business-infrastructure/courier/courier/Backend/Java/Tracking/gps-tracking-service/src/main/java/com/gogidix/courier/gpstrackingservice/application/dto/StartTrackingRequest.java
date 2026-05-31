package com.gogidix.courier.gpstrackingservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Request DTO for starting a tracking session.
 */
@Schema(description = "Request DTO for starting tracking session")
public record StartTrackingRequest(

        @JsonProperty("session_id")
        @Schema(description = "Optional session ID (auto-generated if not provided)", example = "session-abc-123")
        String sessionId,

        @JsonProperty("driver_id")
        @Schema(description = "Driver ID", example = "driver-123", required = true)
        @NotBlank(message = "driverId is required")
        String driverId,

        @JsonProperty("order_ids")
        @Schema(description = "List of order IDs to track", example = "[\"order-456\", \"order-789\"]")
        List<String> orderIds,

        @JsonProperty("device_type")
        @Schema(description = "Device type", example = "Android")
        String deviceType,

        @JsonProperty("app_version")
        @Schema(description = "App version", example = "2.1.0")
        String appVersion,

        @JsonProperty("start_reason")
        @Schema(description = "Reason for starting tracking", example = "Order pickup")
        String startReason
) {
}
