package com.gogidix.platform.metering.domain.repository;

import com.gogidix.platform.metering.domain.model.QuotaAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for QuotaAlert entity.
 */
@Repository
public interface QuotaAlertRepository extends JpaRepository<QuotaAlert, String> {

    /**
     * Find alerts by tenant ID
     */
    List<QuotaAlert> findByTenantIdOrderByCreatedAtDesc(String tenantId);

    /**
     * Find alerts by tenant and quota
     */
    List<QuotaAlert> findByTenantIdAndQuotaIdOrderByCreatedAtDesc(String tenantId, String quotaId);

    /**
     * Find unacknowledged alerts
     */
    List<QuotaAlert> findByTenantIdAndIsAcknowledgedOrderByCreatedAtDesc(String tenantId, boolean isAcknowledged);

    /**
     * Find alerts by severity
     */
    List<QuotaAlert> findByTenantIdAndSeverityOrderByCreatedAtDesc(String tenantId, QuotaAlert.Severity severity);

    /**
     * Find alerts created after a specific time
     */
    List<QuotaAlert> findByTenantIdAndCreatedAtAfterOrderByCreatedAtDesc(String tenantId, LocalDateTime createdAt);

    /**
     * Find recent unacknowledged critical alerts
     */
    @Query("SELECT qa FROM QuotaAlert qa WHERE qa.tenantId = :tenantId " +
           "AND qa.isAcknowledged = false " +
           "AND qa.severity = 'CRITICAL' " +
           "AND qa.createdAt > :since")
    List<QuotaAlert> findRecentCriticalAlerts(
        @Param("tenantId") String tenantId,
        @Param("since") LocalDateTime since
    );

    /**
     * Count unacknowledged alerts by tenant
     */
    long countByTenantIdAndIsAcknowledged(String tenantId, boolean isAcknowledged);

    /**
     * Delete old alerts
     */
    void deleteByCreatedAtBefore(LocalDateTime cutoff);
}
