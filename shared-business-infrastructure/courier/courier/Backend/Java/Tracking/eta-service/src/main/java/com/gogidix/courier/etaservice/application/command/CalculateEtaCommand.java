package com.gogidix.courier.etaservice.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * Command to calculate ETA for a dispatch.
 */
public record CalculateEtaCommand(

        @NotBlank(message = "Dispatch ID is required")
        String dispatchId,

        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        @NotNull(message = "Pickup location is required")
        LocationDto pickupLocation,

        @NotNull(message = "Dropoff location is required")
        LocationDto dropoffLocation,

        String vehicleType,

        Map<String, Object> metadata

) {
    public CalculateEtaCommand {
        if (vehicleType == null || vehicleType.isBlank()) {
            vehicleType = "car";
        }
    }

    /**
     * Location DTO.
     */
    public record LocationDto(
            @NotNull(message = "Latitude is required")
            Double latitude,

            @NotNull(message = "Longitude is required")
            Double longitude,

            String address,
            String city,
            String country
    ) {}
}
