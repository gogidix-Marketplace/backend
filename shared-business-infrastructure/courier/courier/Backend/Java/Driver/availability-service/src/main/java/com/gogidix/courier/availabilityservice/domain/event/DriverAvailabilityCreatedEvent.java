package com.gogidix.courier.availabilityservice.domain.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain event fired when a driver's availability is created.
 */
public class DriverAvailabilityCreatedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String driverId;
    private final String date;
    private final Instant timestamp;

    public DriverAvailabilityCreatedEvent(String aggregateId, String tenantId,
                                         String driverId, String date) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId is required");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.date = Objects.requireNonNull(date, "date is required");
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
        return "DriverAvailabilityCreated";
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

    public String getDate() {
        return date;
    }
}
