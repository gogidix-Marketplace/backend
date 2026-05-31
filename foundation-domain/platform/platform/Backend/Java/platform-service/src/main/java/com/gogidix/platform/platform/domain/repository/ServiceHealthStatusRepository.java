package com.gogidix.platform.platform.domain.repository;

import com.gogidix.platform.platform.domain.model.ServiceHealthStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ServiceHealthStatus entity operations.
 */
@Repository
public interface ServiceHealthStatusRepository extends JpaRepository<ServiceHealthStatus, String> {

    /**
     * Find health status by service name
     */
    Optional<ServiceHealthStatus> findByServiceName(String serviceName);

    /**
     * Find all health statuses for a tenant
     */
    List<ServiceHealthStatus> findByTenantId(String tenantId);

    /**
     * Find all services with specific health status
     */
    List<ServiceHealthStatus> findByTenantIdAndHealthStatus(String tenantId, ServiceHealthStatus.HealthStatus healthStatus);

    /**
     * Find services that haven't reported health recently
     */
    List<ServiceHealthStatus> findByLastCheckTimeBefore(LocalDateTime threshold);

    /**
     * Find all services ordered by last check time
     */
    List<ServiceHealthStatus> findAllByOrderByLastCheckTimeDesc();
}
