package com.gogidix.aiservices.aigatewayservice.infrastructure.persistence.repository;

import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;
import com.gogidix.aiservices.aigatewayservice.infrastructure.persistence.document.GatewayRouteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for GatewayRoute.
 */
@Repository
public interface SpringDataGatewayRouteRepository extends MongoRepository<GatewayRouteDocument, String> {

    List<GatewayRouteDocument> findByTenantId(String tenantId);

    List<GatewayRouteDocument> findByTenantIdAndStatus(String tenantId, RouteStatus status);

    List<GatewayRouteDocument> findByStatus(RouteStatus status);

    GatewayRouteDocument findByRouteIdAndTenantId(String routeId, String tenantId);

    boolean existsByRouteIdAndTenantId(String routeId, String tenantId);

    void deleteByRouteIdAndTenantId(String routeId, String tenantId);
}
