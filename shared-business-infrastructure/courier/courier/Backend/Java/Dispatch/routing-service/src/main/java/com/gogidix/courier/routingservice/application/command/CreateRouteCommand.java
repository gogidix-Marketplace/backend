package com.gogidix.courier.routingservice.application.command;

import com.gogidix.courier.routingservice.domain.entity.Route;
import com.gogidix.courier.routingservice.domain.entity.RouteWaypoint;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Command to create a new route.
 */
public record CreateRouteCommand(
        String tenantId,
        String routeId,
        String driverId,
        Double startLatitude,
        Double startLongitude,
        String startAddress,
        Double endLatitude,
        Double endLongitude,
        String endAddress,
        Route.VehicleType vehicleType,
        Route.RoutePriority priority,
        Instant startDate,
        Instant endDate,
        String notes,
        List<WaypointCommand> waypoints,
        String createdBy
) {
    public CreateRouteCommand {
        Objects.requireNonNull(tenantId, "tenantId is required");
        Objects.requireNonNull(routeId, "routeId is required");
        Objects.requireNonNull(driverId, "driverId is required");
        if (routeId.isBlank()) {
            throw new IllegalArgumentException("routeId cannot be blank");
        }
        if (driverId.isBlank()) {
            throw new IllegalArgumentException("driverId cannot be blank");
        }
    }

    /**
     * Command for creating a waypoint.
     */
    public record WaypointCommand(
            String waypointId,
            Double latitude,
            Double longitude,
            String address,
            String orderId,
            String customerId,
            String customerName,
            RouteWaypoint.WaypointType waypointType,
            Integer serviceDurationSeconds,
            Instant timeWindowStart,
            Instant timeWindowEnd,
            Integer priority,
            String notes,
            String contactPhone,
            Integer packageCount,
            Double packageWeightKg,
            String specialInstructions
    ) {
        public WaypointCommand {
            Objects.requireNonNull(latitude, "latitude is required");
            Objects.requireNonNull(longitude, "longitude is required");
            if (latitude < -90 || latitude > 90) {
                throw new IllegalArgumentException("latitude must be between -90 and 90");
            }
            if (longitude < -180 || longitude > 180) {
                throw new IllegalArgumentException("longitude must be between -180 and 180");
            }
        }

        public enum WaypointType {
            PICKUP,
            DELIVERY,
            TRANSFER,
            WAREHOUSE,
            FUEL_STOP,
            REST_STOP,
            CUSTOM
        }
    }
}
