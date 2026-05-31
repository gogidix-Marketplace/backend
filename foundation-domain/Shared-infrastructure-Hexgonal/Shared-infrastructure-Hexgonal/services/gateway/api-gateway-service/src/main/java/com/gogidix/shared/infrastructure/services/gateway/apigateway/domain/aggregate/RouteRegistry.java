package com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.aggregate;

import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.event.RouteCreatedEvent;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.event.RouteDeletedEvent;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.event.RouteUpdatedEvent;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.Route;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.RouteDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Aggregate root for managing API Gateway routes.
 * Handles route lifecycle and provides routing information.
 */
public class RouteRegistry {

    private final Map<String, Route> routes = new ConcurrentHashMap<>();
    private final List<Consumer<RouteCreatedEvent>> createdEventHandlers = new ArrayList<>();
    private final List<Consumer<RouteUpdatedEvent>> updatedEventHandlers = new ArrayList<>();
    private final List<Consumer<RouteDeletedEvent>> deletedEventHandlers = new ArrayList<>();

    /**
     * Register a new route from a route definition.
     */
    public Route register(RouteDefinition definition) {
        Route route = definition.toRoute();
        routes.put(route.getRouteId(), route);

        publishEvent(new RouteCreatedEvent(route));

        return route;
    }

    /**
     * Register a route directly.
     */
    public Route register(Route route) {
        routes.put(route.getRouteId(), route);

        publishEvent(new RouteCreatedEvent(route));

        return route;
    }

    /**
     * Update an existing route.
     */
    public Optional<Route> update(String routeId, RouteDefinition definition) {
        Route existing = routes.get(routeId);
        if (existing == null) {
            return Optional.empty();
        }

        Route updated = definition.toRoute();
        routes.put(routeId, updated);

        publishEvent(new RouteUpdatedEvent(updated));

        return Optional.of(updated);
    }

    /**
     * Delete a route by ID.
     */
    public Optional<Route> delete(String routeId) {
        Route removed = routes.remove(routeId);

        if (removed != null) {
            publishEvent(new RouteDeletedEvent(routeId, removed.getPath(), removed.getServiceId()));
            return Optional.of(removed);
        }

        return Optional.empty();
    }

    /**
     * Get a route by ID.
     */
    public Optional<Route> getRoute(String routeId) {
        return Optional.ofNullable(routes.get(routeId));
    }

    /**
     * Get all routes.
     */
    public Collection<Route> getAllRoutes() {
        return new ArrayList<>(routes.values());
    }

    /**
     * Get all enabled routes ordered by order value.
     */
    public List<Route> getOrderedRoutes() {
        return routes.values().stream()
                .filter(Route::isEnabled)
                .sorted(Comparator.comparingInt(Route::getOrder))
                .collect(Collectors.toList());
    }

    /**
     * Get all routes for a specific service.
     */
    public List<Route> getRoutesForService(String serviceId) {
        return routes.values().stream()
                .filter(route -> serviceId.equals(route.getServiceId()))
                .collect(Collectors.toList());
    }

    /**
     * Find matching route for a given path.
     */
    public Optional<Route> findMatchingRoute(String path) {
        return routes.values().stream()
                .filter(Route::isEnabled)
                .filter(route -> route.matches(path))
                .min(Comparator.comparingInt(Route::getOrder));
    }

    /**
     * Enable a route.
     */
    public Optional<Route> enable(String routeId) {
        Route existing = routes.get(routeId);
        if (existing == null) {
            return Optional.empty();
        }

        Route enabled = existing.toBuilder().enabled(true).build();
        routes.put(routeId, enabled);

        publishEvent(new RouteUpdatedEvent(enabled));

        return Optional.of(enabled);
    }

    /**
     * Disable a route.
     */
    public Optional<Route> disable(String routeId) {
        Route existing = routes.get(routeId);
        if (existing == null) {
            return Optional.empty();
        }

        Route disabled = existing.toBuilder().enabled(false).build();
        routes.put(routeId, disabled);

        publishEvent(new RouteUpdatedEvent(disabled));

        return Optional.of(disabled);
    }

    /**
     * Check if a route exists.
     */
    public boolean exists(String routeId) {
        return routes.containsKey(routeId);
    }

    /**
     * Get the total number of routes.
     */
    public int size() {
        return routes.size();
    }

    /**
     * Check if registry is empty.
     */
    public boolean isEmpty() {
        return routes.isEmpty();
    }

    /**
     * Clear all routes.
     */
    public void clear() {
        routes.clear();
    }

    /**
     * Register event handler for route created events.
     */
    public void onRouteCreated(Consumer<RouteCreatedEvent> handler) {
        createdEventHandlers.add(handler);
    }

    /**
     * Register event handler for route updated events.
     */
    public void onRouteUpdated(Consumer<RouteUpdatedEvent> handler) {
        updatedEventHandlers.add(handler);
    }

    /**
     * Register event handler for route deleted events.
     */
    public void onRouteDeleted(Consumer<RouteDeletedEvent> handler) {
        deletedEventHandlers.add(handler);
    }

    private void publishEvent(RouteCreatedEvent event) {
        createdEventHandlers.forEach(h -> h.accept(event));
    }

    private void publishEvent(RouteUpdatedEvent event) {
        updatedEventHandlers.forEach(h -> h.accept(event));
    }

    private void publishEvent(RouteDeletedEvent event) {
        deletedEventHandlers.forEach(h -> h.accept(event));
    }
}
