package com.gogidix.courier.gpstrackingservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for finding nearby drivers.
 */
@Schema(description = "Request DTO for finding nearby drivers")
public record NearbyDriversRequest(

        @JsonProperty("latitude")
        @Schema(description = "Center latitude", example = "40.7128", required = true)
        @NotNull(message = "latitude is required")
        @DecimalMin(value = "-90.0", message = "latitude must be >= -90")
        @DecimalMax(value = "90.0", message = "latitude must be <= 90")
        Double latitude,

        @JsonProperty("longitude")
        @Schema(description = "Center longitude", example = "-74.0060", required = true)
        @NotNull(message = "longitude is required")
        @DecimalMin(value = "-180.0", message = "longitude must be >= -180")
        @DecimalMax(value = "180.0", message = "longitude must be <= 180")
        Double longitude,

        @JsonProperty("radius_meters")
        @Schema(description = "Search radius in meters", example = "5000")
        @Min(value = 100, message = "radius must be at least 100 meters")
        @Max(value = 50000, message = "radius must not exceed 50000 meters")
        Integer radiusMeters,

        @JsonProperty("max_results")
        @Schema(description = "Maximum number of results", example = "20")
        @Min(value = 1, message = "maxResults must be at least 1")
        @Max(value = 100, message = "maxResults must not exceed 100")
        Integer maxResults,

        @JsonProperty("include_inactive")
        @Schema(description = "Include inactive drivers", example = "false")
        Boolean includeInactive
) {
    public NearbyDriversRequest {
        if (radiusMeters == null) {
            radiusMeters = 5000;
        }
        if (maxResults == null) {
            maxResults = 20;
        }
        if (includeInactive == null) {
            includeInactive = false;
        }
    }
}
