package com.gogidix.courier.etaservice.shared.util;

/**
 * Utility class for distance calculations using the Haversine formula.
 */
public final class DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private DistanceCalculator() {
        // Utility class - prevent instantiation
    }

    /**
     * Calculate the distance between two points using the Haversine formula.
     *
     * @param lat1 latitude of first point
     * @param lon1 longitude of first point
     * @param lat2 latitude of second point
     * @param lon2 longitude of second point
     * @return distance in kilometers
     * @throws IllegalArgumentException if coordinates are invalid
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        validateCoordinates(lat1, lon1);
        validateCoordinates(lat2, lon2);

        // Convert latitude and longitude to radians
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        // Haversine formula
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Calculate the bearing between two points.
     *
     * @param lat1 latitude of first point
     * @param lon1 longitude of first point
     * @param lat2 latitude of second point
     * @param lon2 longitude of second point
     * @return bearing in degrees (0-360)
     */
    public static double calculateBearing(double lat1, double lon1, double lat2, double lon2) {
        validateCoordinates(lat1, lon1);
        validateCoordinates(lat2, lon2);

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double dLon = Math.toRadians(lon2 - lon1);

        double x = Math.sin(dLon) * Math.cos(lat2Rad);
        double y = Math.cos(lat1Rad) * Math.sin(lat2Rad)
                - Math.sin(lat1Rad) * Math.cos(lat2Rad) * Math.cos(dLon);

        double bearing = Math.toDegrees(Math.atan2(x, y));
        return (bearing + 360) % 360;
    }

    /**
     * Validate latitude and longitude coordinates.
     *
     * @param lat latitude
     * @param lon longitude
     * @throws IllegalArgumentException if coordinates are invalid
     */
    public static void validateCoordinates(double lat, double lon) {
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90: " + lat);
        }
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180: " + lon);
        }
    }

    /**
     * Check if coordinates are valid.
     *
     * @param lat latitude
     * @param lon longitude
     * @return true if valid, false otherwise
     */
    public static boolean isValidCoordinates(double lat, double lon) {
        try {
            validateCoordinates(lat, lon);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
