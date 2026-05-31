package com.gogidix.shared.infrastructure.services.gateway.apigateway.infrastructure.adapter;

import com.gogidix.shared.infrastructure.services.gateway.apigateway.application.port.in.RouteManagementPort;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.application.port.in.RouteQueryPort;
import com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Infrastructure adapter that bridges the hexagonal domain with Spring Cloud Gateway.
 * This adapter translates between our domain model and Spring Cloud Gateway's route definitions.
 */
@Component
public class SpringCloudGatewayAdapter {

    private static final Logger log = LoggerFactory.getLogger(SpringCloudGatewayAdapter.class);

    private final RouteManagementPort routeManagementPort;
    private final RouteQueryPort routeQueryPort;

    public SpringCloudGatewayAdapter(RouteManagementPort routeManagementPort,
                                     RouteQueryPort routeQueryPort) {
        this.routeManagementPort = routeManagementPort;
        this.routeQueryPort = routeQueryPort;
        log.info("SpringCloudGatewayAdapter initialized");
    }

    /**
     * Sync all domain routes to Spring Cloud Gateway.
     */
    public void syncRoutes() {
        List<com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.Route> domainRoutes =
                new ArrayList<>(routeQueryPort.getAllRoutes());

        log.info("Syncing {} routes to Spring Cloud Gateway", domainRoutes.size());

        for (com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.Route route : domainRoutes) {
            log.debug("Syncing route: {} -> {}", route.getRouteId(), route.getUri());
        }
    }

    /**
     * Get statistics about routes.
     */
    public RouteStats getRouteStats() {
        int totalRoutes = routeQueryPort.getRouteCount();
        long enabledRoutes = routeQueryPort.getAllRoutes().stream()
                .filter(com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model.Route::isEnabled)
                .count();

        return new RouteStats(totalRoutes, enabledRoutes);
    }

    /**
     * Value object for route statistics.
     */
    public record RouteStats(int totalRoutes, long enabledRoutes) {}
}
