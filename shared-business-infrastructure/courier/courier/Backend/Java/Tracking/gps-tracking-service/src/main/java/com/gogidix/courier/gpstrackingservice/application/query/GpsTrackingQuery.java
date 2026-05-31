package com.gogidix.courier.gpstrackingservice.application.query;

import java.time.Instant;
import java.util.Objects;

/**
 * Query for GPS tracking data.
 */
public record GpsTrackingQuery(
        String tenantId,
        String driverId,
        String orderId,
        Instant startTime,
        Instant endTime,
        Integer page,
        Integer size,
        String sortBy,
        String sortDirection
) {
    public GpsTrackingQuery {
        if (page != null && page < 0) {
            throw new IllegalArgumentException("page must be non-negative");
        }
        if (size != null && (size < 1 || size > 1000)) {
            throw new IllegalArgumentException("size must be between 1 and 1000");
        }
        if (endTime != null && startTime != null && endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
    }

    /**
     * Create a query for current driver location.
     */
    public static GpsTrackingQuery forCurrentLocation(String tenantId, String driverId) {
        return new GpsTrackingQuery(tenantId, driverId, null, null, null, null, null, null, null);
    }

    /**
     * Create a query for location history.
     */
    public static GpsTrackingQuery forHistory(String tenantId, String driverId, Instant startTime, Instant endTime) {
        return new GpsTrackingQuery(tenantId, driverId, null, startTime, endTime, 0, 100, "timestamp", "ASC");
    }

    /**
     * Create a query for nearby drivers.
     */
    public static GpsTrackingQuery forNearby(String tenantId) {
        return new GpsTrackingQuery(tenantId, null, null, null, null, null, null, null, null);
    }
}
