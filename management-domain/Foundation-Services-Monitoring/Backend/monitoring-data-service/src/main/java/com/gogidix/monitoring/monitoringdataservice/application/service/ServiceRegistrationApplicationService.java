package com.gogidix.monitoring.monitoringdataservice.application.service;

import com.gogidix.monitoring.monitoringdataservice.application.dto.RegisterServiceRequestDto;
import com.gogidix.monitoring.monitoringdataservice.application.dto.ServiceRegistrationResponseDto;
import com.gogidix.monitoring.monitoringdataservice.application.mapper.ServiceRegistrationMapper;
import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint;
import com.gogidix.monitoring.monitoringdataservice.domain.model.ServiceRegistration;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.ServiceRegistrationRepositoryPort;
import com.gogidix.monitoring.monitoringdataservice.shared.exception.ConflictException;
import com.gogidix.monitoring.monitoringdataservice.shared.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Application service for service registration.
 */
@Service
@Transactional(readOnly = true)
public class ServiceRegistrationApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ServiceRegistrationApplicationService.class);

    private final ServiceRegistrationRepositoryPort repository;
    private final ServiceRegistrationMapper mapper;

    public ServiceRegistrationApplicationService(
            ServiceRegistrationRepositoryPort repository,
            ServiceRegistrationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Register a new service for monitoring.
     */
    @Transactional
    @CacheEvict(value = "serviceRegistrations", allEntries = true)
    public ServiceRegistrationResponseDto registerService(String tenantId, RegisterServiceRequestDto request) {
        log.info("Registering service: {} for tenant: {}", request.getServiceName(), tenantId);

        // Check if service already exists
        if (repository.existsByTenantAndServiceName(tenantId, request.getServiceName())) {
            throw new ConflictException(
                    String.format("Service '%s' is already registered for tenant '%s'",
                            request.getServiceName(), tenantId));
        }

        ServiceRegistration registration = ServiceRegistration.builder()
                .serviceId(UUID.randomUUID().toString())
                .tenantId(tenantId)
                .serviceName(request.getServiceName())
                .serviceType(parseServiceType(request.getServiceType()))
                .category(request.getCategory())
                .version(request.getVersion())
                .description(request.getDescription())
                .baseUrl(request.getBaseUrl())
                .healthEndpoint(request.getHealthEndpoint() != null ? request.getHealthEndpoint() : "/actuator/health")
                .metricsEndpoint(request.getMetricsEndpoint() != null ? request.getMetricsEndpoint() : "/actuator/metrics")
                .collectionInterval(request.getCollectionInterval() != null ? request.getCollectionInterval() : 60)
                .enabled(request.getEnabled())
                .tags(request.getTags())
                .metadata(request.getMetadata())
                .registeredAt(java.time.Instant.now())
                .lastHeartbeat(java.time.Instant.now())
                .status(ServiceRegistration.ServiceStatus.REGISTERED)
                .build();

        ServiceRegistration saved = repository.save(registration);
        log.info("Service registered with ID: {}", saved.getServiceId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Get a service registration by ID.
     */
    @Cacheable(value = "serviceRegistrations", key = "#serviceId")
    public ServiceRegistrationResponseDto getService(String serviceId, String tenantId) {
        return repository.findById(serviceId)
                .filter(reg -> reg.getTenantId().equals(tenantId))
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Service '%s' not found for tenant '%s'", serviceId, tenantId)));
    }

    /**
     * Get all services for a tenant.
     */
    @Cacheable(value = "serviceRegistrations", key = "'tenant:' + #tenantId")
    public List<ServiceRegistrationResponseDto> getServicesByTenant(String tenantId) {
        return repository.findByTenantId(tenantId).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    /**
     * Get active services for a tenant.
     */
    public List<ServiceRegistrationResponseDto> getActiveServicesByTenant(String tenantId) {
        return repository.findActiveByTenantId(tenantId).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    /**
     * Update service heartbeat.
     */
    @Transactional
    public void updateHeartbeat(String serviceId, String tenantId) {
        repository.findById(serviceId)
                .filter(reg -> reg.getTenantId().equals(tenantId))
                .ifPresentOrElse(
                        reg -> {
                            repository.updateHeartbeat(serviceId);
                            log.debug("Updated heartbeat for service: {}", serviceId);
                        },
                        () -> {
                            throw new NotFoundException(
                                    String.format("Service '%s' not found for tenant '%s'", serviceId, tenantId));
                        }
                );
    }

    /**
     * Unregister a service.
     */
    @Transactional
    @CacheEvict(value = "serviceRegistrations", allEntries = true)
    public void unregisterService(String serviceId, String tenantId) {
        log.info("Unregistering service: {} for tenant: {}", serviceId, tenantId);

        ServiceRegistration registration = repository.findById(serviceId)
                .filter(reg -> reg.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException(
                        String.format("Service '%s' not found for tenant '%s'", serviceId, tenantId)));

        repository.deleteById(serviceId);
        log.info("Service unregistered: {}", serviceId);
    }

    private MetricDataPoint.ServiceType parseServiceType(String value) {
        if (value == null) return null;
        try {
            return MetricDataPoint.ServiceType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
