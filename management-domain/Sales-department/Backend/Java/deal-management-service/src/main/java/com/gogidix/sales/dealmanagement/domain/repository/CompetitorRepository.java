package com.gogidix.sales.dealmanagement.domain.repository;

import com.gogidix.sales.dealmanagement.domain.model.Competitor;

import java.util.List;
import java.util.Optional;

/**
 * Competitor Repository Interface
 * Defines the contract for competitor persistence operations
 */
public interface CompetitorRepository {

    Competitor save(Competitor competitor);

    Optional<Competitor> findById(String id);

    Optional<Competitor> findByCompetitorIdAndTenantId(String competitorId, String tenantId);

    List<Competitor> findByDealIdAndTenantId(String dealId, String tenantId);

    List<Competitor> findAllByTenantId(String tenantId);

    List<Competitor> findByCompetitorNameAndTenantId(String competitorName, String tenantId);

    void deleteById(String id);

    void deleteByCompetitorIdAndTenantId(String competitorId, String tenantId);

    void deleteByDealIdAndTenantId(String dealId, String tenantId);

    boolean existsByCompetitorIdAndTenantId(String competitorId, String tenantId);
}
