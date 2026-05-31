package com.gogidix.shared.infrastructure.services.gateway.apigateway.application.port.in;

import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.Route;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Input port for route query operations.
 * Provides use cases for querying routes.
 */
public interface RouteQueryPort {

    /**
     * Get a route by ID.
     *
     * @param routeId the route ID
     * @return the route, if found
     */
    Optional<Route> getRoute(String routeId);

    /**
     * Get all routes.
     *
     * @return all routes
     */
    Collection<Route> getAllRoutes();

    /**
     * Get all enabled routes ordered by order value.
     *
     * @return ordered list of enabled routes
     */
    List<Route> getOrderedRoutes();

    /**
     * Get routes for a specific service.
     *
     * @param serviceId the service ID
     * @return list of routes for the service
     */
    List<Route> getRoutesForService(String serviceId);

    /**
     * Find matching route for a given path.
     *
     * @param path the request path
     * @return the matching route, if any
     */
    Optional<Route> findMatchingRoute(String path);

    /**
     * Check if a route exists.
     *
     * @param routeId the route ID
     * @return true if route exists
     */
    boolean routeExists(String routeId);

    /**
     * Get total number of routes.
     *
     * @return the route count
     */
    int getRouteCount();
}
