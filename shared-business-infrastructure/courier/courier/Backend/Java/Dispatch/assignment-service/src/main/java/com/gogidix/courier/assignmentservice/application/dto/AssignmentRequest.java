package com.gogidix.courier.assignmentservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating a driver assignment.
 */
@Schema(description = "Request DTO for creating a driver assignment")
public record AssignmentRequest(

        @JsonProperty("tenant_id")
        @Schema(description = "Tenant identifier", example = "tenant-001", required = true)
        @NotBlank(message = "tenantId is required")
        String tenantId,

        @JsonProperty("dispatch_id")
        @Schema(description = "Dispatch order identifier", example = "dispatch-12345", required = true)
        @NotBlank(message = "dispatchId is required")
        String dispatchId,

        @JsonProperty("driver_id")
        @Schema(description = "Driver identifier", example = "driver-789", required = true)
        @NotBlank(message = "driverId is required")
        String driverId,

        @Schema(description = "Pickup location", required = true)
        @NotNull(message = "pickupLocation is required")
        @Valid
        LocationDto pickupLocation,

        @Schema(description = "Delivery location", required = true)
        @NotNull(message = "deliveryLocation is required")
        @Valid
        LocationDto deliveryLocation,

        @Schema(description = "Assignment priority", example = "NORMAL")
        DriverAssignment.AssignmentPriority priority,

        @Schema(description = "Assignment score for optimization", example = "0.85")
        Double assignmentScore,

        @Schema(description = "Assignment reason", example = "Closest available driver")
        String assignmentReason,

        @Schema(description = "Estimated distance in km", example = "5.2")
        Double estimatedDistanceKm,

        @Schema(description = "Estimated duration in minutes", example = "15")
        Integer estimatedDurationMinutes,

        @Schema(description = "Assignment notes", example = "Customer requested prompt delivery")
        String notes
) {
    /**
     * Location DTO.
     */
    @Schema(description = "Location details")
    public record LocationDto(

            @Schema(description = "Latitude", example = "40.7128", required = true)
            Double latitude,

            @Schema(description = "Longitude", example = "-74.0060", required = true)
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
}
