package com.gogidix.platform.metering.domain.repository;

import com.gogidix.platform.metering.domain.model.QuotaUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for QuotaUsage entity.
 */
@Repository
public interface QuotaUsageRepository extends JpaRepository<QuotaUsage, String> {

    /**
     * Find quota usage by tenant and quota ID
     */
    Optional<QuotaUsage> findByTenantIdAndQuotaId(String tenantId, String quotaId);

    /**
     * Find quota usage by tenant, quota, and period
     */
    @Query("SELECT qu FROM QuotaUsage qu WHERE qu.tenantId = :tenantId " +
           "AND qu.quotaId = :quotaId " +
           "AND qu.periodStart <= :currentTime " +
           "AND qu.periodEnd > :currentTime")
    Optional<QuotaUsage> findCurrentQuotaUsage(
        @Param("tenantId") String tenantId,
        @Param("quotaId") String quotaId,
        @Param("currentTime") LocalDateTime currentTime
    );

    /**
     * Find all quota usage for tenant
     */
    List<QuotaUsage> findByTenantId(String tenantId);

    /**
     * Find exceeded quotas
     */
    @Query("SELECT qu FROM QuotaUsage qu WHERE qu.tenantId = :tenantId " +
           "AND (qu.softLimitExceeded = true OR qu.hardLimitExceeded = true)")
    List<QuotaUsage> findExceededQuotas(@Param("tenantId") String tenantId);

    /**
     * Find hard limit exceeded quotas
     */
    List<QuotaUsage> findByTenantIdAndHardLimitExceeded(String tenantId, boolean hardLimitExceeded);

    /**
     * Delete expired quota usage records
     */
    void deleteByPeriodEndBefore(LocalDateTime cutoff);
}
