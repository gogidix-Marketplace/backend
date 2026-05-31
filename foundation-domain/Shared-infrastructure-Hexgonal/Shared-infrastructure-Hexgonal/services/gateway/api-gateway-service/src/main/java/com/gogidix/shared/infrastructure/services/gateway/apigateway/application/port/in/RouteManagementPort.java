package com.gogidix.shared.infrastructure.services.gateway.apigateway.application.port.in;

import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.Route;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.RouteDefinition;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Input port for route management operations.
 * Provides use cases for creating, updating, and deleting routes.
 */
public interface RouteManagementPort {

    /**
     * Create a new route from a route definition.
     *
     * @param definition the route definition
     * @return the created route
     */
    Route createRoute(RouteDefinition definition);

    /**
     * Update an existing route.
     *
     * @param routeId the route ID to update
     * @param definition the new route definition
     * @return the updated route, if found
     */
    Optional<Route> updateRoute(String routeId, RouteDefinition definition);

    /**
     * Delete a route by ID.
     *
     * @param routeId the route ID to delete
     * @return the deleted route, if found
     */
    Optional<Route> deleteRoute(String routeId);

    /**
     * Enable a route.
     *
     * @param routeId the route ID to enable
     * @return the enabled route, if found
     */
    Optional<Route> enableRoute(String routeId);

    /**
     * Disable a route.
     *
     * @param routeId the route ID to disable
     * @return the disabled route, if found
     */
    Optional<Route> disableRoute(String routeId);

    /**
     * Register a route directly.
     *
     * @param route the route to register
     * @return the registered route
     */
    Route registerRoute(Route route);
}
