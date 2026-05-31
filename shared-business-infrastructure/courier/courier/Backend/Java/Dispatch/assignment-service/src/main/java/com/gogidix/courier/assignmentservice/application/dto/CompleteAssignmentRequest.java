package com.gogidix.courier.assignmentservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Request DTO for completing a driver assignment.
 */
@Schema(description = "Request DTO for completing an assignment")
public record CompleteAssignmentRequest(

        @JsonProperty("actual_distance_km")
        @Schema(description = "Actual distance traveled in km", example = "5.5")
        @PositiveOrZero(message = "actualDistanceKm must be non-negative")
        Double actualDistanceKm,

        @JsonProperty("actual_duration_minutes")
        @Schema(description = "Actual duration in minutes", example = "18")
        @PositiveOrZero(message = "actualDurationMinutes must be non-negative")
        Integer actualDurationMinutes
) {}
