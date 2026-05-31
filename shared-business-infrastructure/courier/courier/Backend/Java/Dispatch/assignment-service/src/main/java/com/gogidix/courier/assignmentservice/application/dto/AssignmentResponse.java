package com.gogidix.courier.assignmentservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO for driver assignment data.
 */
@Schema(description = "Response DTO for driver assignment")
public record AssignmentResponse(

        @Schema(description = "Unique assignment ID", example = "550e8400-e29b-41d4-a716-446655440000")
        String id,

        @JsonProperty("tenant_id")
        @Schema(description = "Tenant identifier", example = "tenant-001")
        String tenantId,

        @JsonProperty("dispatch_id")
        @Schema(description = "Dispatch order identifier", example = "dispatch-12345")
        String dispatchId,

        @JsonProperty("driver_id")
        @Schema(description = "Driver identifier", example = "driver-789")
        String driverId,

        @Schema(description = "Assignment status", example = "IN_PROGRESS")
        DriverAssignment.AssignmentStatus status,

        @Schema(description = "Assignment priority", example = "HIGH")
        DriverAssignment.AssignmentPriority priority,

        @JsonProperty("pickup_location")
        @Schema(description = "Pickup location")
        LocationDto pickupLocation,

        @JsonProperty("delivery_location")
        @Schema(description = "Delivery location")
        LocationDto deliveryLocation,

        @JsonProperty("estimated_distance_km")
        @Schema(description = "Estimated distance in km", example = "5.2")
        Double estimatedDistanceKm,

        @JsonProperty("estimated_duration_minutes")
        @Schema(description = "Estimated duration in minutes", example = "15")
        Integer estimatedDurationMinutes,

        @JsonProperty("actual_distance_km")
        @Schema(description = "Actual distance traveled in km", example = "5.5")
        Double actualDistanceKm,

        @JsonProperty("actual_duration_minutes")
        @Schema(description = "Actual duration in minutes", example = "18")
        Integer actualDurationMinutes,

        @JsonProperty("assignment_score")
        @Schema(description = "Optimization score", example = "0.85")
        Double assignmentScore,

        @JsonProperty("assignment_reason")
        @Schema(description = "Reason for assignment", example = "Closest available driver")
        String assignmentReason,

        @JsonProperty("assigned_at")
        @Schema(description = "Assignment timestamp", example = "2023-01-01T10:00:00Z")
        Instant assignedAt,

        @JsonProperty("accepted_at")
        @Schema(description = "Acceptance timestamp", example = "2023-01-01T10:02:00Z")
        Instant acceptedAt,

        @JsonProperty("started_at")
        @Schema(description = "Start timestamp", example = "2023-01-01T10:05:00Z")
        Instant startedAt,

        @JsonProperty("completed_at")
        @Schema(description = "Completion timestamp", example = "2023-01-01T10:23:00Z")
        Instant completedAt,

        @JsonProperty("cancelled_at")
        @Schema(description = "Cancellation timestamp", example = "2023-01-01T10:10:00Z")
        Instant cancelledAt,

        @JsonProperty("cancellation_reason")
        @Schema(description = "Cancellation reason", example = "Driver unavailable")
        String cancellationReason,

        @Schema(description = "Assignment notes", example = "Customer requested prompt delivery")
        String notes,

        @Schema(description = "Assignment metadata")
        AssignmentMetadataDto metadata,

        @JsonProperty("created_at")
        @Schema(description = "Creation timestamp", example = "2023-01-01T10:00:00Z")
        Instant createdAt,

        @JsonProperty("updated_at")
        @Schema(description = "Last update timestamp", example = "2023-01-01T10:05:00Z")
        Instant updatedAt,

        @Schema(description = "Entity version for optimistic locking", example = "1")
        Long version
) {
    /**
     * Location DTO.
     */
    @Schema(description = "Location details")
    public record LocationDto(
            @Schema(description = "Latitude", example = "40.7128")
            Double latitude,

            @Schema(description = "Longitude", example = "-74.0060")
            Double longitude,

            @Schema(description = "Address", example = "123 Main St")
            String address,

            @Schema(description = "City", example = "New York")
            String city,

            @Schema(description = "Postal code", example = "10001")
            String postalCode,

            @Schema(description = "Country", example = "USA")
            String country
    ) {}

    /**
     * Assignment metadata DTO.
     */
    @Schema(description = "Assignment metadata")
    public record AssignmentMetadataDto(
            @Schema(description = "User or system that made the assignment", example = "auto-assigner-v1")
            String assignedBy,

            @Schema(description = "Algorithm used for assignment", example = "nearest-driver")
            String assignmentAlgorithm,

            @JsonProperty("reassignment_history")
            @Schema(description = "History of reassignments")
            List<ReassignmentRecordDto> reassignmentHistory,

            @JsonProperty("attempt_count")
            @Schema(description = "Number of assignment attempts", example = "1")
            Integer attemptCount,

            @JsonProperty("previous_driver_id")
            @Schema(description = "Previous driver ID if reassigned", example = "driver-456")
            String previousDriverId
    ) {}

    /**
     * Reassignment record DTO.
     */
    @Schema(description = "Reassignment record")
    public record ReassignmentRecordDto(
            @JsonProperty("from_driver_id")
            @Schema(description = "Previous driver ID", example = "driver-456")
            String fromDriverId,

            @JsonProperty("to_driver_id")
            @Schema(description = "New driver ID", example = "driver-789")
            String toDriverId,

            @Schema(description = "Reassignment timestamp", example = "2023-01-01T10:10:00Z")
            Instant timestamp,

            @Schema(description = "Reassignment reason", example = "Previous driver rejected")
            String reason
    ) {}
}
