package com.gogidix.courier.availabilityservice.domain.event;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain event fired when an availability slot is booked.
 */
public class SlotBookedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String driverId;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String bookingId;
    private final Instant timestamp;

    public SlotBookedEvent(String aggregateId, String tenantId, String driverId,
                          LocalDate date, LocalTime startTime, LocalTime endTime,
                          String bookingId) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId is required");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.date = Objects.requireNonNull(date, "date is required");
        this.startTime = Objects.requireNonNull(startTime, "startTime is required");
        this.endTime = Objects.requireNonNull(endTime, "endTime is required");
        this.bookingId = Objects.requireNonNull(bookingId, "bookingId is required");
        this.timestamp = Instant.now();
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getEventType() {
        return "SlotBooked";
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getDriverId() {
        return driverId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getBookingId() {
        return bookingId;
    }
}
