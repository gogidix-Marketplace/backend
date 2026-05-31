package com.gogidix.courier.availabilityservice.application.dto;

import com.gogidix.courier.availabilityservice.domain.entity.AvailabilitySlot;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for availability slot response.
 */
public record SlotResponse(
        String id,
        String tenantId,
        String driverId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        AvailabilitySlot.SlotStatus status,
        String bookingId,
        String zoneId,
        Instant createdAt,
        Instant updatedAt
) {
}
