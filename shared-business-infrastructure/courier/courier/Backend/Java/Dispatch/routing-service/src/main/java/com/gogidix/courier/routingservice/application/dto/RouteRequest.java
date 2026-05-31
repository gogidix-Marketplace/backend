package com.gogidix.courier.routingservice.application.dto;

import com.gogidix.courier.routingservice.domain.entity.Route;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

/**
 * DTO for creating or updating a route.
 */
public record RouteRequest(
        @NotBlank(message = "routeId is required")
        String routeId,

        @NotBlank(message = "driverId is required")
        String driverId,

        @NotNull(message = "startLatitude is required")
        @DecimalMin(value = "-90.0", message = "startLatitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "startLatitude must be between -90 and 90")
        Double startLatitude,

        @NotNull(message = "startLongitude is required")
        @DecimalMin(value = "-180.0", message = "startLongitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "startLongitude must be between -180 and 180")
        Double startLongitude,

        String startAddress,

        @DecimalMin(value = "-90.0", message = "endLatitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "endLatitude must be between -90 and 90")
        Double endLatitude,

        @DecimalMin(value = "-180.0", message = "endLongitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "endLongitude must be between -180 and 180")
        Double endLongitude,

        String endAddress,

        Route.VehicleType vehicleType,

        Route.RoutePriority priority,

        Instant startDate,

        Instant endDate,

        String notes,

        List<WaypointDTO> waypoints
) {
}
