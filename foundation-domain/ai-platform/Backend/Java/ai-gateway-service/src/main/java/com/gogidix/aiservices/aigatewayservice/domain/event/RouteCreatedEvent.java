package com.gogidix.aiservices.aigatewayservice.domain.event;

/**
 * Event published when a route is created.
 */
public class RouteCreatedEvent extends RouteEvent {

    private final String path;
    private final String targetService;

    public RouteCreatedEvent(String routeId, String tenantId, String path, String targetService) {
        super(routeId, tenantId, "ROUTE_CREATED");
        this.path = path;
        this.targetService = targetService;
    }

    public String getPath() {
        return path;
    }

    public String getTargetService() {
        return targetService;
    }
}
