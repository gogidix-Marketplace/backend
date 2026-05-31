package com.gogidix.courier.availabilityservice.application.command;

import com.gogidix.courier.availabilityservice.domain.entity.DriverAvailability;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Command to update driver availability.
 */
public record UpdateAvailabilityCommand(
        String availabilityId,
        LocalDate date,
        DriverAvailability.AvailabilityStatus status,
        Integer maxCapacity,
        List<String> preferredZones,
        LocalTime locationUpdateTime,
        Double latitude,
        Double longitude
) {
    public UpdateAvailabilityCommand {
        if (availabilityId == null || availabilityId.isBlank()) {
            throw new IllegalArgumentException("availabilityId is required");
        }
    }
}
