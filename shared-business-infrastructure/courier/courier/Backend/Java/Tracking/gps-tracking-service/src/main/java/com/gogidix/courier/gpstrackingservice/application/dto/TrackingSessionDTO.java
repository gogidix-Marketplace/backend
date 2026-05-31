package com.gogidix.courier.gpstrackingservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogidix.courier.gpstrackingservice.domain.entity.DriverTrackingSession;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO for tracking session data.
 */
@Schema(description = "Response DTO for tracking session")
public record TrackingSessionDTO(

        @JsonProperty("id")
        @Schema(description = "Session internal ID", example = "sess-internal-123")
        String id,

        @JsonProperty("tenant_id")
        @Schema(description = "Tenant ID", example = "tenant-001")
        String tenantId,

        @JsonProperty("session_id")
        @Schema(description = "Session ID", example = "session-abc-123")
        String sessionId,

        @JsonProperty("driver_id")
        @Schema(description = "Driver ID", example = "driver-123")
        String driverId,

        @JsonProperty("order_ids")
        @Schema(description = "Associated order IDs", example = "[\"order-456\", \"order-789\"]")
        List<String> orderIds,

        @JsonProperty("status")
        @Schema(description = "Session status", example = "ACTIVE")
        DriverTrackingSession.SessionStatus status,

        @JsonProperty("start_time")
        @Schema(description = "Session start time", example = "2025-02-20T10:00:00Z")
        Instant startTime,

        @JsonProperty("end_time")
        @Schema(description = "Session end time", example = "2025-02-20T12:00:00Z")
        Instant endTime,

        @JsonProperty("last_latitude")
        @Schema(description = "Last known latitude", example = "40.7128")
        Double lastLatitude,

        @JsonProperty("last_longitude")
        @Schema(description = "Last known longitude", example = "-74.0060")
        Double lastLongitude,

        @JsonProperty("last_location_time")
        @Schema(description = "Last location update time", example = "2025-02-20T11:59:00Z")
        Instant lastLocationTime,

        @JsonProperty("total_distance_meters")
        @Schema(description = "Total distance traveled in meters", example = "12500.0")
        Double totalDistanceMeters,

        @JsonProperty("location_update_count")
        @Schema(description = "Number of location updates", example = "150")
        Integer locationUpdateCount,

        @JsonProperty("duration_seconds")
        @Schema(description = "Session duration in seconds", example = "7200")
        Long durationSeconds,

        @JsonProperty("end_reason")
        @Schema(description = "Reason for session end", example = "Order delivered")
        String endReason,

        @JsonProperty("device_type")
        @Schema(description = "Device type", example = "Android")
        String deviceType,

        @JsonProperty("app_version")
        @Schema(description = "App version", example = "2.1.0")
        String appVersion,

        @JsonProperty("is_location_stale")
        @Schema(description = "Whether location data is stale (older than 5 minutes)", example = "false")
        Boolean isLocationStale,

        @JsonProperty("created_at")
        @Schema(description = "Record creation time", example = "2025-02-20T10:00:01Z")
        Instant createdAt,

        @JsonProperty("updated_at")
        @Schema(description = "Record update time", example = "2025-02-20T11:59:01Z")
        Instant updatedAt
) {
}
