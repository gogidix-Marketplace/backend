package com.gogidix.aiservices.aigatewayservice.application.query;

import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;
import com.gogidix.aiservices.aigatewayservice.domain.port.in.GetRouteQuery;
import com.gogidix.aiservices.aigatewayservice.domain.repository.GatewayRouteRepository;

/**
 * Query handler for finding gateway routes by ID.
 */
public class FindGatewayRouteByIdQuery implements GetRouteQuery {

    private final GatewayRouteRepository repository;

    public FindGatewayRouteByIdQuery(GatewayRouteRepository repository) {
        this.repository = repository;
    }

    @Override
    public GatewayRoute getRouteById(String routeId, String tenantId) {
        return repository.findByRouteIdAndTenantId(routeId, tenantId)
                .orElseThrow(() -> new com.gogidix.aiservices.aigatewayservice.shared.exception.NotFoundException(
                        "GatewayRoute", routeId));
    }
}
