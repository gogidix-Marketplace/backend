package com.gogidix.courier.locationservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GeofenceEvent entity.
 */
@DisplayName("GeofenceEvent Entity Tests")
class GeofenceEventTest {

    @Test
    @DisplayName("Should create geofence event with valid parameters")
    void shouldCreateGeofenceEventWithValidParameters() {
        // Given
        String tenantId = "tenant-001";
        String driverId = "driver-001";
        String geofenceId = "geofence-001";
        GeofenceEvent.GeofenceEventType eventType = GeofenceEvent.GeofenceEventType.ENTERED;
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        // When
        GeofenceEvent event = new GeofenceEvent(tenantId, driverId, geofenceId, eventType, location);

        // Then
        assertNotNull(event.getId());
        assertEquals(tenantId, event.getTenantId());
        assertEquals(driverId, event.getDriverId());
        assertEquals(geofenceId, event.getGeofenceId());
        assertEquals(eventType, event.getEventType());
        assertEquals(location, event.getLocation());
        assertNotNull(event.getTimestamp());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    @DisplayName("Should throw when tenantId is null")
    void shouldThrowWhenTenantIdIsNull() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        assertThrows(NullPointerException.class, () ->
            new GeofenceEvent(null, "driver-001", "geofence-001",
                GeofenceEvent.GeofenceEventType.ENTERED, location)
        );
    }

    @Test
    @DisplayName("Should throw when driverId is null")
    void shouldThrowWhenDriverIdIsNull() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        assertThrows(NullPointerException.class, () ->
            new GeofenceEvent("tenant-001", null, "geofence-001",
                GeofenceEvent.GeofenceEventType.ENTERED, location)
        );
    }

    @Test
    @DisplayName("Should throw when geofenceId is null")
    void shouldThrowWhenGeofenceIdIsNull() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        assertThrows(NullPointerException.class, () ->
            new GeofenceEvent("tenant-001", "driver-001", null,
                GeofenceEvent.GeofenceEventType.ENTERED, location)
        );
    }

    @Test
    @DisplayName("Should throw when eventType is null")
    void shouldThrowWhenEventTypeIsNull() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        assertThrows(NullPointerException.class, () ->
            new GeofenceEvent("tenant-001", "driver-001", "geofence-001", null, location)
        );
    }

    @Test
    @DisplayName("Should throw when location is null")
    void shouldThrowWhenLocationIsNull() {
        assertThrows(NullPointerException.class, () ->
            new GeofenceEvent("tenant-001", "driver-001", "geofence-001",
                GeofenceEvent.GeofenceEventType.ENTERED, null)
        );
    }

    @Test
    @DisplayName("Should create entry event using factory method")
    void shouldCreateEntryEventUsingFactoryMethod() {
        // Given
        String tenantId = "tenant-001";
        String driverId = "driver-001";
        String geofenceId = "geofence-001";
        String geofenceName = "Warehouse Zone";
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        // When
        GeofenceEvent event = GeofenceEvent.createEntry(tenantId, driverId, geofenceId, geofenceName, location);

        // Then
        assertEquals(tenantId, event.getTenantId());
        assertEquals(driverId, event.getDriverId());
        assertEquals(geofenceId, event.getGeofenceId());
        assertEquals(geofenceName, event.getGeofenceName());
        assertEquals(GeofenceEvent.GeofenceEventType.ENTERED, event.getEventType());
        assertEquals(location, event.getLocation());
        assertTrue(event.isEntry());
        assertFalse(event.isExit());
    }

    @Test
    @DisplayName("Should create exit event using factory method")
    void shouldCreateExitEventUsingFactoryMethod() {
        // Given
        String tenantId = "tenant-001";
        String driverId = "driver-001";
        String geofenceId = "geofence-001";
        String geofenceName = "Warehouse Zone";
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7580, -73.9855);

        // When
        GeofenceEvent event = GeofenceEvent.createExit(tenantId, driverId, geofenceId, geofenceName, location);

        // Then
        assertEquals(tenantId, event.getTenantId());
        assertEquals(driverId, event.getDriverId());
        assertEquals(geofenceId, event.getGeofenceId());
        assertEquals(geofenceName, event.getGeofenceName());
        assertEquals(GeofenceEvent.GeofenceEventType.EXITED, event.getEventType());
        assertEquals(location, event.getLocation());
        assertFalse(event.isEntry());
        assertTrue(event.isExit());
    }

    @Test
    @DisplayName("Should create dwell event using factory method")
    void shouldCreateDwellEventUsingFactoryMethod() {
        // Given
        String tenantId = "tenant-001";
        String driverId = "driver-001";
        String geofenceId = "geofence-001";
        String geofenceName = "No Parking Zone";
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        Long dwellTimeMs = 300000L; // 5 minutes

        // When
        GeofenceEvent event = GeofenceEvent.createDwell(tenantId, driverId, geofenceId, geofenceName, location, dwellTimeMs);

        // Then
        assertEquals(tenantId, event.getTenantId());
        assertEquals(driverId, event.getDriverId());
        assertEquals(geofenceId, event.getGeofenceId());
        assertEquals(geofenceName, event.getGeofenceName());
        assertEquals(GeofenceEvent.GeofenceEventType.DWELL, event.getEventType());
        assertEquals(location, event.getLocation());
        assertEquals(Map.of("dwellTimeMs", dwellTimeMs), event.getAttributes());
    }

    @Test
    @DisplayName("Should set attribute")
    void shouldSetAttribute() {
        // Given
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);
        GeofenceEvent event = new GeofenceEvent("tenant-001", "driver-001", "geofence-001",
            GeofenceEvent.GeofenceEventType.ENTERED, location);

        // When
        event.setAttribute("speed", 45.5);

        // Then
        assertNotNull(event.getAttributes());
        assertEquals(45.5, event.getAttributes().get("speed"));
    }

    @Test
    @DisplayName("Should identify entry events correctly")
    void shouldIdentifyEntryEventsCorrectly() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        GeofenceEvent entryEvent = new GeofenceEvent("tenant-001", "driver-001", "geofence-001",
            GeofenceEvent.GeofenceEventType.ENTERED, location);
        assertTrue(entryEvent.isEntry());
        assertFalse(entryEvent.isExit());

        GeofenceEvent exitEvent = new GeofenceEvent("tenant-001", "driver-001", "geofence-001",
            GeofenceEvent.GeofenceEventType.EXITED, location);
        assertFalse(exitEvent.isEntry());
        assertTrue(exitEvent.isExit());
    }

    @Test
    @DisplayName("Should handle all geofence event types")
    void shouldHandleAllGeofenceEventTypes() {
        LocationEvent.GeoLocation location = new LocationEvent.GeoLocation(40.7128, -74.0060);

        GeofenceEvent enteredEvent = new GeofenceEvent("tenant-001", "driver-001", "geofence-001",
            GeofenceEvent.GeofenceEventType.ENTERED, location);
        assertEquals(GeofenceEvent.GeofenceEventType.ENTERED, enteredEvent.getEventType());

        GeofenceEvent exitedEvent = new GeofenceEvent("tenant-001", "driver-001", "geofence-001",
            GeofenceEvent.GeofenceEventType.EXITED, location);
        assertEquals(GeofenceEvent.GeofenceEventType.EXITED, exitedEvent.getEventType());

        GeofenceEvent dwellEvent = new GeofenceEvent("tenant-001", "driver-001", "geofence-001",
            GeofenceEvent.GeofenceEventType.DWELL, location);
        assertEquals(GeofenceEvent.GeofenceEventType.DWELL, dwellEvent.getEventType());

        GeofenceEvent transitionEvent = new GeofenceEvent("tenant-001", "driver-001", "geofence-001",
            GeofenceEvent.GeofenceEventType.TRANSITION, location);
        assertEquals(GeofenceEvent.GeofenceEventType.TRANSITION, transitionEvent.getEventType());
    }

    @Test
    @DisplayName("Should create entry and exit for same geofence")
    void shouldCreateEntryAndExitForSameGeofence() {
        // Given
        String tenantId = "tenant-001";
        String driverId = "driver-001";
        String geofenceId = "geofence-001";
        String geofenceName = "Main Depot";
        LocationEvent.GeoLocation entryLocation = new LocationEvent.GeoLocation(40.7128, -74.0060);
        LocationEvent.GeoLocation exitLocation = new LocationEvent.GeoLocation(40.7580, -73.9855);

        // When
        GeofenceEvent entryEvent = GeofenceEvent.createEntry(tenantId, driverId, geofenceId, geofenceName, entryLocation);
        GeofenceEvent exitEvent = GeofenceEvent.createExit(tenantId, driverId, geofenceId, geofenceName, exitLocation);

        // Then
        assertEquals(geofenceId, entryEvent.getGeofenceId());
        assertEquals(geofenceId, exitEvent.getGeofenceId());
        assertEquals(geofenceName, entryEvent.getGeofenceName());
        assertEquals(geofenceName, exitEvent.getGeofenceName());
        assertTrue(entryEvent.getTimestamp().isBefore(exitEvent.getTimestamp()) ||
                   entryEvent.getTimestamp().equals(exitEvent.getTimestamp()));
    }
}
