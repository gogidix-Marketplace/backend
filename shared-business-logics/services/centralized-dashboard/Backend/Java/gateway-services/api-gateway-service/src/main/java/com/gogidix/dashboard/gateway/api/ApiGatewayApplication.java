package com.gogidix.dashboard.gateway.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for API Gateway Service.
 *
 * <p>This service acts as a BFF (Backend for Frontend) for the centralized dashboard,
 * aggregating responses from multiple microservices and providing a unified API interface.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>REST API aggregation</li>
 *   <li>Request routing and load balancing</li>
 *   <li>Response caching with Redis</li>
 *   <li>Tenant isolation</li>
 *   <li>Rate limiting</li>
 *   <li>Authentication and authorization</li>
 * </ul>
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
