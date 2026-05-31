package com.gogidix.shared.servicediscovery.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gogidix.shared.servicediscovery.domain.model.ServiceMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Register Service Request DTO
 *
 * <p>Request object for registering a new service with the Foundation service registry.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterServiceRequest {

    /**
     * Service name (required)
     */
    @NotBlank(message = "Service name is required")
    private String serviceName;

    /**
     * Tenant ID (for multi-tenancy)
     */
    private String tenantId;

    /**
     * Service domain (required)
     */
    @NotNull(message = "Service domain is required")
    private ServiceMetadata.ServiceDomain domain;

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
     * Port number (required)
     */
    @NotNull(message = "Port is required")
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

    /**
     * Service metadata version
     */
    private String metadataVersion;

    // Setter for tenantId
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    // Explicit setters for compatibility
    public void setHealthCheckPath(String path) {
        this.healthCheckUrl = path;
    }

    public void setHealthCheckUrl(String url) {
        this.healthCheckUrl = url;
    }

    public void setServiceDomain(ServiceMetadata.ServiceDomain domain) {
        this.domain = domain;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public void setRequiresAuth(Boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public void setCapabilities(Set<String> capabilities) {
        this.capabilities = capabilities;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void setAuthMethods(Set<String> authMethods) {
        this.authMethods = authMethods;
    }
}
