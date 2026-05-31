package com.gogidix.shared.infrastructure.services.gateway.apigateway.infrastructure.config;

import com.gogidix.shared.infrastructure.services.gateway.apigateway.infrastructure.filter.TenantGatewayFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway route configuration.
 * <p>
 * Defines all API routes through the gateway.
 */
@Slf4j
@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service Routes
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(new TenantGatewayFilter().apply(new TenantGatewayFilter.Config()))
                        )
                        .uri("lb://auth-service"))

                // User Management Service Routes
                .route("user-management-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(new TenantGatewayFilter().apply(new TenantGatewayFilter.Config()))
                        )
                        .uri("lb://user-management-service"))

                // Tenant Management Service Routes
                .route("tenant-management-service", r -> r
                        .path("/api/tenants/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(new TenantGatewayFilter().apply(new TenantGatewayFilter.Config()))
                        )
                        .uri("lb://tenant-management-service"))

                // Notification Service Routes
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(new TenantGatewayFilter().apply(new TenantGatewayFilter.Config()))
                        )
                        .uri("lb://notification-service"))

                // File Storage Service Routes
                .route("file-storage-service", r -> r
                        .path("/api/files/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(new TenantGatewayFilter().apply(new TenantGatewayFilter.Config()))
                        )
                        .uri("lb://file-storage-service"))

                // Audit Service Routes
                .route("audit-service", r -> r
                        .path("/api/audit/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(new TenantGatewayFilter().apply(new TenantGatewayFilter.Config()))
                        )
                        .uri("lb://audit-service"))

                // Rate Limiting Service Routes
                .route("rate-limiting-service", r -> r
                        .path("/api/rate-limit/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(new TenantGatewayFilter().apply(new TenantGatewayFilter.Config()))
                        )
                        .uri("lb://rate-limiting-service"))

                .build();
    }
}
