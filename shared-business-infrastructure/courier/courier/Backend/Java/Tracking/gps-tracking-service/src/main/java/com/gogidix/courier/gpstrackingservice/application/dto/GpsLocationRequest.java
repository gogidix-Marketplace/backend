package com.gogidix.courier.gpstrackingservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogidix.courier.gpstrackingservice.domain.entity.GpsLocation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for submitting a GPS location update.
 */
@Schema(description = "Request DTO for GPS location update")
public record GpsLocationRequest(

        @JsonProperty("driver_id")
        @Schema(description = "Driver ID", example = "driver-123", required = true)
        @NotBlank(message = "driverId is required")
        String driverId,

        @JsonProperty("order_id")
        @Schema(description = "Associated Order ID (optional)", example = "order-456")
        String orderId,

        @JsonProperty("latitude")
        @Schema(description = "Latitude coordinate", example = "40.7128", required = true)
        @NotNull(message = "latitude is required")
        @DecimalMin(value = "-90.0", message = "latitude must be >= -90")
        @DecimalMax(value = "90.0", message = "latitude must be <= 90")
        Double latitude,

        @JsonProperty("longitude")
        @Schema(description = "Longitude coordinate", example = "-74.0060", required = true)
        @NotNull(message = "longitude is required")
        @DecimalMin(value = "-180.0", message = "longitude must be >= -180")
        @DecimalMax(value = "180.0", message = "longitude must be <= 180")
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
        @Schema(description = "Heading in degrees (0-360)", example = "45.0")
        @Min(value = 0, message = "heading must be >= 0")
        @Max(value = 360, message = "heading must be <= 360")
        Double heading,

        @JsonProperty("battery_level")
        @Schema(description = "Battery level percentage (0-100)", example = "85")
        @Min(value = 0, message = "batteryLevel must be >= 0")
        @Max(value = 100, message = "batteryLevel must be <= 100")
        Integer batteryLevel,

        @JsonProperty("location_source")
        @Schema(description = "Location source", example = "GPS")
        GpsLocation.LocationSource locationSource
) {
}
