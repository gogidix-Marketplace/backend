package com.gogidix.shared.servicediscovery.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration Properties for Service Discovery
 *
 * <p>Configure service discovery behavior in application.yml:</p>
 * <pre>
 * gogidix:
 *   service-discovery:
 *     enabled: true
 *     registry-url: http://localhost:8761
 *     service-name: my-service
 *     domain: BUSINESS
 *     capabilities: courier,tracking
 *     tags: v1,production
 * </pre>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "gogidix.service-discovery")
public class ServiceDiscoveryProperties {

    /**
     * Enable/disable service discovery
     */
    private boolean enabled = true;

    /**
     * URL of the service registry
     */
    private String registryUrl = "http://localhost:8761";

    /**
     * Service name (defaults to spring.application.name)
     */
    private String serviceName;

    /**
     * Service domain (FOUNDATION, MANAGEMENT, BUSINESS_INFRASTRUCTURE, BUSINESS, PUBLIC_USER)
     */
    private String domain = "FOUNDATION";

    /**
     * Tenant ID
     */
    private String tenantId = "default";

    /**
     * Service protocol (http, https, grpc)
     */
    private String protocol = "http";

    /**
     * Health check path
     */
    private String healthCheckPath = "/actuator/health";

    /**
     * API version
     */
    private String apiVersion = "1.0.0";

    /**
     * Service capabilities list
     */
    private List<String> capabilities = new ArrayList<>();

    /**
     * Service tags
     */
    private List<String> tags = new ArrayList<>();

    /**
     * Supported authentication methods
     */
    private List<String> authMethods = List.of("JWT");

    /**
     * Auto-register on startup
     */
    private boolean autoRegister = true;

    /**
     * Cache TTL in milliseconds
     */
    private long cacheTtl = 30000;

    /**
     * Heartbeat interval in milliseconds
     */
    private long heartbeatInterval = 30000;

    /**
     * Connection timeout in milliseconds
     */
    private int connectionTimeout = 5000;

    /**
     * Read timeout in milliseconds
     */
    private int readTimeout = 10000;

    /**
     * Retry attempts for service calls
     */
    private int retryAttempts = 3;

    /**
     * Enable load balancing
     */
    private boolean loadBalancing = true;
}
