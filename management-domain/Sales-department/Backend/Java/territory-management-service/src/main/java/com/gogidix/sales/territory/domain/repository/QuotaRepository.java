package com.gogidix.sales.territory.domain.repository;

import com.gogidix.sales.territory.domain.model.Quota;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Quota Repository Interface (Port)
 * Defines the contract for quota persistence operations
 */
public interface QuotaRepository {

    Quota save(Quota quota);

    List<Quota> saveAll(List<Quota> quotas);

    Optional<Quota> findById(String id);

    Optional<Quota> findByQuotaIdAndTenantId(String quotaId, String tenantId);

    List<Quota> findByTenantId(String tenantId);

    List<Quota> findByTenantIdAndTerritoryId(String tenantId, String territoryId);

    List<Quota> findByTenantIdAndSalesRepresentativeId(String tenantId, String salesRepresentativeId);

    List<Quota> findByTenantIdAndStatus(String tenantId, Quota.QuotaStatus status);

    List<Quota> findByTenantIdAndType(String tenantId, Quota.QuotaType type);

    List<Quota> findByTenantIdAndPeriod(String tenantId, Quota.QuotaPeriod period);

    List<Quota> findByTenantIdAndYear(String tenantId, Integer year);

    List<Quota> findByTenantIdAndYearAndMonth(String tenantId, Integer year, Integer month);

    List<Quota> findActiveByTenantIdAndTerritoryId(String tenantId, String territoryId);

    List<Quota> findActiveByTenantIdAndSalesRepresentativeId(String tenantId, String salesRepresentativeId);

    List<Quota> findByTenantIdAndPeriodBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    Optional<Quota> findActiveByTenantIdAndTerritoryIdAndType(String tenantId, String territoryId, Quota.QuotaType type);

    boolean existsByQuotaIdAndTenantId(String quotaId, String tenantId);

    void deleteById(String id);

    void deleteByQuotaIdAndTenantId(String quotaId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndTerritoryId(String tenantId, String territoryId);

    long countByTenantIdAndStatus(String tenantId, Quota.QuotaStatus status);

    java.math.BigDecimal sumAmountByTenantIdAndTerritoryId(String tenantId, String territoryId);

    java.math.BigDecimal sumAmountByTenantIdAndYear(String tenantId, Integer year);
}
