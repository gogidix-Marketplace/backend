package com.gogidix.courier.availabilityservice.application.command;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Command to book an availability slot.
 */
public record BookSlotCommand(
        String slotId,
        String bookingId
) {
    public BookSlotCommand {
        if (slotId == null || slotId.isBlank()) {
            throw new IllegalArgumentException("slotId is required");
        }
        if (bookingId == null || bookingId.isBlank()) {
            throw new IllegalArgumentException("bookingId is required");
        }
    }
}
