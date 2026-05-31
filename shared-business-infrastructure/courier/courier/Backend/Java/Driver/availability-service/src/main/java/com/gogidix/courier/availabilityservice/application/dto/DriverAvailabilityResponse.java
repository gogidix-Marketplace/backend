package com.gogidix.courier.availabilityservice.application.dto;

import com.gogidix.courier.availabilityservice.domain.entity.DriverAvailability;

import java.time.Instant;
import java.util.List;

/**
 * DTO for driver availability response.
 */
public record DriverAvailabilityResponse(
        String id,
        String tenantId,
        String driverId,
        String date,
        DriverAvailability.AvailabilityStatus status,
        List<SlotResponse> slots,
        Integer maxCapacity,
        Integer currentLoad,
        List<String> preferredZones,
        LocationResponse currentLocation,
        Instant createdAt,
        Instant updatedAt
) {
    public record SlotResponse(
            String startTime,
            String endTime,
            String status
    ) {
    }

    public record LocationResponse(
            Double latitude,
            Double longitude,
            String updatedAt
    ) {
    }
}
