package com.gogidix.courier.routingservice.application.command;

import com.gogidix.courier.routingservice.domain.entity.OptimizedRoute;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Command to optimize a route.
 */
public record OptimizeRouteCommand(
        String tenantId,
        String routeId,
        OptimizedRoute.OptimizationAlgorithm algorithm,
        OptimizationConstraints constraints,
        Boolean generateAlternatives,
        Integer maxAlternatives,
        String requestedBy
) {
    public OptimizeRouteCommand {
        Objects.requireNonNull(tenantId, "tenantId is required");
        Objects.requireNonNull(routeId, "routeId is required");
        if (routeId.isBlank()) {
            throw new IllegalArgumentException("routeId cannot be blank");
        }
    }

    /**
     * Optimization constraints.
     */
    public record OptimizationConstraints(
            Integer maxRouteDurationSeconds,
            Double maxRouteDistanceMeters,
            Boolean considerTimeWindows,
            Boolean considerTraffic,
            Boolean considerDriverBreaks,
            Integer maxWaypointsPerRoute,
            Double vehicleCapacityKg,
            Integer vehicleCapacityPackages,
            Instant earliestStartTime,
            Instant latestEndTime,
            List<String> requiredWaypointIds,
            List<String> preferredWaypointIds,
            Double maxDetourMeters,
            Integer maxDetourSeconds
    ) {
        public OptimizationConstraints {
            // Default values
            if (considerTimeWindows == null) {
                considerTimeWindows = true;
            }
            if (considerTraffic == null) {
                considerTraffic = false;
            }
            if (considerDriverBreaks == null) {
                considerDriverBreaks = true;
            }
        }
    }

    /**
     * Optimization parameters.
     */
    public record OptimizationParameters(
            Integer populationSize,
            Integer maxGenerations,
            Double mutationRate,
            Double crossoverRate,
            Integer tournamentSize,
            Double temperature,
            Double coolingRate,
            Integer maxIterations,
            Double convergenceThreshold,
            Integer localSearchIterations
    ) {
        public OptimizationParameters {
            // Set defaults for genetic algorithm
            if (populationSize == null) {
                populationSize = 100;
            }
            if (maxGenerations == null) {
                maxGenerations = 500;
            }
            if (mutationRate == null) {
                mutationRate = 0.1;
            }
            if (crossoverRate == null) {
                crossoverRate = 0.8;
            }
            // Set defaults for simulated annealing
            if (temperature == null) {
                temperature = 1000.0;
            }
            if (coolingRate == null) {
                coolingRate = 0.95;
            }
            // Set defaults for general optimization
            if (maxIterations == null) {
                maxIterations = 1000;
            }
            if (convergenceThreshold == null) {
                convergenceThreshold = 0.001;
            }
            if (localSearchIterations == null) {
                localSearchIterations = 50;
            }
        }
    }
}
