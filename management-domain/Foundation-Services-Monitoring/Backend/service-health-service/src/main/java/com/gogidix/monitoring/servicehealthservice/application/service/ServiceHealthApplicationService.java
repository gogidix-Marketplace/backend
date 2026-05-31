package com.gogidix.monitoring.servicehealthservice.application.service;

import com.gogidix.monitoring.servicehealthservice.application.dto.ServiceHealthStatusResponseDto;
import com.gogidix.monitoring.servicehealthservice.application.dto.ServiceHealthSummaryDto;
import com.gogidix.monitoring.servicehealthservice.application.mapper.ServiceHealthMapper;
import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceHealthStatus;
import com.gogidix.monitoring.servicehealthservice.domain.port.out.ServiceDependencyRepositoryPort;
import com.gogidix.monitoring.servicehealthservice.domain.port.out.ServiceHealthStatusRepositoryPort;
import com.gogidix.monitoring.servicehealthservice.domain.port.out.ServiceUptimeRepositoryPort;
import com.gogidix.monitoring.servicehealthservice.shared.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Application service for service health operations.
 */
@Service
@Transactional(readOnly = true)
public class ServiceHealthApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ServiceHealthApplicationService.class);

    private final ServiceHealthStatusRepositoryPort healthStatusRepository;
    private final ServiceUptimeRepositoryPort uptimeRepository;
    private final ServiceDependencyRepositoryPort dependencyRepository;
    private final ServiceHealthMapper mapper;

    public ServiceHealthApplicationService(
            ServiceHealthStatusRepositoryPort healthStatusRepository,
            ServiceUptimeRepositoryPort uptimeRepository,
            ServiceDependencyRepositoryPort dependencyRepository,
            ServiceHealthMapper mapper) {
        this.healthStatusRepository = healthStatusRepository;
        this.uptimeRepository = uptimeRepository;
        this.dependencyRepository = dependencyRepository;
        this.mapper = mapper;
    }

    /**
     * Get health status for a specific service.
     */
    @Cacheable(value = "serviceHealth", key = "#tenantId + ':' + #serviceName")
    public ServiceHealthStatusResponseDto getServiceHealth(String tenantId, String serviceName) {
        log.debug("Getting health status for service: {}", serviceName);

        return healthStatusRepository.findByTenantAndService(tenantId, serviceName)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Service '%s' not found", serviceName)));
    }

    /**
     * Get all health statuses for a tenant.
     */
    public List<ServiceHealthStatusResponseDto> getAllServiceHealth(String tenantId) {
        return healthStatusRepository.findByTenantId(tenantId).stream()
                .map(mapper::toResponseDto)
                .sorted(Comparator.comparing(ServiceHealthStatusResponseDto::getServiceName))
                .toList();
    }

    /**
     * Get health summary for a tenant.
     */
    public ServiceHealthSummaryDto getHealthSummary(String tenantId) {
        List<ServiceHealthStatus> allStatuses = healthStatusRepository.findByTenantId(tenantId);

        long total = allStatuses.size();
        long healthy = allStatuses.stream().filter(s -> s.getStatus() == ServiceHealthStatus.HealthStatus.HEALTHY).count();
        long degraded = allStatuses.stream().filter(s -> s.getStatus() == ServiceHealthStatus.HealthStatus.DEGRADED).count();
        long unhealthy = allStatuses.stream().filter(s -> s.getStatus() == ServiceHealthStatus.HealthStatus.UNHEALTHY).count();
        long down = allStatuses.stream().filter(s -> s.getStatus() == ServiceHealthStatus.HealthStatus.DOWN).count();
        long unknown = allStatuses.stream().filter(s -> s.getStatus() == ServiceHealthStatus.HealthStatus.UNKNOWN).count();

        double avgScore = allStatuses.stream()
                .filter(s -> s.getHealthScore() != null)
                .mapToInt(ServiceHealthStatus::getHealthScore)
                .average()
                .orElse(0.0);

        Map<String, Long> statusBreakdown = allStatuses.stream()
                .collect(Collectors.groupingBy(s -> s.getStatus() != null ? s.getStatus().name() : "UNKNOWN", Collectors.counting()));

        Map<String, Long> serviceTypeBreakdown = allStatuses.stream()
                .filter(s -> s.getServiceType() != null)
                .collect(Collectors.groupingBy(s -> s.getServiceType().name(), Collectors.counting()));

        return ServiceHealthSummaryDto.builder()
                .totalServices(total)
                .healthyServices(healthy)
                .degradedServices(degraded)
                .unhealthyServices(unhealthy)
                .downServices(down)
                .unknownServices(unknown)
                .averageHealthScore(avgScore)
                .statusBreakdown(statusBreakdown)
                .serviceTypeBreakdown(serviceTypeBreakdown)
                .build();
    }

    /**
     * Get services by health status.
     */
    public List<ServiceHealthStatusResponseDto> getServicesByStatus(
            String tenantId,
            ServiceHealthStatus.HealthStatus status
    ) {
        return healthStatusRepository.findByTenantIdAndStatus(tenantId, status).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    /**
     * Get services by type.
     */
    public List<ServiceHealthStatusResponseDto> getServicesByType(
            String tenantId,
            com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType serviceType
    ) {
        return healthStatusRepository.findByTenantIdAndServiceType(tenantId, serviceType).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    /**
     * Update service health status (called by health check scheduler).
     */
    @Transactional
    public void updateServiceHealth(
            String tenantId,
            String serviceName,
            ServiceHealthStatus.HealthStatus newStatus,
            Integer healthScore
    ) {
        healthStatusRepository.findByTenantAndService(tenantId, serviceName).ifPresentOrElse(
                status -> {
                    ServiceHealthStatus.HealthStatus oldStatus = status.getStatus();
                    healthStatusRepository.updateStatus(status.getId(), newStatus, healthScore);

                    if (oldStatus != newStatus) {
                        log.info("Service {} status changed: {} -> {}", serviceName, oldStatus, newStatus);
                        // Event would be published here
                    }
                },
                () -> {
                    // Create new health status if not exists
                    ServiceHealthStatus newHealthStatus = ServiceHealthStatus.builder()
                            .tenantId(tenantId)
                            .serviceName(serviceName)
                            .status(newStatus)
                            .healthScore(healthScore)
                            .lastCheckAt(java.time.Instant.now())
                            .createdAt(java.time.Instant.now())
                            .build();
                    healthStatusRepository.save(newHealthStatus);
                    log.info("Created health status for service: {}", serviceName);
                }
        );
    }

    /**
     * Get service dependencies.
     */
    public List<com.gogidix.monitoring.servicehealthservice.application.dto.ServiceDependencyResponseDto> getServiceDependencies(
            String tenantId,
            String serviceName
    ) {
        return dependencyRepository.findByTenantAndService(tenantId, serviceName).stream()
                .map(mapper::toDependencyResponseDto)
                .toList();
    }

    /**
     * Get services that depend on this service.
     */
    public List<com.gogidix.monitoring.servicehealthservice.application.dto.ServiceDependencyResponseDto> getServiceDependents(
            String tenantId,
            String serviceName
    ) {
        return dependencyRepository.findDependents(tenantId, serviceName).stream()
                .map(mapper::toDependencyResponseDto)
                .toList();
    }
}
