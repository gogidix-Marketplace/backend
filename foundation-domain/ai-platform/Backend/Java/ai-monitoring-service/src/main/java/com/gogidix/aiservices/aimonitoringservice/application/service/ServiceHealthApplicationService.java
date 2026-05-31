package com.gogidix.aiservices.aimonitoringservice.application.service;

import com.gogidix.aiservices.aimonitoringservice.application.dto.ServiceHealthResponseDto;
import com.gogidix.aiservices.aimonitoringservice.domain.model.ServiceHealth;
import com.gogidix.aiservices.aimonitoringservice.domain.repository.ServiceHealthRepository;

import java.util.Map;

/**
 * Application service for managing service health.
 */
public class ServiceHealthApplicationService {

    private final ServiceHealthRepository repository;

    public ServiceHealthApplicationService(ServiceHealthRepository repository) {
        this.repository = repository;
    }

    public ServiceHealthResponseDto getServiceHealth(String serviceName, String tenantId) {
        ServiceHealth health = repository.findByServiceNameAndTenantId(serviceName, tenantId)
                .orElseGet(() -> {
                    ServiceHealth newHealth = new ServiceHealth(serviceName, tenantId);
                    return repository.save(newHealth);
                });
        return ServiceHealthResponseDto.from(health);
    }

    public ServiceHealthResponseDto updateServiceHealth(String serviceName, String tenantId, ServiceHealth.HealthStatus status, String message, Map<String, Object> metrics) {
        ServiceHealth health = repository.findByServiceNameAndTenantId(serviceName, tenantId)
                .orElseGet(() -> new ServiceHealth(serviceName, tenantId));

        health.updateStatus(status, message);
        if (metrics != null) {
            health.updateMetrics(metrics);
        }

        ServiceHealth saved = repository.save(health);
        return ServiceHealthResponseDto.from(saved);
    }
}
