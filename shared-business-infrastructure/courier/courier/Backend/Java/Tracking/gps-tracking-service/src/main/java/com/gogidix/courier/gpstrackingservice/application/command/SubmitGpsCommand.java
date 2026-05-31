package com.gogidix.courier.gpstrackingservice.application.command;

import com.gogidix.courier.gpstrackingservice.domain.entity.GpsLocation;

import java.util.Objects;

/**
 * Command to submit a GPS location update.
 */
public record SubmitGpsCommand(
        String tenantId,
        String driverId,
        String orderId,
        Double latitude,
        Double longitude,
        Double altitude,
        Double accuracy,
        Double speed,
        Double heading,
        Integer batteryLevel,
        GpsLocation.LocationSource locationSource,
        String userId
) {
    public SubmitGpsCommand {
        tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        driverId = Objects.requireNonNull(driverId, "driverId is required");
        latitude = Objects.requireNonNull(latitude, "latitude is required");
        longitude = Objects.requireNonNull(longitude, "longitude is required");

        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("longitude must be between -180 and 180");
        }
        if (altitude != null && altitude < -1000) {
            throw new IllegalArgumentException("altitude must be greater than -1000 meters");
        }
        if (accuracy != null && accuracy < 0) {
            throw new IllegalArgumentException("accuracy must be non-negative");
        }
        if (speed != null && speed < 0) {
            throw new IllegalArgumentException("speed must be non-negative");
        }
        if (batteryLevel != null && (batteryLevel < 0 || batteryLevel > 100)) {
            throw new IllegalArgumentException("batteryLevel must be between 0 and 100");
        }
    }
}
