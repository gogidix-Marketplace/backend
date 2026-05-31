package com.gogidix.shared.infrastructure.services.gateway.apigateway.application.service;

import com.gogidix.shared.infrastructure.services.gateway.apigateway.application.port.in.RouteQueryPort;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.aggregate.RouteRegistry;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Application service for route query operations.
 * Acts as the use case orchestrator for route queries.
 */
@Service
public class RouteQueryService implements RouteQueryPort {

    private static final Logger log = LoggerFactory.getLogger(RouteQueryService.class);

    private final RouteRegistry routeRegistry;

    public RouteQueryService(RouteRegistry routeRegistry) {
        this.routeRegistry = routeRegistry;
    }

    @Override
    public Optional<Route> getRoute(String routeId) {
        log.debug("Looking up route: {}", routeId);

        return routeRegistry.getRoute(routeId);
    }

    @Override
    public Collection<Route> getAllRoutes() {
        Collection<Route> routes = routeRegistry.getAllRoutes();

        log.debug("Found {} routes", routes.size());

        return routes;
    }

    @Override
    public List<Route> getOrderedRoutes() {
        List<Route> routes = routeRegistry.getOrderedRoutes();

        log.debug("Found {} ordered routes", routes.size());

        return routes;
    }

    @Override
    public List<Route> getRoutesForService(String serviceId) {
        log.debug("Looking up routes for service: {}", serviceId);

        List<Route> routes = routeRegistry.getRoutesForService(serviceId);

        log.debug("Found {} routes for service: {}", routes.size(), serviceId);

        return routes;
    }

    @Override
    public Optional<Route> findMatchingRoute(String path) {
        log.debug("Finding matching route for path: {}", path);

        Optional<Route> route = routeRegistry.findMatchingRoute(path);

        route.ifPresentOrElse(
                r -> log.debug("Found matching route: {} for path: {}", r.getRouteId(), path),
                () -> log.debug("No matching route found for path: {}", path)
        );

        return route;
    }

    @Override
    public boolean routeExists(String routeId) {
        return routeRegistry.exists(routeId);
    }

    @Override
    public int getRouteCount() {
        return routeRegistry.size();
    }
}
