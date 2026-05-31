package com.gogidix.courier.gpstrackingservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogidix.courier.gpstrackingservice.domain.entity.GpsLocation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Response DTO for GPS location data.
 */
@Schema(description = "Response DTO for GPS location")
public record GpsLocationResponse(

        @JsonProperty("id")
        @Schema(description = "Location ID", example = "loc-123")
        String id,

        @JsonProperty("tenant_id")
        @Schema(description = "Tenant ID", example = "tenant-001")
        String tenantId,

        @JsonProperty("driver_id")
        @Schema(description = "Driver ID", example = "driver-123")
        String driverId,

        @JsonProperty("order_id")
        @Schema(description = "Associated Order ID", example = "order-456")
        String orderId,

        @JsonProperty("latitude")
        @Schema(description = "Latitude coordinate", example = "40.7128")
        Double latitude,

        @JsonProperty("longitude")
        @Schema(description = "Longitude coordinate", example = "-74.0060")
        Double longitude,

        @JsonProperty("altitude")
        @Schema(description = "Altitude in meters", example = "10.5")
        Double altitude,

        @JsonProperty("accuracy")
        @Schema(description = "Accuracy in meters", example = "5.0")
        Double accuracy,

        @JsonProperty("speed")
        @Schema(description = "Speed in meters per second", example = "8.5")
        Double speed,

        @JsonProperty("heading")
        @Schema(description = "Heading in degrees", example = "45.0")
        Double heading,

        @JsonProperty("battery_level")
        @Schema(description = "Battery level percentage", example = "85")
        Integer batteryLevel,

        @JsonProperty("location_source")
        @Schema(description = "Location source", example = "GPS")
        GpsLocation.LocationSource locationSource,

        @JsonProperty("timestamp")
        @Schema(description = "Location timestamp", example = "2025-02-20T10:30:00Z")
        Instant timestamp,

        @JsonProperty("created_at")
        @Schema(description = "Record creation time", example = "2025-02-20T10:30:01Z")
        Instant createdAt,

        @JsonProperty("is_moving")
        @Schema(description = "Whether the driver is moving", example = "true")
        Boolean isMoving,

        @JsonProperty("is_recent")
        @Schema(description = "Whether the location is recent (within 5 minutes)", example = "true")
        Boolean isRecent
) {
}
