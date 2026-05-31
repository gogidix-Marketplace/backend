package com.gogidix.courier.availabilityservice.domain.event;

import com.gogidix.courier.availabilityservice.domain.entity.DriverAvailability;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain event fired when a driver's availability is updated.
 */
public class DriverAvailabilityUpdatedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String driverId;
    private final DriverAvailability.AvailabilityStatus status;
    private final Instant timestamp;

    public DriverAvailabilityUpdatedEvent(String aggregateId, String tenantId,
                                         String driverId, DriverAvailability.AvailabilityStatus status) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId is required");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.status = Objects.requireNonNull(status, "status is required");
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
        return "DriverAvailabilityUpdated";
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

    public DriverAvailability.AvailabilityStatus getStatus() {
        return status;
    }
}
