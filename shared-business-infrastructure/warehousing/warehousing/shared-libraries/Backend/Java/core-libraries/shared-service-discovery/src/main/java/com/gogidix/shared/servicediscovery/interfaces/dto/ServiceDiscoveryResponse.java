package com.gogidix.shared.servicediscovery.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gogidix.shared.servicediscovery.domain.model.ServiceMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Service Discovery Response DTO
 *
 * <p>Response object for service discovery queries.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceDiscoveryResponse {

    /**
     * Unique service instance identifier
     */
    private String instanceId;

    /**
     * Service name
     */
    private String serviceName;

    /**
     * Tenant ID (for multi-tenancy)
     */
    private String tenantId;

    /**
     * Service domain
     */
    private ServiceMetadata.ServiceDomain serviceDomain;

    /**
     * Service URL
     */
    private String serviceUrl;

    /**
     * Health check URL
     */
    private String healthCheckUrl;

    /**
     * Protocol (http/https)
     */
    private String protocol;

    /**
     * Host address
     */
    private String host;

    /**
     * Port number
     */
    private Integer port;

    /**
     * API version
     */
    private String apiVersion;

    /**
     * Service capabilities
     */
    private Set<String> capabilities;

    /**
     * Service tags
     */
    private Set<String> tags;

    /**
     * Whether authentication is required
     */
    private Boolean requiresAuth;

    /**
     * Supported authentication methods
     */
    private Set<String> authMethods;

    /**
     * Service priority (for load balancing)
     */
    private Integer priority;

    /**
     * Health status
     */
    private Boolean healthy;

    // Explicit getters for Lombok compatibility
    public boolean isRequiresAuth() {
        return requiresAuth != null ? requiresAuth : false;
    }

    public boolean isHealthy() {
        return healthy != null ? healthy : false;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public ServiceMetadata.ServiceDomain getServiceDomain() {
        return serviceDomain;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getHealthCheckUrl() {
        return healthCheckUrl;
    }

    public String getProtocol() {
        return protocol;
    }

    public Integer getPort() {
        return port;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public Set<String> getCapabilities() {
        return capabilities;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Integer getPriority() {
        return priority;
    }

    public Set<String> getAuthMethods() {
        return authMethods;
    }
}
