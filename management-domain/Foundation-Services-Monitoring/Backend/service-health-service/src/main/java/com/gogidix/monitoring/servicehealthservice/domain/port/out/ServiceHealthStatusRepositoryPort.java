package com.gogidix.monitoring.servicehealthservice.domain.port.out;

import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceHealthStatus;

import java.util.List;
import java.util.Optional;

/**
 * Output port for service health status repository operations.
 */
public interface ServiceHealthStatusRepositoryPort {

    /**
     * Save a service health status.
     */
    ServiceHealthStatus save(ServiceHealthStatus healthStatus);

    /**
     * Find by service name and tenant.
     */
    Optional<ServiceHealthStatus> findByTenantAndService(String tenantId, String serviceName);

    /**
     * Find all services for a tenant.
     */
    List<ServiceHealthStatus> findByTenantId(String tenantId);

    /**
     * Find services by health status.
     */
    List<ServiceHealthStatus> findByTenantIdAndStatus(String tenantId, ServiceHealthStatus.HealthStatus status);

    /**
     * Find services by type.
     */
    List<ServiceHealthStatus> findByTenantIdAndServiceType(
            String tenantId,
            com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType serviceType
    );

    /**
     * Update service health status.
     */
    void updateStatus(String id, ServiceHealthStatus.HealthStatus status, Integer healthScore);

    /**
     * Delete by service name and tenant.
     */
    void deleteByTenantAndService(String tenantId, String serviceName);
}
