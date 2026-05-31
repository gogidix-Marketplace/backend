package com.gogidix.shared.warehousing.cyclecounting.domain.repository;

import com.gogidix.shared.warehousing.cyclecounting.domain.entity.CountDiscrepancy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Count Discrepancy Repository
 */
@Repository
public interface CountDiscrepancyRepository extends MongoRepository<CountDiscrepancy, String> {

    /**
     * Find discrepancies by cycle count
     */
    List<CountDiscrepancy> findByTenantIdAndCycleCountIdOrderByCreatedAtDesc(
            String tenantId, String cycleCountId);

    /**
     * Find unresolved discrepancies
     */
    List<CountDiscrepancy> findByTenantIdAndResolvedFalse(String tenantId);

    /**
     * Find discrepancies by SKU
     */
    List<CountDiscrepancy> findByTenantIdAndSkuOrderByCreatedAtDesc(String tenantId, String sku);

    /**
     * Find discrepancies by status
     */
    List<CountDiscrepancy> findByTenantIdAndStatus(String tenantId,
            CountDiscrepancy.DiscrepancyStatus status);

    /**
     * Find discrepancies by type
     */
    List<CountDiscrepancy> findByTenantIdAndDiscrepancyType(String tenantId,
            CountDiscrepancy.DiscrepancyType discrepancyType);
}
