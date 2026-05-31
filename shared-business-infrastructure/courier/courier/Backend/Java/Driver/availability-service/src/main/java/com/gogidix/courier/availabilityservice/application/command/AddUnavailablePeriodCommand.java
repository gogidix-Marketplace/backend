package com.gogidix.courier.availabilityservice.application.command;

import com.gogidix.courier.availabilityservice.domain.entity.UnavailablePeriod;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Command to add an unavailable period.
 */
public record AddUnavailablePeriodCommand(
        String tenantId,
        String driverId,
        LocalDate startDate,
        LocalTime startTime,
        LocalDate endDate,
        LocalTime endTime,
        String reason,
        UnavailablePeriod.UnavailabilityReasonType reasonType,
        Boolean isRecurring,
        String recurrencePattern
) {
    public AddUnavailablePeriodCommand {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("driverId is required");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("startDate is required");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("startTime is required");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("endDate is required");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("endTime is required");
        }
    }
}
