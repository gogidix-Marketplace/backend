package com.gogidix.aiservices.aigatewayservice.domain.event;

/**
 * Event published when a route is deleted.
 */
public class RouteDeletedEvent extends RouteEvent {

    public RouteDeletedEvent(String routeId, String tenantId) {
        super(routeId, tenantId, "ROUTE_DELETED");
    }
}
