package com.gogidix.courier.availabilityservice.application.command;

import java.time.LocalDate;
import java.util.List;

/**
 * Command to create driver availability.
 */
public record CreateAvailabilityCommand(
        String tenantId,
        String driverId,
        LocalDate date,
        Integer maxCapacity,
        List<String> preferredZones
) {
    public CreateAvailabilityCommand {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("driverId is required");
        }
        if (date == null) {
            throw new IllegalArgumentException("date is required");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("date cannot be in the past");
        }
        if (maxCapacity != null && maxCapacity <= 0) {
            throw new IllegalArgumentException("maxCapacity must be positive");
        }
    }
}
