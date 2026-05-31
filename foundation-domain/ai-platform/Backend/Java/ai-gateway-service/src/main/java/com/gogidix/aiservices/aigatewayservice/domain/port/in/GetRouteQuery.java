package com.gogidix.aiservices.aigatewayservice.domain.port.in;

import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;

/**
 * Input port for getting a route by ID.
 */
public interface GetRouteQuery {

    GatewayRoute getRouteById(String routeId, String tenantId);
}
