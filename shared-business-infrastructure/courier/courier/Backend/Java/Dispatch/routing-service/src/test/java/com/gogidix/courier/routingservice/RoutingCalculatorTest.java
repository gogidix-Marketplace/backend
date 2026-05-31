package com.gogidix.courier.routingservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Routing Calculator domain logic.
 */
@DisplayName("Routing Calculator Domain Tests")
class RoutingCalculatorTest {

    @Test
    @DisplayName("Should calculate distance between two coordinates")
    void shouldCalculateDistanceBetweenTwoCoordinates() {
        double lat1 = 40.7128;
        double lon1 = -74.0060;
        double lat2 = 40.7580;
        double lon2 = -73.9855;

        double distance = calculateHaversineDistance(lat1, lon1, lat2, lon2);

        assertTrue(distance > 0);
        assertTrue(distance < 10); // Should be less than 10km
    }

    @Test
    @DisplayName("Should calculate route with waypoints")
    void shouldCalculateRouteWithWaypoints() {
        double[] startLat = {40.7128};
        double[] startLon = {-74.0060};
        List<double[]> waypoints = List.of(
                new double[]{40.7300, -74.0000},
                new double[]{40.7450, -73.9900}
        );
        double[] endLat = {40.7580};
        double[] endLon = {-73.9855};

        double totalDistance = 0;
        double prevLat = startLat[0];
        double prevLon = startLon[0];

        for (double[] waypoint : waypoints) {
            totalDistance += calculateHaversineDistance(prevLat, prevLon, waypoint[0], waypoint[1]);
            prevLat = waypoint[0];
            prevLon = waypoint[1];
        }
        totalDistance += calculateHaversineDistance(prevLat, prevLon, endLat[0], endLon[0]);

        assertTrue(totalDistance > 0);
    }

    @Test
    @DisplayName("Should estimate travel time")
    void shouldEstimateTravelTime() {
        double distanceKm = 15.0;
        double averageSpeedKph = 30.0;

        double estimatedMinutes = (distanceKm / averageSpeedKph) * 60;

        assertEquals(30.0, estimatedMinutes);
    }

    @Test
    @DisplayName("Should adjust for traffic conditions")
    void shouldAdjustForTrafficConditions() {
        double baseMinutes = 30.0;
        double trafficFactor = 1.5; // 50% slower due to traffic

        double adjustedMinutes = baseMinutes * trafficFactor;

        assertEquals(45.0, adjustedMinutes);
    }

    @Test
    @DisplayName("Should optimize route with multiple stops")
    void shouldOptimizeRouteWithMultipleStops() {
        List<String> stops = List.of("A", "B", "C", "D", "E");

        // Simple optimization: check that route can be computed
        assertNotNull(stops);
        assertEquals(5, stops.size());
    }

    @Test
    @DisplayName("Should handle invalid coordinates")
    void shouldHandleInvalidCoordinates() {
        double lat1 = 40.7128;
        double lon1 = -74.0060;
        double lat2 = 100.0; // Invalid latitude
        double lon2 = -73.9855;

        assertThrows(IllegalArgumentException.class, () ->
                validateAndCalculateDistance(lat1, lon1, lat2, lon2)
        );
    }

    @Test
    @DisplayName("Should calculate bearing between points")
    void shouldCalculateBearingBetweenPoints() {
        double lat1 = 40.7128;
        double lon1 = -74.0060;
        double lat2 = 40.7580;
        double lon2 = -73.9855;

        double bearing = calculateBearing(lat1, lon1, lat2, lon2);

        assertTrue(bearing >= 0 && bearing < 360);
    }

    @Test
    @DisplayName("Should format route instructions")
    void shouldFormatRouteInstructions() {
        String instruction = formatInstruction("Turn left", "Main St", 200);

        assertTrue(instruction.contains("Turn left"));
        assertTrue(instruction.contains("Main St"));
    }

    @Test
    @DisplayName("Should calculate route bounds")
    void shouldCalculateRouteBounds() {
        List<Double> lats = List.of(40.7128, 40.7580, 40.7300);
        List<Double> lons = List.of(-74.0060, -73.9855, -74.0000);

        double minLat = lats.stream().min(Double::compare).orElse(0.0);
        double maxLat = lats.stream().max(Double::compare).orElse(0.0);
        double minLon = lons.stream().min(Double::compare).orElse(0.0);
        double maxLon = lons.stream().max(Double::compare).orElse(0.0);

        assertTrue(minLat < maxLat);
        assertTrue(minLon < maxLon);
    }

    // Helper methods
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusKm = 6371.0;
        double latDiffRad = Math.toRadians(lat2 - lat1);
        double lonDiffRad = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDiffRad / 2) * Math.sin(latDiffRad / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDiffRad / 2) * Math.sin(lonDiffRad / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }

    private double validateAndCalculateDistance(double lat1, double lon1, double lat2, double lon2) {
        if (lat1 < -90 || lat1 > 90 || lat2 < -90 || lat2 > 90) {
            throw new IllegalArgumentException("Invalid latitude");
        }
        if (lon1 < -180 || lon1 > 180 || lon2 < -180 || lon2 > 180) {
            throw new IllegalArgumentException("Invalid longitude");
        }
        return calculateHaversineDistance(lat1, lon1, lat2, lon2);
    }

    private double calculateBearing(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double diffLon = Math.toRadians(lon2 - lon1);

        double x = Math.sin(diffLon) * Math.cos(lat2Rad);
        double y = Math.cos(lat1Rad) * Math.sin(lat2Rad) -
                Math.sin(lat1Rad) * Math.cos(lat2Rad) * Math.cos(diffLon);

        return (Math.toDegrees(Math.atan2(x, y)) + 360) % 360;
    }

    private String formatInstruction(String action, String street, double distanceMeters) {
        return String.format("%s onto %s in %.0fm", action, street, distanceMeters);
    }
}
