package com.gogidix.shared.servicediscovery.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Service Metadata
 *
 * <p>Metadata about services registered in the Foundation service registry.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceMetadata {

    /**
     * Service Domain Classification
     */
    public enum ServiceDomain {
        FOUNDATION("foundation"),
        BUSINESS("business"),
        INFRASTRUCTURE("infrastructure"),
        PLATFORM("platform"),
        AI("ai"),
        ANALYTICS("analytics"),
        SHARED("shared");

        private final String value;

        ServiceDomain(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Unique service identifier
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
    private ServiceDomain domain;

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

    /**
     * Registration timestamp
     */
    private Long registeredAt;

    /**
     * Last heartbeat timestamp
     */
    private Long lastHeartbeat;

    /**
     * Service metadata version
     */
    private String metadataVersion;
}
