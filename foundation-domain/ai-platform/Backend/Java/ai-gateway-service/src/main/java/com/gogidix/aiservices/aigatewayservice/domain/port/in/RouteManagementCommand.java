package com.gogidix.aiservices.aigatewayservice.domain.port.in;

import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;

/**
 * Input port for route management commands.
 */
public interface RouteManagementCommand {

    GatewayRoute createRoute(CreateRouteCommand command);

    GatewayRoute updateRoute(UpdateRouteCommand command);

    void deleteRoute(DeleteRouteCommand command);

    void activateRoute(String routeId, String tenantId);

    void deactivateRoute(String routeId, String tenantId);

    void resetCircuitBreaker(String routeId, String tenantId);
}
