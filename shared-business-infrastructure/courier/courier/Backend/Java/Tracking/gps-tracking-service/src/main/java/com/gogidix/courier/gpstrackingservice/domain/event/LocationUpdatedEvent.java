package com.gogidix.courier.gpstrackingservice.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event fired when a GPS location is updated.
 */
public class LocationUpdatedEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final String tenantId;
    private final String driverId;
    private final double latitude;
    private final double longitude;
    private final Double altitude;
    private final Double accuracy;
    private final Double speed;
    private final Double heading;
    private final Instant occurredAt;

    public LocationUpdatedEvent(
            String aggregateId,
            String tenantId,
            String driverId,
            double latitude,
            double longitude,
            Double altitude,
            Double accuracy,
            Double speed,
            Double heading) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.driverId = Objects.requireNonNull(driverId, "driverId cannot be null");
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.speed = speed;
        this.heading = heading;
        this.occurredAt = Instant.now();
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
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getDriverId() {
        return driverId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public Double getSpeed() {
        return speed;
    }

    public Double getHeading() {
        return heading;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationUpdatedEvent that = (LocationUpdatedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "LocationUpdatedEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
