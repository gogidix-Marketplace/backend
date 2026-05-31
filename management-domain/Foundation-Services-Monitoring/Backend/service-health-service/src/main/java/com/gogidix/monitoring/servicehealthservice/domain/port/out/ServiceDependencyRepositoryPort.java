package com.gogidix.monitoring.servicehealthservice.domain.port.out;

import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceDependency;

import java.util.List;
import java.util.Optional;

/**
 * Output port for service dependency repository operations.
 */
public interface ServiceDependencyRepositoryPort {

    /**
     * Save a service dependency.
     */
    ServiceDependency save(ServiceDependency dependency);

    /**
     * Find dependencies for a service.
     */
    List<ServiceDependency> findByTenantAndService(String tenantId, String serviceName);

    /**
     * Find dependents (services that depend on this service).
     */
    List<ServiceDependency> findDependents(String tenantId, String serviceName);

    /**
     * Find all dependencies for a tenant.
     */
    List<ServiceDependency> findByTenantId(String tenantId);

    /**
     * Delete by ID.
     */
    void deleteById(String id);

    /**
     * Delete all dependencies for a service.
     */
    void deleteByTenantAndService(String tenantId, String serviceName);

    /**
     * Find critical dependencies.
     */
    List<ServiceDependency> findCriticalByTenant(String tenantId);
}
