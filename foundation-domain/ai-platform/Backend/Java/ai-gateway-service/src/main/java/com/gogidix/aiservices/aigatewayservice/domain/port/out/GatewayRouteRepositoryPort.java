package com.gogidix.aiservices.aigatewayservice.domain.port.out;

import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;

import java.util.List;
import java.util.Optional;

/**
 * Output port for GatewayRoute repository.
 */
public interface GatewayRouteRepositoryPort {

    GatewayRoute save(GatewayRoute route);

    Optional<GatewayRoute> findById(String id);

    Optional<GatewayRoute> findByRouteIdAndTenantId(String routeId, String tenantId);

    List<GatewayRoute> findByTenantId(String tenantId);

    List<GatewayRoute> findByTenantIdAndStatus(String tenantId, RouteStatus status);

    List<GatewayRoute> findByStatus(RouteStatus status);

    void deleteById(String id);

    void deleteByRouteIdAndTenantId(String routeId, String tenantId);

    boolean existsByRouteIdAndTenantId(String routeId, String tenantId);
}
