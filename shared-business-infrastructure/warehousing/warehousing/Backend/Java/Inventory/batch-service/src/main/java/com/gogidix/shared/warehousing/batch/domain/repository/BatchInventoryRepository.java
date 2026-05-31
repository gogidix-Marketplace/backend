package com.gogidix.shared.warehousing.batch.domain.repository;

import com.gogidix.shared.warehousing.batch.domain.entity.BatchInventory;
import com.gogidix.shared.warehousing.batch.domain.entity.BatchInventory.InventoryStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Batch Inventory Repository with Multi-Tenant Support
 */
@Repository
public interface BatchInventoryRepository extends MongoRepository<BatchInventory, String> {

    /**
     * Find batch inventory by lot ID, SKU, and tenant
     */
    Optional<BatchInventory> findByTenantIdAndLotIdAndSku(String tenantId, String lotId, String sku);

    /**
     * Find batch inventory by lot ID
     */
    List<BatchInventory> findByLotId(String lotId);

    /**
     * Find batch inventory by SKU and tenant
     */
    List<BatchInventory> findByTenantIdAndSku(String tenantId, String sku);

    /**
     * Find batch inventory by status and tenant
     */
    List<BatchInventory> findByTenantIdAndStatus(String tenantId, InventoryStatus status);

    /**
     * Find batch inventory by tenant and location
     */
    List<BatchInventory> findByTenantIdAndLocationId(String tenantId, String locationId);

    /**
     * Find batch inventory with low available quantity
     */
    List<BatchInventory> findByTenantIdAndQuantityAvailableLessThan(String tenantId, int threshold);

    /**
     * Find all batch inventory for a tenant
     */
    List<BatchInventory> findByTenantId(String tenantId);

    /**
     * Delete batch inventory by lot ID
     */
    void deleteByLotId(String lotId);
}
