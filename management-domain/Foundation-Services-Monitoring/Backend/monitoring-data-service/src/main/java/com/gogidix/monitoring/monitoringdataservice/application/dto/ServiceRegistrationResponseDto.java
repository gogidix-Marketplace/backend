package com.gogidix.monitoring.monitoringdataservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Response DTO for service registration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRegistrationResponseDto {

    private String serviceId;
    private String tenantId;
    private String serviceName;
    private String serviceType;
    private String category;
    private String version;
    private String description;
    private String baseUrl;
    private String healthEndpoint;
    private String metricsEndpoint;
    private Integer collectionInterval;
    private Boolean enabled;
    private Map<String, String> tags;
    private Map<String, Object> metadata;
    private Instant registeredAt;
    private Instant lastHeartbeat;
    private String status;
}
