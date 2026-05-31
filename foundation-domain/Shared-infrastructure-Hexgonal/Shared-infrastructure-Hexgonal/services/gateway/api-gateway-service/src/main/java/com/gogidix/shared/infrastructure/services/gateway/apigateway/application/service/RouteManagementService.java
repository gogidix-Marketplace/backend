package com.gogidix.shared.infrastructure.services.gateway.apigateway.application.service;

import com.gogidix.shared.infrastructure.services.gateway.apigateway.application.port.in.RouteManagementPort;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.application.port.in.RouteQueryPort;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.aggregate.RouteRegistry;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.Route;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Application service for route management operations.
 * Acts as the use case orchestrator for route lifecycle management.
 */
@Service
public class RouteManagementService implements RouteManagementPort {

    private static final Logger log = LoggerFactory.getLogger(RouteManagementService.class);

    private final RouteRegistry routeRegistry;

    public RouteManagementService(RouteRegistry routeRegistry) {
        this.routeRegistry = routeRegistry;
    }

    @Override
    public Route createRoute(RouteDefinition definition) {
        log.info("Creating route: routeId={}, path={}, serviceId={}",
                definition.getRouteId(), definition.getPath(), definition.getServiceId());

        Route route = routeRegistry.register(definition);

        log.debug("Route created successfully: {}", route.getRouteId());

        return route;
    }

    @Override
    public Optional<Route> updateRoute(String routeId, RouteDefinition definition) {
        log.info("Updating route: routeId={}", routeId);

        Optional<Route> updated = routeRegistry.update(routeId, definition);

        updated.ifPresentOrElse(
                r -> log.debug("Route updated successfully: {}", routeId),
                () -> log.warn("Route not found for update: {}", routeId)
        );

        return updated;
    }

    @Override
    public Optional<Route> deleteRoute(String routeId) {
        log.info("Deleting route: routeId={}", routeId);

        Optional<Route> deleted = routeRegistry.delete(routeId);

        deleted.ifPresentOrElse(
                r -> log.debug("Route deleted successfully: {}", routeId),
                () -> log.warn("Route not found for deletion: {}", routeId)
        );

        return deleted;
    }

    @Override
    public Optional<Route> enableRoute(String routeId) {
        log.info("Enabling route: routeId={}", routeId);

        Optional<Route> enabled = routeRegistry.enable(routeId);

        enabled.ifPresentOrElse(
                r -> log.debug("Route enabled successfully: {}", routeId),
                () -> log.warn("Route not found for enable: {}", routeId)
        );

        return enabled;
    }

    @Override
    public Optional<Route> disableRoute(String routeId) {
        log.info("Disabling route: routeId={}", routeId);

        Optional<Route> disabled = routeRegistry.disable(routeId);

        disabled.ifPresentOrElse(
                r -> log.debug("Route disabled successfully: {}", routeId),
                () -> log.warn("Route not found for disable: {}", routeId)
        );

        return disabled;
    }

    @Override
    public Route registerRoute(Route route) {
        log.info("Registering route: routeId={}, path={}", route.getRouteId(), route.getPath());

        Route registered = routeRegistry.register(route);

        log.debug("Route registered successfully: {}", registered.getRouteId());

        return registered;
    }
}
