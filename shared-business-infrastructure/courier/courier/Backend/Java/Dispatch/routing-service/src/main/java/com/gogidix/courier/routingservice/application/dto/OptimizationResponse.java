package com.gogidix.courier.routingservice.application.dto;

import com.gogidix.courier.routingservice.domain.entity.OptimizedRoute;

import java.time.Instant;
import java.util.List;

/**
 * DTO for optimization response.
 */
public record OptimizationResponse(
        String id,
        String tenantId,
        String optimizedRouteId,
        String originalRouteId,
        String driverId,
        List<OptimizedWaypointDTO> optimizedWaypoints,
        OptimizedRoute.OptimizationAlgorithm optimizationAlgorithm,
        Double originalDistanceMeters,
        Integer originalDurationSeconds,
        Double optimizedDistanceMeters,
        Integer optimizedDurationSeconds,
        Double distanceSavedMeters,
        Integer timeSavedSeconds,
        Double distanceImprovementPercent,
        Double timeImprovementPercent,
        Double optimizationScore,
        OptimizationConstraintsDTO constraints,
        OptimizedRoute.OptimizationStatus status,
        Long executionTimeMs,
        Integer iterationCount,
        List<AlternativeRouteDTO> alternativeRoutes,
        List<String> warnings,
        OptimizationMetadataDTO metadata,
        Instant createdAt,
        Instant appliedAt
) {
    /**
     * DTO for optimized waypoint.
     */
    public record OptimizedWaypointDTO(
            String waypointId,
            Integer sequenceNumber,
            Instant estimatedArrival,
            Instant estimatedDeparture,
            Double cumulativeDistanceMeters,
            Integer cumulativeTimeSeconds
    ) {
        public static OptimizedWaypointDTO from(OptimizedRoute.OptimizedWaypoint waypoint) {
            if (waypoint == null) {
                return null;
            }
            return new OptimizedWaypointDTO(
                    waypoint.getWaypointId(),
                    waypoint.getSequenceNumber(),
                    waypoint.getEstimatedArrival(),
                    waypoint.getEstimatedDeparture(),
                    waypoint.getCumulativeDistanceMeters(),
                    waypoint.getCumulativeTimeSeconds()
            );
        }
    }

    /**
     * DTO for optimization constraints.
     */
    public record OptimizationConstraintsDTO(
            Integer maxRouteDurationSeconds,
            Double maxRouteDistanceMeters,
            Boolean timeWindowsSatisfied,
            Boolean capacityConstraintsSatisfied,
            Boolean driverBreaksSatisfied,
            Boolean vehicleConstraintsSatisfied
    ) {
        public static OptimizationConstraintsDTO from(OptimizedRoute.OptimizationConstraints constraints) {
            if (constraints == null) {
                return null;
            }
            return new OptimizationConstraintsDTO(
                    constraints.getMaxRouteDurationSeconds(),
                    constraints.getMaxRouteDistanceMeters(),
                    constraints.getTimeWindowsSatisfied(),
                    constraints.getCapacityConstraintsSatisfied(),
                    constraints.getDriverBreaksSatisfied(),
                    constraints.getVehicleConstraintsSatisfied()
            );
        }
    }

    /**
     * DTO for alternative route.
     */
    public record AlternativeRouteDTO(
            String routeId,
            Double distanceMeters,
            Integer durationSeconds,
            Double score
    ) {
        public static AlternativeRouteDTO from(OptimizedRoute.AlternativeRoute alternative) {
            if (alternative == null) {
                return null;
            }
            return new AlternativeRouteDTO(
                    alternative.getRouteId(),
                    alternative.getDistanceMeters(),
                    alternative.getDurationSeconds(),
                    alternative.getScore()
            );
        }
    }

    /**
     * DTO for optimization metadata.
     */
    public record OptimizationMetadataDTO(
            String failureReason,
            String algorithmVersion,
            String parametersUsed,
            String computedBy
    ) {
        public static OptimizationMetadataDTO from(OptimizedRoute.OptimizationMetadata metadata) {
            if (metadata == null) {
                return null;
            }
            return new OptimizationMetadataDTO(
                    metadata.getFailureReason(),
                    metadata.getAlgorithmVersion(),
                    metadata.getParametersUsed(),
                    metadata.getComputedBy()
            );
        }
    }
}
