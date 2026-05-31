package com.gogidix.monitoring.servicehealthservice.application.dto;

import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceDependency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO for service dependency.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDependencyResponseDto {

    private String id;
    private String tenantId;
    private String serviceName;
    private String dependsOnService;
    private String dependencyType;
    private Boolean isCritical;
    private Double healthImpact;
    private Instant lastVerifiedAt;
    private String status;
    private List<DependencyEndpointDto> endpoints;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DependencyEndpointDto {
        private String url;
        private String method;
        private Double averageLatency;
        private Double successRate;
    }
}
