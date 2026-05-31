package com.gogidix.shared.servicediscovery.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Service Registration Response DTO
 *
 * <p>Response object after successful service registration.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceRegistrationResponse {

    /**
     * Unique service instance identifier assigned by registry
     */
    private String instanceId;

    /**
     * Service name
     */
    private String serviceName;

    /**
     * Tenant ID
     */
    private String tenantId;

    /**
     * Registration timestamp
     */
    private Long registeredAt;

    /**
     * Expiration time (if TTL is configured)
     */
    private Long expiresAt;

    /**
     * Status of registration
     */
    private String status;

    /**
     * Heartbeat interval in seconds
     */
    private Integer heartbeatInterval;

    /**
     * Message
     */
    private String message;

    // Explicit getter for compatibility
    public String getInstanceId() {
        return instanceId;
    }
}
