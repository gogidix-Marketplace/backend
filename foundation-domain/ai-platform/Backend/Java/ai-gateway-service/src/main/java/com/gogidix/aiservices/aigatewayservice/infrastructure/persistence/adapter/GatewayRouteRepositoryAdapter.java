package com.gogidix.aiservices.aigatewayservice.infrastructure.persistence.adapter;

import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;
import com.gogidix.aiservices.aigatewayservice.domain.repository.GatewayRouteRepository;
import com.gogidix.aiservices.aigatewayservice.infrastructure.persistence.document.GatewayRouteDocument;
import com.gogidix.aiservices.aigatewayservice.infrastructure.persistence.repository.SpringDataGatewayRouteRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository adapter for GatewayRoute.
 */
@Component
public class GatewayRouteRepositoryAdapter implements GatewayRouteRepository {

    private final SpringDataGatewayRouteRepository springRepository;

    public GatewayRouteRepositoryAdapter(SpringDataGatewayRouteRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public GatewayRoute save(GatewayRoute route) {
        GatewayRouteDocument document = toDocument(route);
        GatewayRouteDocument saved = springRepository.save(document);
        return toEntity(saved);
    }

    @Override
    public Optional<GatewayRoute> findById(String id) {
        return springRepository.findById(id)
                .map(this::toEntity);
    }

    @Override
    public Optional<GatewayRoute> findByRouteIdAndTenantId(String routeId, String tenantId) {
        return Optional.ofNullable(springRepository.findByRouteIdAndTenantId(routeId, tenantId))
                .map(this::toEntity);
    }

    @Override
    public List<GatewayRoute> findByTenantId(String tenantId) {
        return springRepository.findByTenantId(tenantId).stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public List<GatewayRoute> findByTenantIdAndStatus(String tenantId, RouteStatus status) {
        return springRepository.findByTenantIdAndStatus(tenantId, status).stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public List<GatewayRoute> findByStatus(RouteStatus status) {
        return springRepository.findByStatus(status).stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        springRepository.deleteById(id);
    }

    @Override
    public void deleteByRouteIdAndTenantId(String routeId, String tenantId) {
        springRepository.deleteByRouteIdAndTenantId(routeId, tenantId);
    }

    @Override
    public boolean existsByRouteIdAndTenantId(String routeId, String tenantId) {
        return springRepository.existsByRouteIdAndTenantId(routeId, tenantId);
    }

    private GatewayRouteDocument toDocument(GatewayRoute entity) {
        GatewayRouteDocument document = new GatewayRouteDocument();
        document.setId(entity.getId());
        document.setRouteId(entity.getRouteId());
        document.setTenantId(entity.getTenantId());
        document.setPath(entity.getPath());
        document.setTargetService(entity.getTargetService());
        document.setTargetUrls(new java.util.ArrayList<>(entity.getTargetUrls()));
        document.setFilters(new java.util.ArrayList<>(entity.getFilters()));
        document.setRateLimit(entity.getRateLimit());
        document.setLoadBalancingStrategy(entity.getLoadBalancingStrategy());
        document.setRequestTimeout(entity.getRequestTimeout());
        document.setStatus(entity.getStatus());
        document.setCreatedAt(entity.getCreatedAt());
        document.setUpdatedAt(entity.getUpdatedAt());
        document.setCircuitBreakerFailureCount(entity.getCircuitBreakerFailureCount());
        document.setCircuitBreakerThreshold(entity.getCircuitBreakerThreshold());
        document.setCircuitBreakerOpen(entity.isCircuitBreakerOpen());
        return document;
    }

private GatewayRoute toEntity(GatewayRouteDocument document) {
return GatewayRoute.builder()
                .id(document.getId())
                .routeId(document.getRouteId())
                .tenantId(document.getTenantId())
                .path(document.getPath())
                .targetService(document.getTargetService())
                .targetUrls(document.getTargetUrls())
                .filters(document.getFilters())
                .rateLimit(document.getRateLimit())
                .loadBalancingStrategy(document.getLoadBalancingStrategy())
                .requestTimeout(document.getRequestTimeout())
                .status(document.getStatus())
                .circuitBreakerThreshold(document.getCircuitBreakerThreshold())
                .build();
    }
}
