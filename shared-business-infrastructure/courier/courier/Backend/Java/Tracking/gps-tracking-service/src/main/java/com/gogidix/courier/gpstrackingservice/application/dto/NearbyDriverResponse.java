package com.gogidix.courier.gpstrackingservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Response DTO for nearby driver information.
 */
@Schema(description = "Response DTO for nearby driver")
public record NearbyDriverResponse(

        @JsonProperty("driver_id")
        @Schema(description = "Driver ID", example = "driver-123")
        String driverId,

        @JsonProperty("latitude")
        @Schema(description = "Driver's current latitude", example = "40.7130")
        Double latitude,

        @JsonProperty("longitude")
        @Schema(description = "Driver's current longitude", example = "-74.0065")
        Double longitude,

        @JsonProperty("distance_meters")
        @Schema(description = "Distance from search point in meters", example = "250.5")
        Double distanceMeters,

        @JsonProperty("speed")
        @Schema(description = "Current speed in m/s", example = "8.5")
        Double speed,

        @JsonProperty("heading")
        @Schema(description = "Current heading in degrees", example = "45.0")
        Double heading,

        @JsonProperty("last_update")
        @Schema(description = "Last location update time", example = "2025-02-20T10:29:00Z")
        Instant lastUpdate,

        @JsonProperty("is_moving")
        @Schema(description = "Whether the driver is moving", example = "true")
        Boolean isMoving,

        @JsonProperty("battery_level")
        @Schema(description = "Driver's battery level", example = "85")
        Integer batteryLevel,

        @JsonProperty("session_active")
        @Schema(description = "Whether driver has an active tracking session", example = "true")
        Boolean sessionActive
) {
}
