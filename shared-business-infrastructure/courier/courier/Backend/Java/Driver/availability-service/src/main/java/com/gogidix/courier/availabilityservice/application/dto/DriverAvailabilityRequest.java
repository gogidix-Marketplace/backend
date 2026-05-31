package com.gogidix.courier.availabilityservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating/updating driver availability.
 */
public record DriverAvailabilityRequest(
        @NotBlank(message = "Driver ID is required")
        String driverId,

        @NotNull(message = "Date is required")
        LocalDate date,

        @Positive(message = "Max capacity must be positive")
        Integer maxCapacity,

        List<String> preferredZones
) {
}
