package com.gogidix.courier.assignmentservice.shared.util;

public final class GeoUtils {

    private static final int EARTH_RADIUS_KM = 6371;

    private GeoUtils() {
    }

    public static double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLatRad = Math.toRadians(lat2 - lat1);
        double deltaLonRad = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    public static double calculateEstimatedDurationMinutes(double distanceKm, double averageSpeedKmh) {
        if (averageSpeedKmh <= 0) {
            throw new IllegalArgumentException("Average speed must be positive");
        }
        return (distanceKm / averageSpeedKmh) * 60.0;
    }

    public static boolean isValidCoordinate(double latitude, double longitude) {
        return latitude >= -90.0 && latitude <= 90.0
                && longitude >= -180.0 && longitude <= 180.0;
    }
}
