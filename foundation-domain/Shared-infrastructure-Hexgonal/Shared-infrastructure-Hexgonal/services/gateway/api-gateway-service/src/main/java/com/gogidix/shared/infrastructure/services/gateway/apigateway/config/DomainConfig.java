package com.gogidix.shared.infrastructure.services.gateway.apigateway.config;

import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.aggregate.RouteRegistry;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.event.RouteCreatedEvent;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.event.RouteDeletedEvent;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.event.RouteUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the domain layer.
 * Creates the RouteRegistry aggregate root and wires up event handlers.
 */
@Configuration
public class DomainConfig {

    private static final Logger log = LoggerFactory.getLogger(DomainConfig.class);

    @Bean
    public RouteRegistry routeRegistry() {
        RouteRegistry registry = new RouteRegistry();

        // Register event handlers for logging
        registry.onRouteCreated(this::handleRouteCreated);
        registry.onRouteUpdated(this::handleRouteUpdated);
        registry.onRouteDeleted(this::handleRouteDeleted);

        log.info("RouteRegistry aggregate initialized with event handlers");

        return registry;
    }

    private void handleRouteCreated(RouteCreatedEvent event) {
        log.info("Route created: routeId={}, path={}, serviceId={}",
                event.getRouteId(), event.getPath(), event.getServiceId());
    }

    private void handleRouteUpdated(RouteUpdatedEvent event) {
        log.info("Route updated: routeId={}, path={}, serviceId={}",
                event.getRouteId(), event.getPath(), event.getServiceId());
    }

    private void handleRouteDeleted(RouteDeletedEvent event) {
        log.info("Route deleted: routeId={}, path={}, serviceId={}",
                event.getRouteId(), event.getPath(), event.getServiceId());
    }
}
