package com.gogidix.courier.etaservice.application.dto;

import java.time.Instant;

/**
 * DTO for traffic factor information.
 */
public record TrafficFactorDTO(

        String id,
        String tenantId,
        String areaCode,
        String areaName,
        Double latitude,
        Double longitude,
        Double radiusKm,
        Integer dayOfWeek,
        Integer hourOfDay,
        String trafficLevel,
        Double multiplier,
        Double effectiveMultiplier,
        Double averageSpeedKmh,
        Integer sampleCount,
        Double confidence,
        Boolean isPeakHour,
        Double seasonalFactor,
        Double weatherFactor,
        Instant createdAt,
        Instant updatedAt,
        Instant lastObservedAt

) {
}
