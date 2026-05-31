package com.gogidix.courier.routingservice.application.mapper;

import com.gogidix.courier.routingservice.application.dto.*;
import com.gogidix.courier.routingservice.domain.entity.OptimizedRoute;
import com.gogidix.courier.routingservice.domain.entity.Route;
import com.gogidix.courier.routingservice.domain.entity.RouteWaypoint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between routes and DTOs.
 */
@Component
public class RouteMapper {

    /**
     * Convert a route to response DTO.
     *
     * @param route the route entity
     * @return the response DTO
     */
    public RouteResponse toResponseDto(Route route) {
        if (route == null) {
            return null;
        }

        List<WaypointDTO> waypointDtos = route.getWaypoints() != null ?
                route.getWaypoints().stream()
                        .map(WaypointDTO::from)
                        .collect(Collectors.toList()) :
                List.of();

        return new RouteResponse(
                route.getId(),
                route.getTenantId(),
                route.getRouteId(),
                route.getDriverId(),
                route.getOrderIds(),
                waypointDtos,
                route.getStatus(),
                RouteResponse.GeoPointDTO.from(route.getStartLocation()),
                RouteResponse.GeoPointDTO.from(route.getEndLocation()),
                route.getEstimatedDistanceMeters(),
                route.getEstimatedDurationSeconds(),
                route.getActualDistanceMeters(),
                route.getActualDurationSeconds(),
                route.getPriority(),
                route.getVehicleType(),
                route.getStartTime(),
                route.getEndTime(),
                route.getStartDate(),
                route.getEndDate(),
                route.getNotes(),
                RouteResponse.RouteMetadataDTO.from(route.getMetadata()),
                route.getCreatedAt(),
                route.getUpdatedAt(),
                route.getCompletedAt(),
                route.getWaypointCount(),
                route.getOrderCount(),
                route.calculateEfficiency()
        );
    }

    /**
     * Convert a route request to route entity.
     *
     * @param request the route request
     * @param tenantId the tenant ID
     * @return the route entity
     */
    public Route toEntity(RouteRequest request, String tenantId) {
        if (request == null) {
            return null;
        }

        Route.GeoPoint startPoint = null;
        if (request.startLatitude() != null && request.startLongitude() != null) {
            startPoint = new Route.GeoPoint(request.startLatitude(), request.startLongitude(), request.startAddress());
        }

        Route route = new Route(
                tenantId,
                request.routeId(),
                request.driverId(),
                startPoint,
                request.vehicleType()
        );

        if (request.endLatitude() != null && request.endLongitude() != null) {
            route.setEndLocation(new Route.GeoPoint(request.endLatitude(), request.endLongitude(), request.endAddress()));
        }

        if (request.priority() != null) {
            route.setPriority(request.priority());
        }

        route.setStartDate(request.startDate());
        route.setEndDate(request.endDate());
        route.setNotes(request.notes());

        // Add waypoints if provided
        if (request.waypoints() != null && !request.waypoints().isEmpty()) {
            for (WaypointDTO dto : request.waypoints()) {
                RouteWaypoint waypoint = toWaypointEntity(dto);
                route.addWaypoint(waypoint);
            }
        }

        return route;
    }

    /**
     * Update a route entity from request.
     *
     * @param route   the route entity to update
     * @param request the update request
     */
    public void updateEntityFromRequest(Route route, RouteRequest request) {
        if (route == null || request == null) {
            return;
        }

        if (request.endLatitude() != null && request.endLongitude() != null) {
            route.setEndLocation(new Route.GeoPoint(request.endLatitude(), request.endLongitude(), request.endAddress()));
        }

        if (request.priority() != null) {
            route.setPriority(request.priority());
        }

        if (request.vehicleType() != null) {
            route.setVehicleType(request.vehicleType());
        }

        if (request.startDate() != null) {
            route.setStartDate(request.startDate());
        }

        if (request.endDate() != null) {
            route.setEndDate(request.endDate());
        }

        if (request.notes() != null) {
            route.updateNotes(request.notes());
        }
    }

    /**
     * Convert waypoint DTO to entity.
     *
     * @param dto the waypoint DTO
     * @return the waypoint entity
     */
    public RouteWaypoint toWaypointEntity(WaypointDTO dto) {
        if (dto == null) {
            return null;
        }

        Route.GeoPoint location = null;
        if (dto.location() != null) {
            location = new Route.GeoPoint(
                    dto.location().latitude(),
                    dto.location().longitude(),
                    dto.location().address()
            );
        } else if (dto.waypointId() != null) {
            location = new Route.GeoPoint(0, 0);
        }

        RouteWaypoint waypoint = new RouteWaypoint(
                dto.waypointId(),
                location,
                dto.waypointType()
        );

        waypoint.setOrderId(dto.orderId());
        waypoint.setCustomerId(dto.customerId());
        waypoint.setCustomerName(dto.customerName());
        waypoint.setAddress(dto.address());
        waypoint.setServiceDurationSeconds(dto.serviceDurationSeconds());
        waypoint.setTimeWindow(dto.timeWindowStart(), dto.timeWindowEnd());
        waypoint.setPriority(dto.priority());
        waypoint.setNotes(dto.notes());
        waypoint.setContactPhone(dto.contactPhone());
        waypoint.updatePackageInfo(dto.packageCount(), dto.packageWeightKg());

        return waypoint;
    }

    /**
     * Convert optimized route to response DTO.
     *
     * @param optimizedRoute the optimized route entity
     * @return the response DTO
     */
    public OptimizationResponse toOptimizationResponseDto(OptimizedRoute optimizedRoute) {
        if (optimizedRoute == null) {
            return null;
        }

        List<OptimizationResponse.OptimizedWaypointDTO> waypointDtos =
                optimizedRoute.getOptimizedWaypoints() != null ?
                        optimizedRoute.getOptimizedWaypoints().stream()
                                .map(OptimizationResponse.OptimizedWaypointDTO::from)
                                .collect(Collectors.toList()) :
                        List.of();

        List<OptimizationResponse.AlternativeRouteDTO> alternativeDtos =
                optimizedRoute.getAlternativeRoutes() != null ?
                        optimizedRoute.getAlternativeRoutes().stream()
                                .map(OptimizationResponse.AlternativeRouteDTO::from)
                                .collect(Collectors.toList()) :
                        List.of();

        return new OptimizationResponse(
                optimizedRoute.getId(),
                optimizedRoute.getTenantId(),
                optimizedRoute.getOptimizedRouteId(),
                optimizedRoute.getOriginalRouteId(),
                optimizedRoute.getDriverId(),
                waypointDtos,
                optimizedRoute.getOptimizationAlgorithm(),
                optimizedRoute.getOriginalDistanceMeters(),
                optimizedRoute.getOriginalDurationSeconds(),
                optimizedRoute.getOptimizedDistanceMeters(),
                optimizedRoute.getOptimizedDurationSeconds(),
                optimizedRoute.getDistanceSavedMeters(),
                optimizedRoute.getTimeSavedSeconds(),
                optimizedRoute.getDistanceImprovementPercent(),
                optimizedRoute.getTimeImprovementPercent(),
                optimizedRoute.getOptimizationScore(),
                OptimizationResponse.OptimizationConstraintsDTO.from(optimizedRoute.getConstraints()),
                optimizedRoute.getStatus(),
                optimizedRoute.getExecutionTimeMs(),
                optimizedRoute.getIterationCount(),
                alternativeDtos,
                optimizedRoute.getWarnings(),
                OptimizationResponse.OptimizationMetadataDTO.from(optimizedRoute.getMetadata()),
                optimizedRoute.getCreatedAt(),
                optimizedRoute.getAppliedAt()
        );
    }

    /**
     * Convert a list of routes to response DTOs.
     *
     * @param routes the list of route entities
     * @return the list of response DTOs
     */
    public List<RouteResponse> toResponseDtoList(List<Route> routes) {
        if (routes == null) {
            return List.of();
        }
        return routes.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
