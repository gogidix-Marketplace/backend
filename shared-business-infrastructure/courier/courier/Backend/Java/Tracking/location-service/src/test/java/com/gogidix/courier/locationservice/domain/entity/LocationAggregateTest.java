package com.gogidix.courier.locationservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LocationAggregate entity.
 */
@DisplayName("LocationAggregate Entity Tests")
class LocationAggregateTest {

    @Test
    @DisplayName("Should create location aggregate with valid parameters")
    void shouldCreateLocationAggregateWithValidParameters() {
        // Given
        String tenantId = "tenant-001";
        String driverId = "driver-001";
        Instant periodStart = Instant.now().minusSeconds(3600);
        Instant periodEnd = Instant.now();
        LocationAggregate.AggregateType aggregateType = LocationAggregate.AggregateType.HOURLY;

        // When
        LocationAggregate aggregate = new LocationAggregate(tenantId, driverId, periodStart, periodEnd, aggregateType);

        // Then
        assertNotNull(aggregate.getId());
        assertEquals(tenantId, aggregate.getTenantId());
        assertEquals(driverId, aggregate.getDriverId());
        assertEquals(periodStart, aggregate.getPeriodStart());
        assertEquals(periodEnd, aggregate.getPeriodEnd());
        assertEquals(aggregateType, aggregate.getAggregateType());
        assertEquals(0.0, aggregate.getTotalDistance());
        assertEquals(0L, aggregate.getTotalDuration());
        assertEquals(0.0, aggregate.getAverageSpeed());
        assertEquals(0.0, aggregate.getMaxSpeed());
        assertTrue(aggregate.getWaypoints().isEmpty());
        assertEquals(0L, aggregate.getIdleTime());
        assertEquals(0, aggregate.getBatteryDrained());
        assertNotNull(aggregate.getCreatedAt());
        assertNotNull(aggregate.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw when tenantId is null")
    void shouldThrowWhenTenantIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            new LocationAggregate(null, "driver-001", Instant.now(), Instant.now(),
                LocationAggregate.AggregateType.HOURLY)
        );
    }

    @Test
    @DisplayName("Should throw when driverId is null")
    void shouldThrowWhenDriverIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            new LocationAggregate("tenant-001", null, Instant.now(), Instant.now(),
                LocationAggregate.AggregateType.HOURLY)
        );
    }

    @Test
    @DisplayName("Should throw when periodStart is null")
    void shouldThrowWhenPeriodStartIsNull() {
        assertThrows(NullPointerException.class, () ->
            new LocationAggregate("tenant-001", "driver-001", null, Instant.now(),
                LocationAggregate.AggregateType.HOURLY)
        );
    }

    @Test
    @DisplayName("Should throw when periodEnd is null")
    void shouldThrowWhenPeriodEndIsNull() {
        assertThrows(NullPointerException.class, () ->
            new LocationAggregate("tenant-001", "driver-001", Instant.now(), null,
                LocationAggregate.AggregateType.HOURLY)
        );
    }

    @Test
    @DisplayName("Should throw when aggregateType is null")
    void shouldThrowWhenAggregateTypeIsNull() {
        assertThrows(NullPointerException.class, () ->
            new LocationAggregate("tenant-001", "driver-001", Instant.now(), Instant.now(), null)
        );
    }

    @Test
    @DisplayName("Should add waypoint successfully")
    void shouldAddWaypointSuccessfully() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.HOURLY);
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        Instant timestamp = Instant.now();

        // When
        aggregate.addWaypoint(location, timestamp);

        // Then
        assertEquals(1, aggregate.getWaypoints().size());
        assertEquals(location, aggregate.getWaypoints().get(0).getLocation());
        assertEquals(timestamp, aggregate.getWaypoints().get(0).getTimestamp());
        assertEquals(location, aggregate.getStartLocation());
        assertEquals(location, aggregate.getEndLocation());
    }

    @Test
    @DisplayName("Should update start and end locations with waypoints")
    void shouldUpdateStartAndEndLocationsWithWaypoints() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.TRIP);
        LocationEvent.GeoLocation startLoc = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent.GeoLocation midLoc = new LocationEvent.GeoLocation(40.7300, -74.0000);
        LocationEvent.GeoLocation endLoc = new LocationEvent.GeoLocation(40.7580, -73.9855);

        // When
        aggregate.addWaypoint(startLoc, Instant.now().minusSeconds(3600));
        aggregate.addWaypoint(midLoc, Instant.now().minusSeconds(1800));
        aggregate.addWaypoint(endLoc, Instant.now());

        // Then
        assertEquals(3, aggregate.getWaypoints().size());
        assertEquals(startLoc, aggregate.getStartLocation());
        assertEquals(endLoc, aggregate.getEndLocation());
    }

    @Test
    @DisplayName("Should update metrics correctly")
    void shouldUpdateMetricsCorrectly() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.HOURLY);

        // When
        aggregate.updateMetrics(5000.0, 300L); // 5km in 300 seconds
        aggregate.updateMetrics(3000.0, 200L); // 3km in 200 seconds

        // Then
        assertEquals(8000.0, aggregate.getTotalDistance());
        assertEquals(500L, aggregate.getTotalDuration());
        // Average speed = (8000m / 500s) * 3.6 = 57.6 km/h
        assertEquals(57.6, aggregate.getAverageSpeed(), 0.1);
    }

    @Test
    @DisplayName("Should update max speed correctly")
    void shouldUpdateMaxSpeedCorrectly() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.HOURLY);

        // When
        aggregate.updateMaxSpeed(45.0);
        aggregate.updateMaxSpeed(60.0);
        aggregate.updateMaxSpeed(50.0);

        // Then
        assertEquals(60.0, aggregate.getMaxSpeed());
    }

    @Test
    @DisplayName("Should initialize max speed when null")
    void shouldInitializeMaxSpeedWhenNull() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.HOURLY);

        // Then - initial max speed is 0
        assertEquals(0.0, aggregate.getMaxSpeed());

        // When
        aggregate.updateMaxSpeed(55.0);

        // Then
        assertEquals(55.0, aggregate.getMaxSpeed());
    }

    @Test
    @DisplayName("Should add idle time")
    void shouldAddIdleTime() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.HOURLY);

        // When
        aggregate.addIdleTime(300L); // 5 minutes
        aggregate.addIdleTime(600L); // 10 minutes

        // Then
        assertEquals(900L, aggregate.getIdleTime());
    }

    @Test
    @DisplayName("Should calculate battery drain")
    void shouldCalculateBatteryDrain() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.TRIP);

        // When
        aggregate.calculateBatteryDrain(100, 75);

        // Then
        assertEquals(25, aggregate.getBatteryDrained());
    }

    @Test
    @DisplayName("Should not calculate battery drain when values are null")
    void shouldNotCalculateBatteryDrainWhenValuesAreNull() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.TRIP);

        // When
        aggregate.calculateBatteryDrain(null, 75);
        aggregate.calculateBatteryDrain(100, null);
        aggregate.calculateBatteryDrain(null, null);

        // Then
        assertEquals(0, aggregate.getBatteryDrained());
    }

    @Test
    @DisplayName("Should get average speed in km/h")
    void shouldGetAverageSpeedInKmh() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.HOURLY);
        aggregate.updateMetrics(10000.0, 1000L); // 10km in 1000 seconds

        // When
        Double avgSpeed = aggregate.getAverageSpeedKmh();

        // Then
        assertEquals(36.0, avgSpeed, 0.1); // (10000m/1000s) * 3.6 = 36 km/h
    }

    @Test
    @DisplayName("Should get total distance in km")
    void shouldGetTotalDistanceInKm() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.HOURLY);
        aggregate.updateMetrics(15000.0, 1000L); // 15km in 1000 seconds

        // When
        Double distanceKm = aggregate.getTotalDistanceKm();

        // Then
        assertEquals(15.0, distanceKm, 0.01);
    }

    @Test
    @DisplayName("Should return zero distance when null")
    void shouldReturnZeroDistanceWhenNull() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.HOURLY);

        // When
        Double distanceKm = aggregate.getTotalDistanceKm();

        // Then
        assertEquals(0.0, distanceKm);
    }

    @Test
    @DisplayName("Should handle all aggregate types")
    void shouldHandleAllAggregateTypes() {
        assertNotNull(LocationAggregate.AggregateType.HOURLY);
        assertNotNull(LocationAggregate.AggregateType.DAILY);
        assertNotNull(LocationAggregate.AggregateType.TRIP);
        assertNotNull(LocationAggregate.AggregateType.SHIFT);
        assertNotNull(LocationAggregate.AggregateType.CUSTOM);
    }

    @Test
    @DisplayName("Should create HOURLY aggregate")
    void shouldCreateHourlyAggregate() {
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.HOURLY);
        assertEquals(LocationAggregate.AggregateType.HOURLY, aggregate.getAggregateType());
    }

    @Test
    @DisplayName("Should create DAILY aggregate")
    void shouldCreateDailyAggregate() {
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(86400), Instant.now(), LocationAggregate.AggregateType.DAILY);
        assertEquals(LocationAggregate.AggregateType.DAILY, aggregate.getAggregateType());
    }

    @Test
    @DisplayName("Should create TRIP aggregate")
    void shouldCreateTripAggregate() {
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(1800), Instant.now(), LocationAggregate.AggregateType.TRIP);
        assertEquals(LocationAggregate.AggregateType.TRIP, aggregate.getAggregateType());
    }

    @Test
    @DisplayName("Should update timestamp on modifications")
    void shouldUpdateTimestampOnModifications() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.HOURLY);
        var initialUpdatedAt = aggregate.getUpdatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Sleep interrupted");
        }

        // When
        aggregate.addWaypoint(new LocationEvent.GeoLocation(40.7128, -74.0060), Instant.now());

        // Then
        assertTrue(aggregate.getUpdatedAt().isAfter(initialUpdatedAt));
    }

    @Test
    @DisplayName("Should calculate average speed with zero duration")
    void shouldCalculateAverageSpeedWithZeroDuration() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.HOURLY);
        aggregate.updateMetrics(5000.0, 0L);

        // When
        Double avgSpeed = aggregate.getAverageSpeed();

        // Then
        assertEquals(0.0, avgSpeed);
    }

    @Test
    @DisplayName("Should handle waypoints with sequence numbers")
    void shouldHandleWaypointsWithSequenceNumbers() {
        // Given
        LocationAggregate aggregate = new LocationAggregate("tenant-001", "driver-001",
            Instant.now().minusSeconds(3600), Instant.now(), LocationAggregate.AggregateType.TRIP);

        // When
        LocationEvent.GeoLocation loc1 = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent.GeoLocation loc2 = new LocationEvent.GeoLocation(40.7300, -74.0000);
        LocationEvent.GeoLocation loc3 = new LocationEvent.GeoLocation(40.7580, -73.9855);

        aggregate.addWaypoint(loc1, Instant.now().minusSeconds(3600));
        aggregate.addWaypoint(loc2, Instant.now().minusSeconds(1800));
        aggregate.addWaypoint(loc3, Instant.now());

        // Then
        assertEquals(3, aggregate.getWaypoints().size());
        assertEquals(loc1, aggregate.getWaypoints().get(0).getLocation());
        assertEquals(loc2, aggregate.getWaypoints().get(1).getLocation());
        assertEquals(loc3, aggregate.getWaypoints().get(2).getLocation());
    }
}
