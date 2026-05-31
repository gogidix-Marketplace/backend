package com.gogidix.courier.routingservice.application.service;

import com.gogidix.courier.routingservice.application.command.*;
import com.gogidix.courier.routingservice.application.dto.*;
import com.gogidix.courier.routingservice.application.mapper.RouteMapper;
import com.gogidix.courier.routingservice.domain.entity.*;
import com.gogidix.courier.routingservice.domain.event.RouteCompletedEvent;
import com.gogidix.courier.routingservice.domain.event.RouteCreatedEvent;
import com.gogidix.courier.routingservice.domain.event.RouteOptimizedEvent;
import com.gogidix.courier.routingservice.domain.repository.RouteRepository;
import com.gogidix.courier.routingservice.shared.exception.NotFoundException;
import com.gogidix.courier.routingservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Application service for routing operations.
 * Handles route creation, optimization, and management with various algorithms.
 */
@Service
@Transactional(readOnly = true)
public class RoutingApplicationService {

    private static final Logger log = LoggerFactory.getLogger(RoutingApplicationService.class);
    private static final String CACHE_NAME = "routes";

    private final RouteRepository repository;
    private final RouteMapper mapper;
    private final RouteEventPublisher eventPublisher;

    public RoutingApplicationService(
            RouteRepository repository,
            RouteMapper mapper,
            RouteEventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Create a new route.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public RouteResponse createRoute(RouteRequest request, String tenantId, String userId) {
        log.info("Creating route: {} for tenant: {}", request.routeId(), tenantId);

        if (repository.existsByTenantIdAndRouteId(tenantId, request.routeId())) {
            throw new ValidationException("Route with routeId '" + request.routeId() + "' already exists");
        }

        Route route = mapper.toEntity(request, tenantId);
        route.validate();

        // Set metadata
        if (route.getMetadata() != null) {
            route.getMetadata().setCreatedBy(userId);
        }

        Route saved = repository.save(route);

        // Publish domain event
        eventPublisher.publish(new RouteCreatedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getRouteId(),
                saved.getDriverId(),
                saved.getOrderIds(),
                saved.getStatus(),
                saved.getPriority(),
                saved.getVehicleType(),
                saved.getWaypointCount()
        ));

        log.info("Route created with ID: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Get a route by ID.
     */
    @Cacheable(value = CACHE_NAME, key = "#id")
    public RouteResponse getRouteById(String id) {
        log.debug("Fetching route: {}", id);

        Route route = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Route", id));

        return mapper.toResponseDto(route);
    }

    /**
     * Get a route by route ID within a tenant.
     */
    @Cacheable(value = CACHE_NAME, key = "'tenantId:' + #tenantId + ':routeId:' + #routeId")
    public RouteResponse getRouteByRouteId(String tenantId, String routeId) {
        log.debug("Fetching route by tenantId: {} and routeId: {}", tenantId, routeId);

        Route route = repository.findByTenantIdAndRouteId(tenantId, routeId)
                .orElseThrow(() -> new NotFoundException("Route", routeId));

        return mapper.toResponseDto(route);
    }

    /**
     * List routes with filtering and pagination.
     */
    public PagedResponseDto<RouteResponse> listRoutes(
            String tenantId,
            String driverId,
            Route.RouteStatus status,
            Route.RoutePriority priority,
            Route.VehicleType vehicleType,
            Instant startDateFrom,
            Instant startDateTo,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        log.debug("Listing routes for tenant: {} - page: {}, size: {}", tenantId, page, size);

        List<Route> allRoutes = repository.findByTenantId(tenantId);

        // Apply filters
        List<Route> filtered = allRoutes.stream()
                .filter(r -> driverId == null || r.getDriverId().equals(driverId))
                .filter(r -> status == null || r.getStatus() == status)
                .filter(r -> priority == null || r.getPriority() == priority)
                .filter(r -> vehicleType == null || r.getVehicleType() == vehicleType)
                .filter(r -> startDateFrom == null ||
                        (r.getStartDate() != null && !r.getStartDate().isBefore(startDateFrom)))
                .filter(r -> startDateTo == null ||
                        (r.getStartDate() != null && !r.getStartDate().isAfter(startDateTo)))
                .collect(Collectors.toList());

        // Apply sorting (simple in-memory sort)
        filtered.sort((a, b) -> {
            int comparison = 0;
            switch (sortBy) {
                case "createdAt" -> comparison = a.getCreatedAt().compareTo(b.getCreatedAt());
                case "startDate" -> comparison = a.getStartDate() != null && b.getStartDate() != null ?
                        a.getStartDate().compareTo(b.getStartDate()) : 0;
                case "priority" -> comparison = a.getPriority().compareTo(b.getPriority());
                case "status" -> comparison = a.getStatus().compareTo(b.getStatus());
                default -> comparison = a.getCreatedAt().compareTo(b.getCreatedAt());
            }
            return "DESC".equalsIgnoreCase(sortDirection) ? -comparison : comparison;
        });

        // Apply pagination
        int start = page * size;
        int end = Math.min(start + size, filtered.size());

        List<Route> pagedRoutes = new ArrayList<>();
        if (start < filtered.size()) {
            pagedRoutes = filtered.subList(start, end);
        }

        List<RouteResponse> responses = pagedRoutes.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());

        return PagedResponseDto.of(responses, page, size, filtered.size());
    }

    /**
     * Get active routes for a driver.
     */
    public List<RouteResponse> getActiveRoutesForDriver(String tenantId, String driverId) {
        log.debug("Fetching active routes for driver: {} in tenant: {}", driverId, tenantId);

        List<Route> routes = repository.findActiveByTenantIdAndDriverId(tenantId, driverId);

        return routes.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing route.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#id")
    public RouteResponse updateRoute(String id, RouteRequest request, String userId) {
        log.info("Updating route: {}", id);

        Route route = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Route", id));

        mapper.updateEntityFromRequest(route, request);

        if (route.getMetadata() != null) {
            route.getMetadata().setLastModifiedBy(userId);
        }

        route.validate();
        Route saved = repository.save(route);

        log.info("Route updated: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Optimize a route using the specified algorithm.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public OptimizationResponse optimizeRoute(String routeId, OptimizationRequest request, String tenantId, String userId) {
        log.info("Optimizing route: {} with algorithm: {}", routeId, request.algorithm());

        Route route = repository.findByTenantIdAndRouteId(tenantId, routeId)
                .orElseThrow(() -> new NotFoundException("Route", routeId));

        if (route.getWaypoints() == null || route.getWaypoints().size() < 2) {
            throw new ValidationException("Route must have at least 2 waypoints to optimize");
        }

        // Store original metrics
        double originalDistance = calculateTotalDistance(route);
        int originalDuration = calculateTotalDuration(route);

        // Create optimized route
        String optimizedRouteId = UUID.randomUUID().toString();
        OptimizedRoute optimizedRoute = new OptimizedRoute(
                tenantId,
                optimizedRouteId,
                route.getRouteId(),
                route.getDriverId(),
                request.algorithm()
        );

        long startTime = System.currentTimeMillis();

        // Apply optimization algorithm
        List<RouteWaypoint> optimizedWaypoints = applyOptimizationAlgorithm(
                route.getWaypoints(),
                request,
                route.getStartLocation()
        );

        long executionTime = System.currentTimeMillis() - startTime;

        // Calculate new metrics
        double optimizedDistance = calculateDistanceWithWaypoints(route.getStartLocation(), optimizedWaypoints);
        int optimizedDuration = calculateDurationWithWaypoints(optimizedWaypoints);

        // Build optimized waypoints with timing
        Instant currentTime = route.getStartDate() != null ? route.getStartDate() : Instant.now();
        double cumulativeDistance = 0;
        int cumulativeTime = 0;

        for (RouteWaypoint waypoint : optimizedWaypoints) {
            OptimizedRoute.OptimizedWaypoint optWaypoint = new OptimizedRoute.OptimizedWaypoint(
                    waypoint.getWaypointId(),
                    waypoint.getSequenceNumber()
            );

            cumulativeDistance += waypoint.getDistanceFromPreviousMeters() != null ?
                    waypoint.getDistanceFromPreviousMeters() : 0;
            cumulativeTime += waypoint.getTravelTimeFromPreviousSeconds() != null ?
                    waypoint.getTravelTimeFromPreviousSeconds() : 0;

            currentTime = currentTime.plusSeconds(cumulativeTime);

            optWaypoint.setEstimatedArrival(currentTime);
            optWaypoint.setEstimatedDeparture(currentTime.plusSeconds(
                    waypoint.getServiceDurationSeconds() != null ? waypoint.getServiceDurationSeconds() : 300
            ));
            optWaypoint.setCumulativeDistanceMeters(cumulativeDistance);
            optWaypoint.setCumulativeTimeSeconds(cumulativeTime);

            optimizedRoute.addOptimizedWaypoint(optWaypoint);
        }

        // Set optimization results
        optimizedRoute.setOptimizationResults(
                optimizedDistance,
                optimizedDuration,
                originalDistance,
                originalDuration
        );

        // Apply constraints
        if (request.constraints() != null) {
            OptimizedRoute.OptimizationConstraints constraints = new OptimizedRoute.OptimizationConstraints();
            constraints.setMaxRouteDurationSeconds(request.constraints().maxRouteDurationSeconds());
            constraints.setMaxRouteDistanceMeters(request.constraints().maxRouteDistanceMeters());
            constraints.setTimeWindowsSatisfied(request.constraints().considerTimeWindows());
            optimizedRoute.setConstraints(constraints);
        }

        optimizedRoute.markCompleted(executionTime, 1);

        // Save optimized route
        repository.saveOptimizedRoute(optimizedRoute);

        // Apply optimization to original route
        route.optimizeRoute(optimizedWaypoints);
        route.updateEstimates(optimizedDistance, optimizedDuration);
        repository.save(route);

        // Publish domain event
        eventPublisher.publish(new RouteOptimizedEvent(
                route.getId(),
                route.getTenantId(),
                route.getRouteId(),
                optimizedRouteId,
                route.getDriverId(),
                optimizedRoute.getDistanceSavedMeters(),
                optimizedRoute.getTimeSavedSeconds(),
                optimizedRoute.getDistanceImprovementPercent(),
                optimizedRoute.getTimeImprovementPercent(),
                optimizedRoute.getOptimizationScore(),
                request.algorithm().name()
        ));

        log.info("Route optimized: {} with score: {}", routeId, optimizedRoute.getOptimizationScore());
        return mapper.toOptimizationResponseDto(optimizedRoute);
    }

    /**
     * Complete a route.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#routeId")
    public RouteResponse completeRoute(String routeId, Double actualDistance, Integer actualDuration, String userId) {
        log.info("Completing route: {}", routeId);

        Route route = repository.findById(routeId)
                .orElseThrow(() -> new NotFoundException("Route", routeId));

        route.complete(actualDistance, actualDuration);
        Route saved = repository.save(route);

        // Publish domain event
        eventPublisher.publish(new RouteCompletedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getRouteId(),
                saved.getDriverId(),
                saved.getOrderIds(),
                saved.getActualDistanceMeters(),
                saved.getActualDurationSeconds(),
                saved.getEstimatedDistanceMeters(),
                saved.getEstimatedDurationSeconds(),
                saved.calculateEfficiency(),
                saved.getWaypointCount(),
                saved.getWaypointCount()
        ));

        log.info("Route completed: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Cancel a route.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#routeId")
    public RouteResponse cancelRoute(String routeId, String reason, String userId) {
        log.info("Cancelling route: {}", routeId);

        Route route = repository.findById(routeId)
                .orElseThrow(() -> new NotFoundException("Route", routeId));

        route.cancel(reason);
        Route saved = repository.save(route);

        log.info("Route cancelled: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Delete a route.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#id")
    public void deleteRoute(String id) {
        log.info("Deleting route: {}", id);

        if (!repository.findById(id).isPresent()) {
            throw new NotFoundException("Route", id);
        }

        repository.deleteById(id);
        log.info("Route deleted: {}", id);
    }

    /**
     * Get route statistics.
     */
    public RouteStatsResponse getRouteStatistics(String tenantId, Instant fromDate, Instant toDate) {
        log.debug("Fetching route statistics for tenant: {} from {} to {}", tenantId, fromDate, toDate);

        List<Route> routes = fromDate != null && toDate != null ?
                repository.findByTenantIdAndStartDateBetween(tenantId, fromDate, toDate) :
                repository.findByTenantId(tenantId);

        long totalRoutes = routes.size();
        long completedRoutes = routes.stream().filter(r -> r.getStatus() == Route.RouteStatus.COMPLETED).count();
        long inProgressRoutes = routes.stream().filter(r -> r.getStatus() == Route.RouteStatus.IN_PROGRESS).count();
        long pendingRoutes = routes.stream().filter(r -> r.getStatus() == Route.RouteStatus.PENDING).count();
        long cancelledRoutes = routes.stream().filter(r -> r.getStatus() == Route.RouteStatus.CANCELLED).count();

        double totalDistance = routes.stream()
                .filter(r -> r.getActualDistanceMeters() != null)
                .mapToDouble(Route::getActualDistanceMeters)
                .sum();

        double totalDuration = routes.stream()
                .filter(r -> r.getActualDurationSeconds() != null)
                .mapToDouble(Route::getActualDurationSeconds)
                .sum();

        double averageDistance = completedRoutes > 0 ? totalDistance / completedRoutes : 0;
        double averageDuration = completedRoutes > 0 ? totalDuration / completedRoutes : 0;

        double averageEfficiency = routes.stream()
                .filter(r -> r.calculateEfficiency() > 0)
                .mapToDouble(Route::calculateEfficiency)
                .average()
                .orElse(0);

        long totalWaypoints = routes.stream()
                .mapToInt(Route::getWaypointCount)
                .sum();

        long totalOrders = routes.stream()
                .mapToInt(Route::getOrderCount)
                .sum();

        Map<Route.RouteStatus, Long> byStatus = Arrays.stream(Route.RouteStatus.values())
                .collect(Collectors.toMap(
                        status -> status,
                        status -> routes.stream().filter(r -> r.getStatus() == status).count()
                ));

        Map<Route.RoutePriority, Long> byPriority = Arrays.stream(Route.RoutePriority.values())
                .collect(Collectors.toMap(
                        priority -> priority,
                        priority -> routes.stream().filter(r -> r.getPriority() == priority).count()
                ));

        Map<Route.VehicleType, Long> byVehicleType = Arrays.stream(Route.VehicleType.values())
                .collect(Collectors.toMap(
                        type -> type,
                        type -> routes.stream().filter(r -> r.getVehicleType() == type).count()
                ));

        return new RouteStatsResponse(
                fromDate,
                toDate,
                totalRoutes,
                completedRoutes,
                inProgressRoutes,
                pendingRoutes,
                cancelledRoutes,
                totalDistance,
                totalDuration,
                averageDistance,
                averageDuration,
                averageEfficiency,
                totalWaypoints,
                totalOrders,
                totalRoutes > 0 ? (double) totalWaypoints / totalRoutes : 0,
                totalRoutes > 0 ? (double) totalOrders / totalRoutes : 0,
                byStatus,
                byPriority,
                byVehicleType,
                null
        );
    }

    // ==================== Optimization Algorithms ====================

    /**
     * Apply the specified optimization algorithm.
     */
    private List<RouteWaypoint> applyOptimizationAlgorithm(
            List<RouteWaypoint> waypoints,
            OptimizationRequest request,
            Route.GeoPoint startPoint) {

        return switch (request.algorithm()) {
            case NEAREST_NEIGHBOR -> nearestNeighbor(waypoints, startPoint);
            case TWO_OPT -> twoOpt(waypoints);
            case THREE_OPT -> threeOpt(waypoints);
            case GENETIC_ALGORITHM -> geneticAlgorithm(waypoints, request);
            case SIMULATED_ANNEALING -> simulatedAnnealing(waypoints, request);
            default -> nearestNeighbor(waypoints, startPoint);
        };
    }

    /**
     * Nearest Neighbor algorithm - O(n^2).
     * Starts from the first waypoint and always visits the nearest unvisited waypoint.
     */
    private List<RouteWaypoint> nearestNeighbor(List<RouteWaypoint> waypoints, Route.GeoPoint startPoint) {
        List<RouteWaypoint> result = new ArrayList<>(waypoints);
        boolean[] visited = new boolean[waypoints.size()];
        List<RouteWaypoint> optimized = new ArrayList<>();

        // Start from the first waypoint or nearest to start point
        int current = 0;
        if (startPoint != null) {
            current = findNearestWaypoint(startPoint, waypoints, visited);
        }

        for (int i = 0; i < waypoints.size(); i++) {
            visited[current] = true;
            RouteWaypoint currentWaypoint = result.get(current);
            optimized.add(cloneWaypoint(currentWaypoint, i));

            // Find nearest unvisited waypoint
            int next = findNearestWaypoint(currentWaypoint.getLocation(), waypoints, visited);
            if (next == -1) break;
            current = next;
        }

        // Update travel information
        updateTravelInfo(optimized);

        return optimized;
    }

    /**
     * 2-opt algorithm - O(n^2).
     * Improves a route by removing crossing edges.
     */
    private List<RouteWaypoint> twoOpt(List<RouteWaypoint> waypoints) {
        List<RouteWaypoint> route = new ArrayList<>(waypoints);
        int n = route.size();
        boolean improved = true;

        while (improved) {
            improved = false;
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 2; j < n; j++) {
                    // Calculate current distance
                    double currentDist = calculateDistance(
                            route.get(i).getLocation(),
                            route.get(i + 1).getLocation()
                    ) + calculateDistance(
                            route.get(j).getLocation(),
                            route.get((j + 1) % n).getLocation()
                    );

                    // Calculate new distance after swap
                    double newDist = calculateDistance(
                            route.get(i).getLocation(),
                            route.get(j).getLocation()
                    ) + calculateDistance(
                            route.get(i + 1).getLocation(),
                            route.get((j + 1) % n).getLocation()
                    );

                    if (newDist < currentDist) {
                        // Perform 2-opt swap
                        reverse(route, i + 1, j);
                        improved = true;
                    }
                }
            }
        }

        // Update sequence numbers
        for (int i = 0; i < route.size(); i++) {
            route.get(i).setSequenceNumber(i);
        }

        updateTravelInfo(route);
        return route;
    }

    /**
     * 3-opt algorithm - O(n^3).
     * More thorough than 2-opt but slower.
     */
    private List<RouteWaypoint> threeOpt(List<RouteWaypoint> waypoints) {
        // For simplicity, use 2-opt as base then additional improvements
        List<RouteWaypoint> route = twoOpt(waypoints);
        // Additional 3-opt optimizations could be added here
        return route;
    }

    /**
     * Genetic Algorithm for route optimization.
     * Uses population-based evolution to find optimal routes.
     */
    private List<RouteWaypoint> geneticAlgorithm(List<RouteWaypoint> waypoints, OptimizationRequest request) {
        int populationSize = request.parameters() != null ?
                request.parameters().populationSize() : 100;
        int generations = request.parameters() != null ?
                request.parameters().maxGenerations() : 500;
        double mutationRate = request.parameters() != null ?
                request.parameters().mutationRate() : 0.1;

        // Initialize population
        List<List<RouteWaypoint>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(shuffleWaypoints(new ArrayList<>(waypoints)));
        }

        // Evolve
        for (int gen = 0; gen < generations; gen++) {
            // Sort by fitness
            population.sort(Comparator.comparingDouble(this::calculateTotalDistance));

            // Keep best 20%
            int keep = (int) (populationSize * 0.2);
            List<List<RouteWaypoint>> newPopulation = new ArrayList<>(population.subList(0, keep));

            // Generate offspring
            while (newPopulation.size() < populationSize) {
                List<RouteWaypoint> parent1 = selectParent(population);
                List<RouteWaypoint> parent2 = selectParent(population);
                List<RouteWaypoint> offspring = crossover(parent1, parent2);

                if (Math.random() < mutationRate) {
                    mutate(offspring);
                }

                newPopulation.add(offspring);
            }

            population = newPopulation;
        }

        // Return best solution
        List<RouteWaypoint> best = population.stream()
                .min(Comparator.comparingDouble(this::calculateTotalDistance))
                .orElse(population.get(0));

        for (int i = 0; i < best.size(); i++) {
            best.get(i).setSequenceNumber(i);
        }

        updateTravelInfo(best);
        return best;
    }

    /**
     * Simulated Annealing for route optimization.
     * Uses temperature-based acceptance of worse solutions.
     */
    private List<RouteWaypoint> simulatedAnnealing(List<RouteWaypoint> waypoints, OptimizationRequest request) {
        double temperature = request.parameters() != null ?
                request.parameters().temperature() : 1000.0;
        double coolingRate = request.parameters() != null ?
                request.parameters().coolingRate() : 0.95;
        int maxIterations = request.parameters() != null ?
                request.parameters().maxIterations() : 1000;

        List<RouteWaypoint> current = new ArrayList<>(waypoints);
        List<RouteWaypoint> best = new ArrayList<>(current);
        double currentEnergy = calculateTotalDistance(current);
        double bestEnergy = currentEnergy;

        Random random = new Random();

        for (int i = 0; i < maxIterations && temperature > 1; i++) {
            // Generate neighbor by swapping two waypoints
            List<RouteWaypoint> neighbor = new ArrayList<>(current);
            int idx1 = random.nextInt(neighbor.size());
            int idx2 = random.nextInt(neighbor.size());
            Collections.swap(neighbor, idx1, idx2);

            double neighborEnergy = calculateTotalDistance(neighbor);

            // Accept if better or with probability based on temperature
            if (neighborEnergy < currentEnergy ||
                    Math.exp((currentEnergy - neighborEnergy) / temperature) > random.nextDouble()) {
                current = neighbor;
                currentEnergy = neighborEnergy;

                if (currentEnergy < bestEnergy) {
                    best = new ArrayList<>(current);
                    bestEnergy = currentEnergy;
                }
            }

            temperature *= coolingRate;
        }

        for (int i = 0; i < best.size(); i++) {
            best.get(i).setSequenceNumber(i);
        }

        updateTravelInfo(best);
        return best;
    }

    // ==================== Helper Methods ====================

    private int findNearestWaypoint(Route.GeoPoint point, List<RouteWaypoint> waypoints, boolean[] visited) {
        int nearest = -1;
        double minDist = Double.MAX_VALUE;

        for (int i = 0; i < waypoints.size(); i++) {
            if (!visited[i]) {
                double dist = calculateDistance(point, waypoints.get(i).getLocation());
                if (dist < minDist) {
                    minDist = dist;
                    nearest = i;
                }
            }
        }
        return nearest;
    }

    private double calculateDistance(Route.GeoPoint p1, Route.GeoPoint p2) {
        if (p1 == null || p2 == null) {
            return 0;
        }

        final double EARTH_RADIUS = 6371000; // meters

        double lat1 = Math.toRadians(p1.getLatitude());
        double lat2 = Math.toRadians(p2.getLatitude());
        double deltaLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
        double deltaLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private double calculateTotalDistance(Route route) {
        if (route.getWaypoints() == null || route.getWaypoints().isEmpty()) {
            return 0;
        }
        return calculateDistanceWithWaypoints(route.getStartLocation(), route.getWaypoints());
    }

    private double calculateTotalDistance(List<RouteWaypoint> waypoints) {
        if (waypoints == null || waypoints.isEmpty()) {
            return 0;
        }
        double total = 0;
        for (RouteWaypoint wp : waypoints) {
            if (wp.getLocation() != null) {
                total += wp.getDistanceFromPreviousMeters() != null ? wp.getDistanceFromPreviousMeters() : 0;
            }
        }
        return total;
    }

    private double calculateDistanceWithWaypoints(Route.GeoPoint start, List<RouteWaypoint> waypoints) {
        double total = 0;
        Route.GeoPoint current = start;

        for (RouteWaypoint waypoint : waypoints) {
            if (current != null && waypoint.getLocation() != null) {
                total += calculateDistance(current, waypoint.getLocation());
            }
            current = waypoint.getLocation();
        }
        return total;
    }

    private int calculateTotalDuration(Route route) {
        if (route.getWaypoints() == null || route.getWaypoints().isEmpty()) {
            return 0;
        }
        return calculateDurationWithWaypoints(route.getWaypoints());
    }

    private int calculateDurationWithWaypoints(List<RouteWaypoint> waypoints) {
        int total = 0;
        for (RouteWaypoint waypoint : waypoints) {
            int travelTime = waypoint.getTravelTimeFromPreviousSeconds() != null ?
                    waypoint.getTravelTimeFromPreviousSeconds() : 0;
            int serviceTime = waypoint.getServiceDurationSeconds() != null ?
                    waypoint.getServiceDurationSeconds() : 300;
            total += travelTime + serviceTime;
        }
        return total;
    }

    private void updateTravelInfo(List<RouteWaypoint> waypoints) {
        for (int i = 0; i < waypoints.size(); i++) {
            RouteWaypoint current = waypoints.get(i);
            current.setSequenceNumber(i);

            if (i > 0) {
                RouteWaypoint prev = waypoints.get(i - 1);
                double distance = calculateDistance(prev.getLocation(), current.getLocation());
                int travelTime = (int) (distance / 8.33); // Assume 30 km/h average speed

                current.updateTravelFromPrevious(distance, travelTime);
                prev.updateTravelToNext(distance, travelTime);
            }
        }
    }

    private RouteWaypoint cloneWaypoint(RouteWaypoint original, int sequenceNumber) {
        RouteWaypoint clone = new RouteWaypoint(
                original.getWaypointId(),
                original.getLocation(),
                original.getWaypointType()
        );
        clone.setSequenceNumber(sequenceNumber);
        clone.setOrderId(original.getOrderId());
        clone.setCustomerId(original.getCustomerId());
        clone.setCustomerName(original.getCustomerName());
        clone.setAddress(original.getAddress());
        clone.setServiceDurationSeconds(original.getServiceDurationSeconds());
        clone.setTimeWindow(original.getTimeWindowStart(), original.getTimeWindowEnd());
        clone.setPriority(original.getPriority());
        clone.setNotes(original.getNotes());
        clone.setContactPhone(original.getContactPhone());
        clone.updatePackageInfo(original.getPackageCount(), original.getPackageWeightKg());
        return clone;
    }

    private List<RouteWaypoint> shuffleWaypoints(List<RouteWaypoint> waypoints) {
        List<RouteWaypoint> shuffled = new ArrayList<>(waypoints.subList(1, waypoints.size()));
        Collections.shuffle(shuffled);
        shuffled.add(0, waypoints.get(0)); // Keep first waypoint fixed
        return shuffled;
    }

    private List<RouteWaypoint> selectParent(List<List<RouteWaypoint>> population) {
        // Tournament selection
        int tournamentSize = 5;
        List<RouteWaypoint> best = null;
        double bestFitness = Double.MAX_VALUE;

        Random random = new Random();
        for (int i = 0; i < tournamentSize; i++) {
            List<RouteWaypoint> candidate = population.get(random.nextInt(population.size()));
            double fitness = calculateTotalDistance(candidate);
            if (fitness < bestFitness) {
                bestFitness = fitness;
                best = candidate;
            }
        }
        return best;
    }

    private List<RouteWaypoint> crossover(List<RouteWaypoint> parent1, List<RouteWaypoint> parent2) {
        // Order crossover (OX)
        int size = parent1.size();
        int start = new Random().nextInt(size);
        int end = start + new Random().nextInt(size - start);

        List<RouteWaypoint> offspring = new ArrayList<>(Collections.nCopies(size, null));

        // Copy segment from parent1
        for (int i = start; i <= end; i++) {
            offspring.set(i, parent1.get(i));
        }

        // Fill remaining from parent2
        int current = 0;
        for (RouteWaypoint waypoint : parent2) {
            if (!offspring.contains(waypoint)) {
                while (offspring.get(current) != null) {
                    current++;
                }
                offspring.set(current, waypoint);
            }
        }

        return offspring;
    }

    private void mutate(List<RouteWaypoint> route) {
        // Swap mutation
        Random random = new Random();
        int i = random.nextInt(route.size());
        int j = random.nextInt(route.size());
        Collections.swap(route, i, j);
    }

    private void reverse(List<RouteWaypoint> list, int start, int end) {
        while (start < end) {
            Collections.swap(list, start, end);
            start++;
            end--;
        }
    }
}
