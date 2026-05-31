package com.gogidix.infrastructure.centralizeddashboard.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * Main application class for Centralized API Gateway.
 *
 * <p>This service implements centralized API gateway and routing
 * with multi-tenant SaaS architecture using Spring Cloud Gateway.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Spring Cloud Gateway for API routing</li>
 *   <li>Rate limiting with Redis</li>
 *   <li>Circuit breaker with Resilience4j</li>
 *   <li>JWT authentication and authorization</li>
 *   <li>Multi-tenancy with tenant isolation</li>
 *   <li>Request/response logging and monitoring</li>
 * </ul>
 */
@SpringBootApplication
public class CentralizedApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CentralizedApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("dashboard_core", r -> r.path("/api/dashboard/**")
                        .uri("lb://dashboard-core-service")
                        .filters(f -> f.stripPrefix(2)))
                .route("data_aggregation", r -> r.path("/api/aggregation/**")
                        .uri("lb://data-aggregation-service")
                        .filters(f -> f.stripPrefix(2)))
                .route("performance_monitoring", r -> r.path("/api/performance/**")
                        .uri("lb://centralized-performance-monitoring")
                        .filters(f -> f.stripPrefix(2)))
                .route("realtime_platform", r -> r.path("/api/realtime/**")
                        .uri("lb://centralized-real-time-platform")
                        .filters(f -> f.stripPrefix(2)))
                .route("reporting_service", r -> r.path("/api/reporting/**")
                        .uri("lb://centralized-reporting-service")
                        .filters(f -> f.stripPrefix(2)))
                .build();
    }
}
