package com.gogidix.aiservices.aigatewayservice.application.command;

import com.gogidix.aiservices.aigatewayservice.domain.port.in.DeleteRouteCommand;

/**
 * Application command for deleting a gateway route.
 */
public class DeleteGatewayRouteCommand implements DeleteRouteCommand {

    private final String routeId;
    private final String tenantId;
    private final String userId;

    public DeleteGatewayRouteCommand(String routeId, String tenantId, String userId) {
        this.routeId = routeId;
        this.tenantId = tenantId;
        this.userId = userId;
    }

    @Override
    public String routeId() {
        return routeId;
    }

    @Override
    public String tenantId() {
        return tenantId;
    }

    @Override
    public String userId() {
        return userId;
    }
}
