package com.gogidix.courier.routingservice.application.dto;

import com.gogidix.courier.routingservice.domain.entity.Route;

import java.time.Instant;
import java.util.List;

/**
 * DTO for route responses.
 */
public record RouteResponse(
        String id,
        String tenantId,
        String routeId,
        String driverId,
        List<String> orderIds,
        List<WaypointDTO> waypoints,
        Route.RouteStatus status,
        GeoPointDTO startLocation,
        GeoPointDTO endLocation,
        Double estimatedDistanceMeters,
        Integer estimatedDurationSeconds,
        Double actualDistanceMeters,
        Integer actualDurationSeconds,
        Route.RoutePriority priority,
        Route.VehicleType vehicleType,
        Instant startTime,
        Instant endTime,
        Instant startDate,
        Instant endDate,
        String notes,
        RouteMetadataDTO metadata,
        Instant createdAt,
        Instant updatedAt,
        Instant completedAt,
        int waypointCount,
        int orderCount,
        Double efficiency
) {
    /**
     * DTO for geographic coordinates.
     */
    public record GeoPointDTO(
            Double latitude,
            Double longitude,
            String address,
            String name
    ) {
        public static GeoPointDTO from(Route.GeoPoint geoPoint) {
            if (geoPoint == null) {
                return null;
            }
            return new GeoPointDTO(
                    geoPoint.getLatitude(),
                    geoPoint.getLongitude(),
                    geoPoint.getAddress(),
                    geoPoint.getName()
            );
        }
    }

    /**
     * DTO for route metadata.
     */
    public record RouteMetadataDTO(
            String createdBy,
            String lastModifiedBy,
            Integer optimizationCount,
            Instant lastOptimizationTime,
            String cancellationReason,
            String version
    ) {
        public static RouteMetadataDTO from(Route.RouteMetadata metadata) {
            if (metadata == null) {
                return null;
            }
            return new RouteMetadataDTO(
                    metadata.getCreatedBy(),
                    metadata.getLastModifiedBy(),
                    metadata.getOptimizationCount(),
                    metadata.getLastOptimizationTime(),
                    metadata.getCancellationReason(),
                    metadata.getVersion()
            );
        }
    }
}
