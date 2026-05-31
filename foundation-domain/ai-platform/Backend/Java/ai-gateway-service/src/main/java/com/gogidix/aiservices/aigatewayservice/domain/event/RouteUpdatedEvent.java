package com.gogidix.aiservices.aigatewayservice.domain.event;

/**
 * Event published when a route is updated.
 */
public class RouteUpdatedEvent extends RouteEvent {

    public RouteUpdatedEvent(String routeId, String tenantId) {
        super(routeId, tenantId, "ROUTE_UPDATED");
    }
}
