package com.gogidix.aiservices.aigatewayservice.application.query;

import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;
import com.gogidix.aiservices.aigatewayservice.domain.port.in.ListRoutesQuery;
import com.gogidix.aiservices.aigatewayservice.domain.repository.GatewayRouteRepository;

import java.util.List;

/**
 * Query handler for listing gateway routes by tenant.
 */
public class FindGatewayRoutesByTenantQuery implements ListRoutesQuery {

    private final GatewayRouteRepository repository;

    public FindGatewayRoutesByTenantQuery(GatewayRouteRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<GatewayRoute> getRoutesByTenant(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Override
    public List<GatewayRoute> getRoutesByTenantAndStatus(String tenantId, RouteStatus status) {
        return repository.findByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public List<GatewayRoute> getActiveRoutes() {
        return repository.findByStatus(RouteStatus.ACTIVE);
    }
}
