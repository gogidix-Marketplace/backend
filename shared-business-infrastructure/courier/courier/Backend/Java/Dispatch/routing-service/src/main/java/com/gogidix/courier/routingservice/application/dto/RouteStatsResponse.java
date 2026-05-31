package com.gogidix.courier.routingservice.application.dto;

import com.gogidix.courier.routingservice.domain.entity.Route;

import java.time.Instant;
import java.util.Map;

/**
 * DTO for route statistics response.
 */
public record RouteStatsResponse(
        Instant fromDate,
        Instant toDate,
        Long totalRoutes,
        Long completedRoutes,
        Long inProgressRoutes,
        Long pendingRoutes,
        Long cancelledRoutes,
        Double totalDistanceMeters,
        Double totalDurationSeconds,
        Double averageDistanceMeters,
        Double averageDurationSeconds,
        Double averageEfficiency,
        Long totalWaypoints,
        Long totalOrders,
        Double averageWaypointsPerRoute,
        Double averageOrdersPerRoute,
        Map<Route.RouteStatus, Long> routesByStatus,
        Map<Route.RoutePriority, Long> routesByPriority,
        Map<Route.VehicleType, Long> routesByVehicleType,
        TopDriverStats topDriver
) {
    /**
     * DTO for top driver statistics.
     */
    public record TopDriverStats(
            String driverId,
            Long completedRoutes,
            Double totalDistanceMeters,
            Double averageEfficiency
    ) {
        public TopDriverStats {
            if (completedRoutes == null) {
                completedRoutes = 0L;
            }
            if (totalDistanceMeters == null) {
                totalDistanceMeters = 0.0;
            }
            if (averageEfficiency == null) {
                averageEfficiency = 0.0;
            }
        }
    }
}
