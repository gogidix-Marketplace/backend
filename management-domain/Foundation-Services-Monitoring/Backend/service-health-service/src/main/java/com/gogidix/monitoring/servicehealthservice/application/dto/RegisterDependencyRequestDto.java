package com.gogidix.monitoring.servicehealthservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for registering a service dependency.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDependencyRequestDto {

    @NotBlank(message = "Service name is required")
    private String serviceName;

    @NotBlank(message = "Dependency service name is required")
    private String dependsOnService;

    @NotNull(message = "Dependency type is required")
    private String dependencyType;

    private Boolean isCritical;

    private Double healthImpact;

    private List<DependencyEndpointDto> endpoints;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DependencyEndpointDto {
        private String url;
        private String method;
    }
}
