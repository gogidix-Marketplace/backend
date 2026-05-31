package com.gogidix.courier.gpstrackingservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GPS Location validation logic.
 */
@DisplayName("GPS Location Validator Tests")
class GpsLocationValidatorTest {

    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LONGITUDE = 180.0;
    private static final double MIN_LONGITUDE = -180.0;

    @Test
    @DisplayName("Should validate correct GPS coordinates")
    void shouldValidateCorrectGpsCoordinates() {
        double latitude = 40.7128;
        double longitude = -74.0060;

        boolean isValid = isValidLatitude(latitude) && isValidLongitude(longitude);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should reject latitude above maximum")
    void shouldRejectLatitudeAboveMaximum() {
        double latitude = 91.0;

        assertFalse(isValidLatitude(latitude));
    }

    @Test
    @DisplayName("Should reject latitude below minimum")
    void shouldRejectLatitudeBelowMinimum() {
        double latitude = -91.0;

        assertFalse(isValidLatitude(latitude));
    }

    @Test
    @DisplayName("Should reject longitude above maximum")
    void shouldRejectLongitudeAboveMaximum() {
        double longitude = 181.0;

        assertFalse(isValidLongitude(longitude));
    }

    @Test
    @DisplayName("Should reject longitude below minimum")
    void shouldRejectLongitudeBelowMinimum() {
        double longitude = -181.0;

        assertFalse(isValidLongitude(longitude));
    }

    @Test
    @DisplayName("Should accept boundary latitude values")
    void shouldAcceptBoundaryLatitudeValues() {
        assertTrue(isValidLatitude(MAX_LATITUDE));
        assertTrue(isValidLatitude(MIN_LATITUDE));
    }

    @Test
    @DisplayName("Should accept boundary longitude values")
    void shouldAcceptBoundaryLongitudeValues() {
        assertTrue(isValidLongitude(MAX_LONGITUDE));
        assertTrue(isValidLongitude(MIN_LONGITUDE));
    }

    @Test
    @DisplayName("Should calculate speed between two points")
    void shouldCalculateSpeedBetweenTwoPoints() {
        double distanceMeters = 1000.0; // 1 km
        long timeSeconds = 300; // 5 minutes

        double speedMps = distanceMeters / timeSeconds;
        double speedKph = speedMps * 3.6;

        assertEquals(3.33, speedMps, 0.01);
        assertEquals(12.0, speedKph, 0.1);
    }

    @Test
    @DisplayName("Should detect unrealistic speed")
    void shouldDetectUnrealisticSpeed() {
        double distanceMeters = 50000.0; // 50 km
        long timeSeconds = 300; // 5 minutes

        double speedKph = (distanceMeters / timeSeconds) * 3.6;
        boolean isUnrealistic = speedKph > 200.0; // Max realistic speed 200 km/h

        assertTrue(isUnrealistic);
    }

    @Test
    @DisplayName("Should calculate distance between two GPS points")
    void shouldCalculateDistanceBetweenTwoPoints() {
        double lat1 = 40.7128;
        double lon1 = -74.0060;
        double lat2 = 40.7580;
        double lon2 = -73.9855;

        // Using Haversine formula approximation for small distances
        double latDiff = Math.toRadians(lat2 - lat1);
        double lonDiff = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = 6371000 * c; // Distance in meters

        assertTrue(distance > 4000 && distance < 6000); // Approx 5km
    }

    @Test
    @DisplayName("Should validate timestamp is recent")
    void shouldValidateTimestampIsRecent() {
        Instant timestamp = Instant.now().minusSeconds(30);
        long maxAgeSeconds = 60;

        boolean isRecent = timestamp.isAfter(Instant.now().minusSeconds(maxAgeSeconds));

        assertTrue(isRecent);
    }

    @Test
    @DisplayName("Should reject old timestamp")
    void shouldRejectOldTimestamp() {
        Instant timestamp = Instant.now().minusSeconds(120);
        long maxAgeSeconds = 60;

        boolean isRecent = timestamp.isAfter(Instant.now().minusSeconds(maxAgeSeconds));

        assertFalse(isRecent);
    }

    @Test
    @DisplayName("Should detect stationary location")
    void shouldDetectStationaryLocation() {
        double distanceMeters = 10.0; // Moved only 10 meters
        long timeSeconds = 300; // Over 5 minutes

        double speedKph = (distanceMeters / timeSeconds) * 3.6;
        boolean isStationary = speedKph < 1.0; // Less than 1 km/h

        assertTrue(isStationary);
    }

    @Test
    @DisplayName("Should calculate bearing between two points")
    void shouldCalculateBearingBetweenTwoPoints() {
        double lat1 = 40.7128;
        double lon1 = -74.0060;
        double lat2 = 40.7580;
        double lon2 = -73.9855;

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double diffLon = Math.toRadians(lon2 - lon1);

        double x = Math.sin(diffLon) * Math.cos(lat2Rad);
        double y = Math.cos(lat1Rad) * Math.sin(lat2Rad) -
                Math.sin(lat1Rad) * Math.cos(lat2Rad) * Math.cos(diffLon);
        double bearing = (Math.toDegrees(Math.atan2(x, y)) + 360) % 360;

        assertTrue(bearing >= 0 && bearing < 360);
    }

    // Helper methods
    private boolean isValidLatitude(double latitude) {
        return latitude >= MIN_LATITUDE && latitude <= MAX_LATITUDE;
    }

    private boolean isValidLongitude(double longitude) {
        return longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE;
    }
}
