package com.gogidix.courier.etaservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for ETA calculation.
 */
public record EtaRequest(

        @NotBlank(message = "Dispatch ID is required")
        String dispatchId,

        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        @NotNull(message = "Pickup location is required")
        LocationDto pickupLocation,

        @NotNull(message = "Dropoff location is required")
        LocationDto dropoffLocation,

        String vehicleType,

        String calculatedBy

) {

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
