package com.gogidix.sales.dealmanagement.domain.repository;

import com.gogidix.sales.dealmanagement.domain.model.Deal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Deal Repository Interface
 * Defines the contract for deal persistence operations
 */
public interface DealRepository {

    Deal save(Deal deal);

    Optional<Deal> findById(String id);

    Optional<Deal> findByDealIdAndTenantId(String dealId, String tenantId);

    List<Deal> findAllByTenantId(String tenantId);

    List<Deal> findByTenantIdAndStatus(String tenantId, Deal.DealStatus status);

    List<Deal> findByTenantIdAndStage(String tenantId, Deal.DealStage stage);

    List<Deal> findByOwnerIdAndTenantId(String ownerId, String tenantId);

    List<Deal> findByAccountIdAndTenantId(String accountId, String tenantId);

    List<Deal> findByTenantIdAndExpectedCloseDateBetween(
            String tenantId, LocalDate startDate, LocalDate endDate);

    List<Deal> findByTenantIdAndStageIn(String tenantId, List<Deal.DealStage> stages);

    void deleteById(String id);

    void deleteByDealIdAndTenantId(String dealId, String tenantId);

    boolean existsByDealIdAndTenantId(String dealId, String tenantId);

    /**
     * Pipeline query methods
     */
    List<Deal> findPipelineDeals(String tenantId);

    /**
     * Forecasting query methods
     */
    List<Deal> findForecastDeals(String tenantId, LocalDate asOfDate);

    /**
     * Search deals
     */
    List<Deal> searchDeals(String tenantId, String searchTerm);

    /**
     * Count methods for reporting
     */
    long countByTenantIdAndStatus(String tenantId, Deal.DealStatus status);

    long countByTenantIdAndStage(String tenantId, Deal.DealStage stage);
}
