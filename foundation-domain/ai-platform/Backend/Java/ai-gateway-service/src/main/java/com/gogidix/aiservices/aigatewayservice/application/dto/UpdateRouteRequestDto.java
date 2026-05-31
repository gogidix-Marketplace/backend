package com.gogidix.aiservices.aigatewayservice.application.dto;

import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteFilter;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * DTO for updating a route.
 */
public record UpdateRouteRequestDto(
        String path,
        String targetService,
        List<String> targetUrls,
        List<RouteFilter> filters,
        @Positive(message = "rateLimit must be positive") Integer rateLimit,
        LoadBalancingStrategy loadBalancingStrategy,
        @Positive(message = "requestTimeout must be positive") Integer requestTimeout
) {
    public UpdateRouteRequestDto {
        if (filters == null) {
            filters = List.of();
        }
    }
}
