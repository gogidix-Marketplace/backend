package com.gogidix.sales.territory.domain.repository;

import com.gogidix.sales.territory.domain.model.Territory;

import java.util.List;
import java.util.Optional;

/**
 * Territory Repository Interface (Port)
 * Defines the contract for territory persistence operations
 */
public interface TerritoryRepository {

    Territory save(Territory territory);

    List<Territory> saveAll(List<Territory> territories);

    Optional<Territory> findById(String id);

    Optional<Territory> findByTerritoryIdAndTenantId(String territoryId, String tenantId);

    List<Territory> findByTenantId(String tenantId);

    List<Territory> findByTenantIdAndStatus(String tenantId, Territory.TerritoryStatus status);

    List<Territory> findByTenantIdAndType(String tenantId, Territory.TerritoryType type);

    List<Territory> findByTenantIdAndRegionId(String tenantId, String regionId);

    List<Territory> findByTenantIdAndManagerId(String tenantId, String managerId);

    List<Territory> findByTenantIdAndParentTerritoryId(String tenantId, String parentTerritoryId);

    List<Territory> findActiveByTenantId(String tenantId);

    List<Territory> findPendingRealignmentByTenantId(String tenantId);

    Optional<Territory> findByCodeAndTenantId(String code, String tenantId);

    boolean existsByCodeAndTenantId(String code, String tenantId);

    boolean existsByTerritoryIdAndTenantId(String territoryId, String tenantId);

    void deleteById(String id);

    void deleteByTerritoryIdAndTenantId(String territoryId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Territory.TerritoryStatus status);

    List<Territory> findGeographicTerritoriesByTenantId(String tenantId);

    List<Territory> findByTenantIdAndProductCategoriesContaining(String tenantId, String productCategory);

    List<Territory> findByTenantIdAndCustomerSegmentsContaining(String tenantId, String customerSegment);
}
