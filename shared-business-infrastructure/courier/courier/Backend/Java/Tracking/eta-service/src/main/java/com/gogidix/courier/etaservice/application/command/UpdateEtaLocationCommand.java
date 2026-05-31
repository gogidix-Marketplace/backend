package com.gogidix.courier.etaservice.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Command to update current location for ETA recalculation.
 */
public record UpdateEtaLocationCommand(

        @NotBlank(message = "Dispatch ID is required")
        String dispatchId,

        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        @NotNull(message = "Current location is required")
        LocationDto currentLocation,

        String updatedBy

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
