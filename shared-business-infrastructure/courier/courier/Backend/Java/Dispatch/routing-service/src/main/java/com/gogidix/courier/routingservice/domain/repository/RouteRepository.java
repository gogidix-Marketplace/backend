package com.gogidix.courier.routingservice.domain.repository;

import com.gogidix.courier.routingservice.domain.entity.OptimizedRoute;
import com.gogidix.courier.routingservice.domain.entity.Route;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Route aggregates.
 * Defines the contract for route persistence operations.
 * All implementations MUST filter by tenantId for multi-tenancy.
 */
public interface RouteRepository {

    /**
     * Save a route.
     *
     * @param route the route to save
     * @return the saved route
     */
    Route save(Route route);

    /**
     * Find a route by ID.
     *
     * @param id the route ID
     * @return the route if found
     */
    Optional<Route> findById(String id);

    /**
     * Find a route by route ID within a tenant.
     *
     * @param tenantId the tenant ID
     * @param routeId  the route ID
     * @return the route if found
     */
    Optional<Route> findByTenantIdAndRouteId(String tenantId, String routeId);

    /**
     * Find all routes for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of routes
     */
    List<Route> findByTenantId(String tenantId);

    /**
     * Find routes by driver ID within a tenant.
     *
     * @param tenantId the tenant ID
     * @param driverId the driver ID
     * @return list of routes
     */
    List<Route> findByTenantIdAndDriverId(String tenantId, String driverId);

    /**
     * Find active routes for a driver within a tenant.
     *
     * @param tenantId the tenant ID
     * @param driverId the driver ID
     * @return list of active routes
     */
    List<Route> findActiveByTenantIdAndDriverId(String tenantId, String driverId);

    /**
     * Find routes by status within a tenant.
     *
     * @param tenantId the tenant ID
     * @param status   the route status
     * @return list of routes
     */
    List<Route> findByTenantIdAndStatus(String tenantId, Route.RouteStatus status);

    /**
     * Find routes by priority within a tenant.
     *
     * @param tenantId the tenant ID
     * @param priority the route priority
     * @return list of routes
     */
    List<Route> findByTenantIdAndPriority(String tenantId, Route.RoutePriority priority);

    /**
     * Find routes by vehicle type within a tenant.
     *
     * @param tenantId    the tenant ID
     * @param vehicleType the vehicle type
     * @return list of routes
     */
    List<Route> findByTenantIdAndVehicleType(String tenantId, Route.VehicleType vehicleType);

    /**
     * Find routes within a date range for a tenant.
     *
     * @param tenantId the tenant ID
     * @param startDate the start date
     * @param endDate   the end date
     * @return list of routes
     */
    List<Route> findByTenantIdAndStartDateBetween(String tenantId, Instant startDate, Instant endDate);

    /**
     * Find routes containing a specific order within a tenant.
     *
     * @param tenantId the tenant ID
     * @param orderId  the order ID
     * @return list of routes
     */
    List<Route> findByTenantIdAndOrderIdsContaining(String tenantId, String orderId);

    /**
     * Find pending routes for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of pending routes
     */
    List<Route> findPendingByTenantId(String tenantId);

    /**
     * Find in-progress routes for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of in-progress routes
     */
    List<Route> findInProgressByTenantId(String tenantId);

    /**
     * Find completed routes within a date range.
     *
     * @param tenantId   the tenant ID
     * @param startDate  the start date
     * @param endDate    the end date
     * @return list of completed routes
     */
    List<Route> findCompletedByTenantIdAndCompletedAtBetween(String tenantId, Instant startDate, Instant endDate);

    /**
     * Find routes by multiple criteria with pagination.
     *
     * @param tenantId    the tenant ID
     * @param driverId    the driver ID (optional)
     * @param status      the status (optional)
     * @param priority    the priority (optional)
     * @param startDate   the start date (optional)
     * @param endDate     the end date (optional)
     * @param page        the page number (0-indexed)
     * @param size        the page size
     * @return list of routes
     */
    List<Route> findByCriteria(String tenantId, String driverId, Route.RouteStatus status,
                               Route.RoutePriority priority, Instant startDate, Instant endDate,
                               int page, int size);

    /**
     * Count all routes for a tenant.
     *
     * @param tenantId the tenant ID
     * @return the count
     */
    long countByTenantId(String tenantId);

    /**
     * Count routes by status for a tenant.
     *
     * @param tenantId the tenant ID
     * @param status   the status
     * @return the count
     */
    long countByTenantIdAndStatus(String tenantId, Route.RouteStatus status);

    /**
     * Count routes by driver and status for a tenant.
     *
     * @param tenantId the tenant ID
     * @param driverId the driver ID
     * @param status   the status
     * @return the count
     */
    long countByTenantIdAndDriverIdAndStatus(String tenantId, String driverId, Route.RouteStatus status);

    /**
     * Delete a route by ID.
     *
     * @param id the route ID
     */
    void deleteById(String id);

    /**
     * Check if a route exists by route ID within a tenant.
     *
     * @param tenantId the tenant ID
     * @param routeId  the route ID
     * @return true if exists
     */
    boolean existsByTenantIdAndRouteId(String tenantId, String routeId);

    /**
     * Find all routes.
     *
     * @return list of all routes
     */
    List<Route> findAll();

    // Optimized Route operations

    /**
     * Save an optimized route.
     *
     * @param optimizedRoute the optimized route to save
     * @return the saved optimized route
     */
    OptimizedRoute saveOptimizedRoute(OptimizedRoute optimizedRoute);

    /**
     * Find an optimized route by ID.
     *
     * @param id the optimized route ID
     * @return the optimized route if found
     */
    Optional<OptimizedRoute> findOptimizedRouteById(String id);

    /**
     * Find optimized routes by original route ID within a tenant.
     *
     * @param tenantId        the tenant ID
     * @param originalRouteId the original route ID
     * @return list of optimized routes
     */
    List<OptimizedRoute> findOptimizedRoutesByTenantIdAndOriginalRouteId(String tenantId, String originalRouteId);

    /**
     * Find optimized routes by original route ID.
     *
     * @param originalRouteId the original route ID
     * @return list of optimized routes
     */
    List<OptimizedRoute> findOptimizedRoutesByOriginalRouteId(String originalRouteId);

    /**
     * Find the latest optimized route for a route within a tenant.
     *
     * @param tenantId        the tenant ID
     * @param originalRouteId the original route ID
     * @return the latest optimized route if found
     */
    Optional<OptimizedRoute> findLatestOptimizedRouteByTenantIdAndOriginalRouteId(String tenantId, String originalRouteId);

    /**
     * Find optimized routes by status within a tenant.
     *
     * @param tenantId the tenant ID
     * @param status   the optimization status
     * @return list of optimized routes
     */
    List<OptimizedRoute> findOptimizedRoutesByTenantIdAndStatus(String tenantId, OptimizedRoute.OptimizationStatus status);

    /**
     * Delete an optimized route by ID.
     *
     * @param id the optimized route ID
     */
    void deleteOptimizedRouteById(String id);

    /**
     * Count optimized routes for a tenant.
     *
     * @param tenantId the tenant ID
     * @return the count
     */
    long countOptimizedRoutesByTenantId(String tenantId);
}
