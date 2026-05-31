package com.gogidix.courier.availabilityservice.application.dto;

import com.gogidix.courier.availabilityservice.domain.entity.UnavailablePeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for creating an unavailable period.
 */
public record UnavailablePeriodRequest(
        @NotBlank(message = "Driver ID is required")
        String driverId,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "Start time is required")
        LocalTime startTime,

        @NotNull(message = "End date is required")
        LocalDate endDate,

        @NotNull(message = "End time is required")
        LocalTime endTime,

        String reason,

        UnavailablePeriod.UnavailabilityReasonType reasonType,

        Boolean isRecurring,

        String recurrencePattern
) {
}
