package com.gogidix.courier.etaservice.application.dto;

import com.gogidix.courier.etaservice.domain.entity.EtaHistory;

import java.time.Instant;

/**
 * Response DTO for ETA history entries.
 */
public record EtaHistoryResponse(

        String id,
        String dispatchId,
        Instant timestamp,
        Integer etaMinutes,
        Integer previousEtaMinutes,
        Integer etaChangeMinutes,
        Double distanceKm,
        LocationDto currentLocation,
        String trafficLevel,
        Double trafficMultiplier,
        String vehicleType,
        Double confidenceScore,
        String changeReason,
        EtaHistory.ChangeType changeType,
        String calculatedBy,
        String source

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
