package com.gogidix.platform.metering.domain.repository;

import com.gogidix.platform.metering.domain.model.QuotaDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for QuotaDefinition entity.
 */
@Repository
public interface QuotaDefinitionRepository extends JpaRepository<QuotaDefinition, String> {

    /**
     * Find quota definitions by tenant ID
     */
    List<QuotaDefinition> findByTenantId(String tenantId);

    /**
     * Find active quota definitions by tenant
     */
    List<QuotaDefinition> findByTenantIdAndIsActive(String tenantId, boolean isActive);

    /**
     * Find quota definition by tenant and quota name
     */
    Optional<QuotaDefinition> findByTenantIdAndQuotaName(String tenantId, String quotaName);

    /**
     * Find quota definitions by metric name
     */
    List<QuotaDefinition> findByMetricName(String metricName);

    /**
     * Find active quotas for a specific metric
     */
    @Query("SELECT qd FROM QuotaDefinition qd WHERE qd.tenantId = :tenantId " +
           "AND qd.metricName = :metricName AND qd.isActive = true")
    List<QuotaDefinition> findActiveQuotasByMetric(
        @Param("tenantId") String tenantId,
        @Param("metricName") String metricName
    );
}
