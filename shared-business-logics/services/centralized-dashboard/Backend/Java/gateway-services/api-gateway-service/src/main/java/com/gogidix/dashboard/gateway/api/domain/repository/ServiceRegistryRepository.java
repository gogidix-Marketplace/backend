package com.gogidix.dashboard.gateway.api.domain.repository;

import com.gogidix.dashboard.gateway.api.domain.model.ServiceRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ServiceRegistry entities.
 */
@Repository
public interface ServiceRegistryRepository extends JpaRepository<ServiceRegistry, Long> {

    /**
     * Find all services by tenant ID
     */
    List<ServiceRegistry> findByTenantIdAndEnabledTrue(String tenantId);

    /**
     * Find service by name and tenant ID
     */
    Optional<ServiceRegistry> findByServiceNameAndTenantId(String serviceName, String tenantId);

    /**
     * Check if service exists
     */
    boolean existsByServiceNameAndTenantId(String serviceName, String tenantId);
}
