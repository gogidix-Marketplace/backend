package com.gogidix.courier.locationservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LocationEvent entity.
 */
@DisplayName("LocationEvent Entity Tests")
class LocationEventTest {

    @Test
    @DisplayName("Should create location event with valid parameters")
    void shouldCreateLocationEventWithValidParameters() {
        // Given
        String tenantId = "tenant-001";
        String driverId = "driver-001";
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        // When
        LocationEvent event = new LocationEvent(tenantId, driverId, location);

        // Then
        assertNotNull(event.getId());
        assertEquals(tenantId, event.getTenantId());
        assertEquals(driverId, event.getDriverId());
        assertEquals(location, event.getLocation());
        assertEquals(LocationEvent.EventType.LOCATION_UPDATE, event.getEventType());
        assertEquals(LocationEvent.LocationSource.GPS, event.getLocationSource());
        assertNotNull(event.getTimestamp());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    @DisplayName("Should throw when tenantId is null")
    void shouldThrowWhenTenantIdIsNull() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        assertThrows(NullPointerException.class, () ->
            new LocationEvent(null, "driver-001", location)
        );
    }

    @Test
    @DisplayName("Should throw when driverId is null")
    void shouldThrowWhenDriverIdIsNull() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        assertThrows(NullPointerException.class, () ->
            new LocationEvent("tenant-001", null, location)
        );
    }

    @Test
    @DisplayName("Should throw when location is null")
    void shouldThrowWhenLocationIsNull() {
        assertThrows(NullPointerException.class, () ->
            new LocationEvent("tenant-001", "driver-001", null)
        );
    }

    @Test
    @DisplayName("Should update location successfully")
    void shouldUpdateLocationSuccessfully() {
        // Given
        LocationEvent.GeoLocation originalLocation = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent event = new LocationEvent("tenant-001", "driver-001", originalLocation);
        var originalTimestamp = event.getTimestamp();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Sleep interrupted");
        }

        LocationEvent.GeoLocation newLocation = new LocationEvent.GeoLocation(40.7580, -73.9855);

        // When
        event.updateLocation(newLocation);

        // Then
        assertEquals(newLocation, event.getLocation());
        assertTrue(event.getTimestamp().isAfter(originalTimestamp));
    }

    @Test
    @DisplayName("Should throw when updating with null location")
    void shouldThrowWhenUpdatingWithNullLocation() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent event = new LocationEvent("tenant-001", "driver-001", location);

        assertThrows(NullPointerException.class, () -> event.updateLocation(null));
    }

    @Test
    @DisplayName("Should set movement data successfully")
    void shouldSetMovementDataSuccessfully() {
        // Given
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent event = new LocationEvent("tenant-001", "driver-001", location);

        // When
        event.setMovementData(45.5, 180.0);

        // Then
        assertEquals(45.5, event.getSpeed());
        assertEquals(180.0, event.getHeading());
    }

    @Test
    @DisplayName("Should mark as geofence entry")
    void shouldMarkAsGeofenceEntry() {
        // Given
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent event = new LocationEvent("tenant-001", "driver-001", location);

        // When
        event.markAsGeofenceEntry();

        // Then
        assertEquals(LocationEvent.EventType.GEOFENCE_ENTRY, event.getEventType());
    }

    @Test
    @DisplayName("Should mark as geofence exit")
    void shouldMarkAsGeofenceExit() {
        // Given
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent event = new LocationEvent("tenant-001", "driver-001", location);

        // When
        event.markAsGeofenceExit();

        // Then
        assertEquals(LocationEvent.EventType.GEOFENCE_EXIT, event.getEventType());
    }

    @Test
    @DisplayName("Should check if location is recent correctly")
    void shouldCheckIfLocationIsRecentCorrectly() {
        // Given
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent event = new LocationEvent("tenant-001", "driver-001", location);

        // When & Then - event is just created, so it's recent
        assertTrue(event.isRecent(60)); // Less than 60 seconds ago
        assertTrue(event.isRecent(300)); // Less than 5 minutes ago
        assertTrue(event.isRecent(1)); // Just created, so it's recent
    }

    @Test
    @DisplayName("Should calculate distance to another location")
    void shouldCalculateDistanceToAnotherLocation() {
        // Given
        LocationEvent.GeoLocation location1 = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent event = new LocationEvent("tenant-001", "driver-001", location1);

        LocationEvent.GeoLocation location2 = new LocationEvent.GeoLocation(40.7580, -73.9855);

        // When
        double distance = event.distanceTo(location2);

        // Then
        assertTrue(distance > 0);
        assertTrue(distance < 10000); // Should be less than 10km
    }

    @Test
    @DisplayName("Should handle all location sources")
    void shouldHandleAllLocationSources() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        LocationEvent gpsEvent = new LocationEvent("tenant-001", "driver-001", location);
        assertEquals(LocationEvent.LocationSource.GPS, gpsEvent.getLocationSource());

        // Can test other sources if setters are added or factory methods
        assertNotNull(LocationEvent.LocationSource.NETWORK);
        assertNotNull(LocationEvent.LocationSource.PASSIVE);
        assertNotNull(LocationEvent.LocationSource.MANUAL);
        assertNotNull(LocationEvent.LocationSource.BEACON);
    }

    @Test
    @DisplayName("Should handle all event types")
    void shouldHandleAllEventTypes() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        LocationEvent event = new LocationEvent("tenant-001", "driver-001", location);

        assertEquals(LocationEvent.EventType.LOCATION_UPDATE, event.getEventType());

        event.markAsGeofenceEntry();
        assertEquals(LocationEvent.EventType.GEOFENCE_ENTRY, event.getEventType());

        event.markAsGeofenceExit();
        assertEquals(LocationEvent.EventType.GEOFENCE_EXIT, event.getEventType());

        // Verify enum values exist
        assertNotNull(LocationEvent.EventType.IDLE_DETECTED);
        assertNotNull(LocationEvent.EventType.MOVEMENT_RESUMED);
        assertNotNull(LocationEvent.EventType.OFFLINE);
        assertNotNull(LocationEvent.EventType.ONLINE);
    }

    @Test
    @DisplayName("Should create valid GeoLocation")
    void shouldCreateValidGeoLocation() {
        // Given
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        // Then
        assertTrue(location.isValid());
        assertEquals(40.7128, location.latitude());
        assertEquals(-74.0060, location.longitude());
    }

    @Test
    @DisplayName("Should validate GeoLocation bounds")
    void shouldValidateGeoLocationBounds() {
        // Valid locations
        assertTrue(new LocationEvent.GeoLocation(0.0, 0.0).isValid());
        assertTrue(new LocationEvent.GeoLocation(90.0, 180.0).isValid());
        assertTrue(new LocationEvent.GeoLocation(-90.0, -180.0).isValid());
        assertTrue(new LocationEvent.GeoLocation(45.0, -90.0).isValid());

        // Invalid locations
        assertFalse(new LocationEvent.GeoLocation(91.0, 0.0).isValid()); // Latitude too high
        assertFalse(new LocationEvent.GeoLocation(-91.0, 0.0).isValid()); // Latitude too low
        assertFalse(new LocationEvent.GeoLocation(0.0, 181.0).isValid()); // Longitude too high
        assertFalse(new LocationEvent.GeoLocation(0.0, -181.0).isValid()); // Longitude too low
    }

    @Test
    @DisplayName("Should handle null coordinates in validation")
    void shouldHandleNullCoordinatesInValidation() {
        // Given
        LocationEvent.GeoLocation location1 = new LocationEvent.GeoLocation();
        location1.setLatitude(null);
        location1.setLongitude(null);

        LocationEvent.GeoLocation location2 = new LocationEvent.GeoLocation();
        location2.setLatitude(40.7128);
        location2.setLongitude(null);

        LocationEvent.GeoLocation location3 = new LocationEvent.GeoLocation();
        location3.setLatitude(null);
        location3.setLongitude(-74.0060);

        // Then
        assertFalse(location1.isValid());
        assertFalse(location2.isValid());
        assertFalse(location3.isValid());
    }

    @Test
    @DisplayName("Should set and get accuracy")
    void shouldSetAndGetAccuracy() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent event = new LocationEvent("tenant-001", "driver-001", location);
        event.setAccuracy(10.5);

        assertEquals(10.5, event.getAccuracy());
    }

    @Test
    @DisplayName("Should set and get altitude")
    void shouldSetAndGetAltitude() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent event = new LocationEvent("tenant-001", "driver-001", location);
        event.setAltitude(150.5);

        assertEquals(150.5, event.getAltitude());
    }

    @Test
    @DisplayName("Should set and get battery level")
    void shouldSetAndGetBatteryLevel() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent event = new LocationEvent("tenant-001", "driver-001", location);
        event.setBatteryLevel(85);

        assertEquals(85, event.getBatteryLevel());
    }

    @Test
    @DisplayName("Should set tracking id")
    void shouldSetTrackingId() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent event = new LocationEvent("tenant-001", "driver-001", location);
        event.setTrackingId("track-12345");

        assertEquals("track-12345", event.getTrackingId());
    }

    @Test
    @DisplayName("Should calculate zero distance to same location")
    void shouldCalculateZeroDistanceToSameLocation() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent event = new LocationEvent("tenant-001", "driver-001", location);

        double distance = event.distanceTo(location);

        assertEquals(0.0, distance, 0.01);
    }
}
