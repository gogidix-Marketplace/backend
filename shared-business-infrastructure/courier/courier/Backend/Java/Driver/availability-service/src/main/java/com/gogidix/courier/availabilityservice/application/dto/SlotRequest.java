package com.gogidix.courier.availabilityservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for creating an availability slot.
 */
public record SlotRequest(
        @NotBlank(message = "Driver ID is required")
        String driverId,

        @NotNull(message = "Date is required")
        LocalDate date,

        @NotNull(message = "Start time is required")
        LocalTime startTime,

        @NotNull(message = "End time is required")
        LocalTime endTime,

        String zoneId
) {
}
