package com.gogidix.shared.warehousing.batch.domain.repository;

import com.gogidix.shared.warehousing.batch.domain.entity.BatchLot;
import com.gogidix.shared.warehousing.batch.domain.entity.BatchLot.LotStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Batch Lot Repository with Multi-Tenant Support
 */
@Repository
public interface BatchLotRepository extends MongoRepository<BatchLot, String> {

    /**
     * Find batch lot by lot number and tenant
     */
    Optional<BatchLot> findByTenantIdAndLotNumber(String tenantId, String lotNumber);

    /**
     * Find batch lots by SKU and tenant
     */
    List<BatchLot> findByTenantIdAndSku(String tenantId, String sku);

    /**
     * Find batch lots by status and tenant
     */
    List<BatchLot> findByTenantIdAndStatus(String tenantId, LotStatus status);

    /**
     * Find batch lots by tenant and location
     */
    List<BatchLot> findByTenantIdAndLocationId(String tenantId, String locationId);

    /**
     * Find batch lots by supplier
     */
    List<BatchLot> findByTenantIdAndSupplierId(String tenantId, String supplierId);

    /**
     * Find batch lots expiring before a date
     */
    List<BatchLot> findByTenantIdAndExpirationDateBefore(String tenantId, LocalDate date);

    /**
     * Find batch lots with expiry dates within a range (ordered by expiry date)
     */
    List<BatchLot> findByTenantIdAndExpirationDateBetweenOrderByExpirationDateAsc(
        String tenantId, LocalDate startDate, LocalDate endDate);

    /**
     * Find all batch lots for a tenant
     */
    List<BatchLot> findByTenantId(String tenantId);

    /**
     * Check if lot number exists for tenant
     */
    boolean existsByTenantIdAndLotNumber(String tenantId, String lotNumber);

    /**
     * Find batch lots available for allocation (FEFO - First Expired First Out)
     */
    List<BatchLot> findByTenantIdAndSkuAndStatusOrderByExpirationDateAsc(
        String tenantId, String sku, LotStatus status);
}
