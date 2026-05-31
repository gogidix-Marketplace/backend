package com.gogidix.aiservices.aimonitoringservice.domain.repository;

import com.gogidix.aiservices.aimonitoringservice.domain.model.ServiceHealth;

import java.util.Optional;

/**
 * Repository interface for ServiceHealth entities.
 */
public interface ServiceHealthRepository {

    ServiceHealth save(ServiceHealth health);

    Optional<ServiceHealth> findByServiceNameAndTenantId(String serviceName, String tenantId);

    Optional<ServiceHealth> findById(String id);

    void deleteById(String id);
}
