package com.gogidix.courier.gpstrackingservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Response DTO for location history summary.
 */
@Schema(description = "Response DTO for location history summary")
public record LocationHistoryResponse(

        @JsonProperty("id")
        @Schema(description = "History ID", example = "hist-123")
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

        @JsonProperty("date")
        @Schema(description = "History date (YYYY-MM-DD)", example = "2025-02-20")
        String date,

        @JsonProperty("total_distance_meters")
        @Schema(description = "Total distance traveled in meters", example = "25000.0")
        Double totalDistanceMeters,

        @JsonProperty("duration_seconds")
        @Schema(description = "Total duration in seconds", example = "3600")
        Long durationSeconds,

        @JsonProperty("start_time")
        @Schema(description = "First location time", example = "2025-02-20T10:00:00Z")
        Instant startTime,

        @JsonProperty("end_time")
        @Schema(description = "Last location time", example = "2025-02-20T11:00:00Z")
        Instant endTime,

        @JsonProperty("location_count")
        @Schema(description = "Number of location points", example = "360")
        int locationCount,

        @JsonProperty("average_speed")
        @Schema(description = "Average speed in m/s", example = "6.94")
        double averageSpeed
) {
}
