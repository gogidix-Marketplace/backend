package com.gogidix.aiservices.aigatewayservice.application.dto;

import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteFilter;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;

import java.time.Instant;
import java.util.List;

/**
 * DTO for gateway route response.
 */
public record GatewayRouteResponseDto(
        String id,
        String routeId,
        String tenantId,
        String path,
        String targetService,
        List<String> targetUrls,
        List<RouteFilterDto> filters,
        Integer rateLimit,
        LoadBalancingStrategy loadBalancingStrategy,
        Integer requestTimeout,
        RouteStatus status,
        Boolean circuitBreakerOpen,
        Long circuitBreakerFailureCount,
        Instant createdAt,
        Instant updatedAt
) {

    public record RouteFilterDto(
            String name,
            String type,
            Object parameters
    ) {
        public static RouteFilterDto from(RouteFilter filter) {
            return new RouteFilterDto(
                    filter.getName(),
                    filter.getType(),
                    filter.getParameters()
            );
        }
    }

    public static GatewayRouteResponseDto from(com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute route) {
        return new GatewayRouteResponseDto(
                route.getId(),
                route.getRouteId(),
                route.getTenantId(),
                route.getPath(),
                route.getTargetService(),
                route.getTargetUrls(),
                route.getFilters().stream()
                        .map(RouteFilterDto::from)
                        .toList(),
                route.getRateLimit(),
                route.getLoadBalancingStrategy(),
                route.getRequestTimeout(),
                route.getStatus(),
                route.isCircuitBreakerOpen(),
                route.getCircuitBreakerFailureCount(),
                route.getCreatedAt(),
                route.getUpdatedAt()
        );
    }
}
