package com.gogidix.aiservices.aigatewayservice.domain.port.in;

import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;

import java.util.List;

/**
 * Input port for listing routes.
 */
public interface ListRoutesQuery {

    List<GatewayRoute> getRoutesByTenant(String tenantId);

    List<GatewayRoute> getRoutesByTenantAndStatus(String tenantId, RouteStatus status);

    List<GatewayRoute> getActiveRoutes();
}
