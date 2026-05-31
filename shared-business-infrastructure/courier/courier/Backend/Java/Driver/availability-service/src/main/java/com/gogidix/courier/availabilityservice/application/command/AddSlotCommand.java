package com.gogidix.courier.availabilityservice.application.command;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Command to add an availability slot.
 */
public record AddSlotCommand(
        String tenantId,
        String driverId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String zoneId
) {
    public AddSlotCommand {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("driverId is required");
        }
        if (date == null) {
            throw new IllegalArgumentException("date is required");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("startTime is required");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("endTime is required");
        }
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new IllegalArgumentException("startTime must be before endTime");
        }
    }
}
