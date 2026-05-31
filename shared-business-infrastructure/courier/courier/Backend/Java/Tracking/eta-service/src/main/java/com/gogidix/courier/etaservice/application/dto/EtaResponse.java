package com.gogidix.courier.etaservice.application.dto;

import com.gogidix.courier.etaservice.domain.entity.EtaCalculation;

import java.time.Instant;

/**
 * Response DTO for ETA calculation results.
 */
public record EtaResponse(

        String id,
        String dispatchId,
        String tenantId,
        LocationDto pickupLocation,
        LocationDto dropoffLocation,
        LocationDto currentLocation,
        String vehicleType,
        Double distanceKm,
        Integer etaMinutes,
        Instant estimatedArrival,
        String trafficLevel,
        Double trafficMultiplier,
        Double confidenceScore,
        String calculationMethod,
        EtaCalculation.EtaStatus status,
        Instant createdAt,
        Instant updatedAt,
        Instant recalculatedAt,
        Integer recalculationCount

) {

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
