package com.gogidix.monitoring.servicehealthservice.application.mapper;

import com.gogidix.monitoring.servicehealthservice.application.dto.ServiceDependencyResponseDto;
import com.gogidix.monitoring.servicehealthservice.application.dto.ServiceHealthStatusResponseDto;
import com.gogidix.monitoring.servicehealthservice.application.dto.ScoreComponentsResponseDto;
import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceDependency;
import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceHealthStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for service health entities.
 */
@Component
public class ServiceHealthMapper {

    public ServiceHealthStatusResponseDto toResponseDto(ServiceHealthStatus domain) {
        return ServiceHealthStatusResponseDto.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .serviceName(domain.getServiceName())
                .serviceType(domain.getServiceType() != null ? domain.getServiceType().name() : null)
                .status(domain.getStatus() != null ? domain.getStatus().name() : null)
                .healthScore(domain.getHealthScore())
                .scoreComponents(toScoreComponentsDto(domain.getScoreComponents()))
                .details(domain.getDetails())
                .lastCheckAt(domain.getLastCheckAt())
                .consecutiveFailures(domain.getConsecutiveFailures())
                .lastSuccessAt(domain.getLastSuccessAt())
                .lastFailureAt(domain.getLastFailureAt())
                .averageResponseTime(domain.getAverageResponseTime())
                .errorRate(domain.getErrorRate())
                .uptimePercentage(domain.getUptimePercentage())
                .instanceId(domain.getInstanceId())
                .host(domain.getHost())
                .build();
    }

    public ServiceDependencyResponseDto toDependencyResponseDto(ServiceDependency domain) {
        return ServiceDependencyResponseDto.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .serviceName(domain.getServiceName())
                .dependsOnService(domain.getDependsOnService())
                .dependencyType(domain.getDependencyType() != null ? domain.getDependencyType().name() : null)
                .isCritical(domain.getIsCritical())
                .healthImpact(domain.getHealthImpact())
                .lastVerifiedAt(domain.getLastVerifiedAt())
                .status(domain.getStatus() != null ? domain.getStatus().name() : null)
                .endpoints(toEndpointDtos(domain.getEndpoints()))
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    private ScoreComponentsResponseDto toScoreComponentsDto(ServiceHealthStatus.ScoreComponents components) {
        if (components == null) {
            return null;
        }
        return ScoreComponentsResponseDto.builder()
                .uptimeScore(components.getUptimeScore())
                .responseTimeScore(components.getResponseTimeScore())
                .errorRateScore(components.getErrorRateScore())
                .dependencyScore(components.getDependencyScore())
                .build();
    }

    private List<ServiceDependencyResponseDto.DependencyEndpointDto> toEndpointDtos(
            List<ServiceDependency.DependencyEndpoint> endpoints
    ) {
        if (endpoints == null) {
            return null;
        }
        return endpoints.stream()
                .map(e -> ServiceDependencyResponseDto.DependencyEndpointDto.builder()
                        .url(e.getUrl())
                        .method(e.getMethod())
                        .averageLatency(e.getAverageLatency())
                        .successRate(e.getSuccessRate())
                        .build())
                .collect(Collectors.toList());
    }
}
