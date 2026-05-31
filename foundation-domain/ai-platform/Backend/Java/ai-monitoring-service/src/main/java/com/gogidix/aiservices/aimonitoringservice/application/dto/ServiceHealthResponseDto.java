package com.gogidix.aiservices.aimonitoringservice.application.dto;

import com.gogidix.aiservices.aimonitoringservice.domain.model.ServiceHealth;

import java.time.Instant;
import java.util.Map;

/**
 * DTO for service health response.
 */
public record ServiceHealthResponseDto(
        String id,
        String serviceName,
        String tenantId,
        ServiceHealth.HealthStatus status,
        String message,
        Map<String, Object> metrics,
        Instant lastCheckAt,
        Instant createdAt,
        Long totalChecks,
        Long failedChecks,
        Double successRate
) {
    public static ServiceHealthResponseDto from(ServiceHealth health) {
        return new ServiceHealthResponseDto(
                health.getId(),
                health.getServiceName(),
                health.getTenantId(),
                health.getStatus(),
                health.getMessage(),
                health.getMetrics(),
                health.getLastCheckAt(),
                health.getCreatedAt(),
                health.getTotalChecks(),
                health.getFailedChecks(),
                health.getSuccessRate()
        );
    }
}
