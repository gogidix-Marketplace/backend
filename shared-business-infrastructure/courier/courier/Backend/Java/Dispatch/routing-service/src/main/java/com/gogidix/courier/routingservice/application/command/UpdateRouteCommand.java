package com.gogidix.courier.routingservice.application.command;

import com.gogidix.courier.routingservice.domain.entity.Route;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Command to update an existing route.
 */
public record UpdateRouteCommand(
        String routeId,
        Double endLatitude,
        Double endLongitude,
        String endAddress,
        Route.RoutePriority priority,
        Route.VehicleType vehicleType,
        Instant startDate,
        Instant endDate,
        String notes,
        String updatedBy
) {
    public UpdateRouteCommand {
        Objects.requireNonNull(routeId, "routeId is required");
        if (routeId.isBlank()) {
            throw new IllegalArgumentException("routeId cannot be blank");
        }
    }
}

/**
 * Command to add waypoints to a route.
 */
record AddWaypointsCommand(
        String routeId,
        List<CreateRouteCommand.WaypointCommand> waypoints,
        String updatedBy
) {
    AddWaypointsCommand {
        Objects.requireNonNull(routeId, "routeId is required");
        Objects.requireNonNull(waypoints, "waypoints cannot be null");
        if (waypoints.isEmpty()) {
            throw new IllegalArgumentException("waypoints cannot be empty");
        }
    }
}

/**
 * Command to reassign a route to a different driver.
 */
record ReassignRouteCommand(
        String routeId,
        String newDriverId,
        String reason,
        String updatedBy
) {
    ReassignRouteCommand {
        Objects.requireNonNull(routeId, "routeId is required");
        Objects.requireNonNull(newDriverId, "newDriverId is required");
        if (routeId.isBlank()) {
            throw new IllegalArgumentException("routeId cannot be blank");
        }
        if (newDriverId.isBlank()) {
            throw new IllegalArgumentException("newDriverId cannot be blank");
        }
    }
}

/**
 * Command to update route status.
 */
record UpdateRouteStatusCommand(
        String routeId,
        Route.RouteStatus status,
        String reason,
        String updatedBy
) {
    UpdateRouteStatusCommand {
        Objects.requireNonNull(routeId, "routeId is required");
        Objects.requireNonNull(status, "status is required");
        if (routeId.isBlank()) {
            throw new IllegalArgumentException("routeId cannot be blank");
        }
    }
}

/**
 * Command to complete a route.
 */
record CompleteRouteCommand(
        String routeId,
        Double actualDistanceMeters,
        Integer actualDurationSeconds,
        String completedBy
) {
    CompleteRouteCommand {
        Objects.requireNonNull(routeId, "routeId is required");
        if (routeId.isBlank()) {
            throw new IllegalArgumentException("routeId cannot be blank");
        }
        if (actualDistanceMeters != null && actualDistanceMeters < 0) {
            throw new IllegalArgumentException("actualDistanceMeters cannot be negative");
        }
        if (actualDurationSeconds != null && actualDurationSeconds < 0) {
            throw new IllegalArgumentException("actualDurationSeconds cannot be negative");
        }
    }
}
