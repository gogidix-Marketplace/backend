package com.gogidix.monitoring.monitoringdataservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for registering a service for monitoring.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterServiceRequestDto {

    @NotBlank(message = "Service name is required")
    private String serviceName;

    @NotNull(message = "Service type is required")
    private String serviceType;

    private String category;

    private String version;

    private String description;

    @NotBlank(message = "Base URL is required")
    private String baseUrl;

    private String healthEndpoint;

    private String metricsEndpoint;

    private Integer collectionInterval;

    @Builder.Default
    private Boolean enabled = true;

    private Map<String, String> tags;

    private Map<String, Object> metadata;
}
