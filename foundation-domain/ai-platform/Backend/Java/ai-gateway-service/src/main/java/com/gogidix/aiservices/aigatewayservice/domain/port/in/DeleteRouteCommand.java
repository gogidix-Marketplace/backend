package com.gogidix.aiservices.aigatewayservice.domain.port.in;

/**
 * Input port for deleting a route.
 */
public interface DeleteRouteCommand {

    String routeId();

    String tenantId();

    String userId();
}
