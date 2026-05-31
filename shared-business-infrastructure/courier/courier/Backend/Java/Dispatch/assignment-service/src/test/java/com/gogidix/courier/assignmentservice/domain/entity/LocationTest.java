package com.gogidix.courier.assignmentservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Location value object.
 */
@DisplayName("Location Value Object Tests")
class LocationTest {

    @Test
    @DisplayName("Should create location with coordinates")
    void shouldCreateLocationWithCoordinates() {
        DriverAssignment.Location location = new DriverAssignment.Location(40.7128, -74.0060);

        assertEquals(40.7128, location.getLatitude());
        assertEquals(-74.0060, location.getLongitude());
    }

    @Test
    @DisplayName("Should create location with coordinates and address")
    void shouldCreateLocationWithCoordinatesAndAddress() {
        DriverAssignment.Location location = new DriverAssignment.Location(40.7128, -74.0060, "123 Main St");

        assertEquals(40.7128, location.getLatitude());
        assertEquals(-74.0060, location.getLongitude());
        assertEquals("123 Main St", location.getAddress());
    }

    @Test
    @DisplayName("Should create empty location using default constructor")
    void shouldCreateEmptyLocation() {
        DriverAssignment.Location location = new DriverAssignment.Location();

        assertNull(location.getLatitude());
        assertNull(location.getLongitude());
        assertNull(location.getAddress());
    }

    @Test
    @DisplayName("Should set and get latitude")
    void shouldSetAndGetLatitude() {
        DriverAssignment.Location location = new DriverAssignment.Location();
        location.setLatitude(51.5074);

        assertEquals(51.5074, location.getLatitude());
    }

    @Test
    @DisplayName("Should set and get longitude")
    void shouldSetAndGetLongitude() {
        DriverAssignment.Location location = new DriverAssignment.Location();
        location.setLongitude(-0.1278);

        assertEquals(-0.1278, location.getLongitude());
    }

    @Test
    @DisplayName("Should set and get address")
    void shouldSetAndGetAddress() {
        DriverAssignment.Location location = new DriverAssignment.Location();
        location.setAddress("456 Oak Avenue");

        assertEquals("456 Oak Avenue", location.getAddress());
    }

    @Test
    @DisplayName("Should set and get city")
    void shouldSetAndGetCity() {
        DriverAssignment.Location location = new DriverAssignment.Location();
        location.setCity("New York");

        assertEquals("New York", location.getCity());
    }

    @Test
    @DisplayName("Should set and get postal code")
    void shouldSetAndGetPostalCode() {
        DriverAssignment.Location location = new DriverAssignment.Location();
        location.setPostalCode("10001");

        assertEquals("10001", location.getPostalCode());
    }

    @Test
    @DisplayName("Should set and get country")
    void shouldSetAndGetCountry() {
        DriverAssignment.Location location = new DriverAssignment.Location();
        location.setCountry("USA");

        assertEquals("USA", location.getCountry());
    }

    @Test
    @DisplayName("Should calculate distance to another location - NYC to NYC")
    void shouldCalculateDistanceToAnotherLocation() {
        DriverAssignment.Location location1 = new DriverAssignment.Location(40.7128, -74.0060);
        DriverAssignment.Location location2 = new DriverAssignment.Location(40.7580, -73.9855);

        Double distance = location1.distanceTo(location2);

        assertNotNull(distance);
        assertTrue(distance > 0);
        assertTrue(distance < 10); // Should be less than 10km within NYC
    }

    @Test
    @DisplayName("Should calculate same location distance as zero")
    void shouldCalculateSameLocationDistanceAsZero() {
        DriverAssignment.Location location = new DriverAssignment.Location(40.7128, -74.0060);

        Double distance = location.distanceTo(location);

        assertNotNull(distance);
        assertEquals(0.0, distance, 0.001);
    }

    @Test
    @DisplayName("Should calculate long distance correctly - NYC to LA")
    void shouldCalculateLongDistanceCorrectly() {
        DriverAssignment.Location nyc = new DriverAssignment.Location(40.7128, -74.0060);
        DriverAssignment.Location la = new DriverAssignment.Location(34.0522, -118.2437);

        Double distance = nyc.distanceTo(la);

        assertNotNull(distance);
        assertTrue(distance > 3900); // Approximately 3944 km
        assertTrue(distance < 4000);
    }

    @Test
    @DisplayName("Should return null when calculating distance to null location")
    void shouldReturnNullWhenDistanceToNull() {
        DriverAssignment.Location location = new DriverAssignment.Location(40.7128, -74.0060);

        assertNull(location.distanceTo(null));
    }

    @Test
    @DisplayName("Should return null when this location has null coordinates")
    void shouldReturnNullWhenThisLocationHasNullCoordinates() {
        DriverAssignment.Location location1 = new DriverAssignment.Location();
        location1.setLatitude(40.7128);
        // longitude is null
        DriverAssignment.Location location2 = new DriverAssignment.Location(40.7580, -73.9855);

        assertNull(location1.distanceTo(location2));
    }

    @Test
    @DisplayName("Should return null when other location has null coordinates")
    void shouldReturnNullWhenOtherLocationHasNullCoordinates() {
        DriverAssignment.Location location1 = new DriverAssignment.Location(40.7128, -74.0060);
        DriverAssignment.Location location2 = new DriverAssignment.Location();
        location2.setLatitude(40.7580);
        // longitude is null

        assertNull(location1.distanceTo(location2));
    }

    @Test
    @DisplayName("Should create fully populated location")
    void shouldCreateFullyPopulatedLocation() {
        DriverAssignment.Location location = new DriverAssignment.Location();
        location.setLatitude(48.8566);
        location.setLongitude(2.3522);
        location.setAddress("10 Champs-Elysees");
        location.setCity("Paris");
        location.setPostalCode("75008");
        location.setCountry("France");

        assertEquals(48.8566, location.getLatitude());
        assertEquals(2.3522, location.getLongitude());
        assertEquals("10 Champs-Elysees", location.getAddress());
        assertEquals("Paris", location.getCity());
        assertEquals("75008", location.getPostalCode());
        assertEquals("France", location.getCountry());
    }

    @Test
    @DisplayName("Should handle negative coordinates")
    void shouldHandleNegativeCoordinates() {
        DriverAssignment.Location location = new DriverAssignment.Location(-33.8688, 151.2093);

        assertEquals(-33.8688, location.getLatitude());
        assertEquals(151.2093, location.getLongitude());
    }

    @Test
    @DisplayName("Should handle coordinates at equator and prime meridian")
    void shouldHandleEquatorAndPrimeMeridian() {
        DriverAssignment.Location location = new DriverAssignment.Location(0.0, 0.0);

        assertEquals(0.0, location.getLatitude());
        assertEquals(0.0, location.getLongitude());
    }

    @Test
    @DisplayName("Should handle international date line")
    void shouldHandleInternationalDateLine() {
        DriverAssignment.Location location = new DriverAssignment.Location(21.3069, -157.8583);

        assertEquals(21.3069, location.getLatitude());
        assertEquals(-157.8583, location.getLongitude());
    }
}
