package com.gogidix.platform.platform.infrastructure.persistence.postgres;

import com.gogidix.platform.platform.domain.model.ServiceHealthStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for ServiceHealthStatus entity.
 */
@Repository
public interface ServiceHealthStatusJpaRepository extends JpaRepository<ServiceHealthStatus, String> {

    Optional<ServiceHealthStatus> findByServiceName(String serviceName);

    List<ServiceHealthStatus> findByTenantId(String tenantId);

    @Query("SELECT shs FROM ServiceHealthStatus shs WHERE shs.tenantId = :tenantId " +
           "AND shs.status = 'DEGRADED' OR shs.status = 'DOWN'")
    List<ServiceHealthStatus> findUnhealthyServices(@Param("tenantId") String tenantId);

    @Query("SELECT shs FROM ServiceHealthStatus shs WHERE shs.lastCheckTime < :threshold")
    List<ServiceHealthStatus> findStaleHealthChecks(@Param("threshold") LocalDateTime threshold);
}
