package com.gogidix.courier.etaservice.application.command;

import jakarta.validation.constraints.NotBlank;

/**
 * Command to recalculate ETA for a dispatch.
 */
public record RecalculateEtaCommand(

        @NotBlank(message = "Dispatch ID is required")
        String dispatchId,

        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        LocationDto currentLocation,

        String reason,

        Boolean forceRecalculation

) {
    public RecalculateEtaCommand {
        if (forceRecalculation == null) {
            forceRecalculation = false;
        }
    }

    /**
     * Location DTO.
     */
    public record LocationDto(
            Double latitude,
            Double longitude,
            String address,
            String city,
            String country
    ) {}
}
