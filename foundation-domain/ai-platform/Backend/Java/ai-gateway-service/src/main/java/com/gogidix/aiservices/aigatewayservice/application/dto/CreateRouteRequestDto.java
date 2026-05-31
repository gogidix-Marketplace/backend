package com.gogidix.aiservices.aigatewayservice.application.dto;

import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteFilter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * DTO for creating a new route.
 */
public record CreateRouteRequestDto(
        @NotBlank(message = "path is required")
        String path,

        @NotBlank(message = "targetService is required")
        String targetService,

        @NotEmpty(message = "targetUrls is required")
        List<String> targetUrls,

        List<RouteFilter> filters,

        @Positive(message = "rateLimit must be positive")
        Integer rateLimit,

        LoadBalancingStrategy loadBalancingStrategy,

        @Positive(message = "requestTimeout must be positive")
        Integer requestTimeout
) {
    public CreateRouteRequestDto {
        if (filters == null) {
            filters = List.of();
        }
    }
}
