package com.gogidix.courier.etaservice.application.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for ETA recalculation.
 */
public record RecalculateEtaRequest(

        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        LocationDto currentLocation,

        String reason,

        Boolean forceRecalculation,

        String updatedBy

) {

    public RecalculateEtaRequest {
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
