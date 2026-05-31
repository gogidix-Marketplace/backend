package com.gogidix.courier.routingservice.application.dto;

import com.gogidix.courier.routingservice.domain.entity.OptimizedRoute;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

/**
 * DTO for route optimization requests.
 */
public record OptimizationRequest(
        @NotNull(message = "algorithm is required")
        OptimizedRoute.OptimizationAlgorithm algorithm,

        OptimizationConstraintsDTO constraints,

        @Min(value = 0, message = "maxAlternatives must be non-negative")
        Integer maxAlternatives,

        Boolean generateAlternatives,

        OptimizationParametersDTO parameters
) {
    public OptimizationRequest {
        if (generateAlternatives == null) {
            generateAlternatives = false;
        }
        if (maxAlternatives == null) {
            maxAlternatives = generateAlternatives ? 3 : 0;
        }
    }

    /**
     * DTO for optimization constraints.
     */
    public record OptimizationConstraintsDTO(
            @Min(value = 1, message = "maxRouteDurationSeconds must be positive")
            Integer maxRouteDurationSeconds,

            @Min(value = 1, message = "maxRouteDistanceMeters must be positive")
            Double maxRouteDistanceMeters,

            Boolean considerTimeWindows,

            Boolean considerTraffic,

            Boolean considerDriverBreaks,

            @Min(value = 1, message = "maxWaypointsPerRoute must be positive")
            Integer maxWaypointsPerRoute,

            @Min(value = 0, message = "vehicleCapacityKg cannot be negative")
            Double vehicleCapacityKg,

            @Min(value = 0, message = "vehicleCapacityPackages cannot be negative")
            Integer vehicleCapacityPackages,

            Instant earliestStartTime,

            Instant latestEndTime,

            List<String> requiredWaypointIds,

            List<String> preferredWaypointIds,

            @Min(value = 0, message = "maxDetourMeters cannot be negative")
            Double maxDetourMeters,

            @Min(value = 0, message = "maxDetourSeconds cannot be negative")
            Integer maxDetourSeconds
    ) {
        public OptimizationConstraintsDTO {
            // Set defaults
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
     * DTO for optimization algorithm parameters.
     */
    public record OptimizationParametersDTO(
            @Min(value = 10, message = "populationSize must be at least 10")
            Integer populationSize,

            @Min(value = 10, message = "maxGenerations must be at least 10")
            Integer maxGenerations,

            @Min(value = 0, message = "mutationRate cannot be negative")
            Double mutationRate,

            @Min(value = 0, message = "crossoverRate cannot be negative")
            Double crossoverRate,

            @Min(value = 2, message = "tournamentSize must be at least 2")
            Integer tournamentSize,

            @Min(value = 0, message = "temperature cannot be negative")
            Double temperature,

            @Min(value = 0, message = "coolingRate cannot be negative")
            Double coolingRate,

            @Min(value = 1, message = "maxIterations must be positive")
            Integer maxIterations,

            @Min(value = 0, message = "convergenceThreshold cannot be negative")
            Double convergenceThreshold,

            @Min(value = 1, message = "localSearchIterations must be positive")
            Integer localSearchIterations
    ) {
        public OptimizationParametersDTO {
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
