package com.gogidix.shared.warehousing.inventory.domain.repository;

import com.gogidix.shared.warehousing.inventory.domain.entity.Inventory;
import com.gogidix.shared.warehousing.inventory.domain.entity.Inventory.TenantType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Inventory Repository with Multi-Tenant Support
 *
 * MongoDB-based repository for inventory management
 */
@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String> {

    /**
     * Find inventory by SKU and tenant
     */
    List<Inventory> findByTenantIdAndSku(String tenantId, String sku);

    /**
     * Find inventory by tenant and location
     */
    List<Inventory> findByTenantIdAndLocationId(String tenantId, String locationId);

    /**
     * Find inventory by tenant, SKU, and location
     */
    Optional<Inventory> findByTenantIdAndSkuAndLocationId(String tenantId, String sku, String locationId);

    /**
     * Find all inventory for a tenant with quantity > 0
     */
    List<Inventory> findByTenantIdAndQuantityGreaterThan(String tenantId, int quantity);

    /**
     * Find inventory by tenant type and SKU
     */
    List<Inventory> findByTenantTypeAndSku(TenantType tenantType, String sku);

    /**
     * Find inventory requiring restocking (quantity < threshold)
     */
    List<Inventory> findByTenantIdAndQuantityLessThan(String tenantId, int threshold);

    /**
     * Count total inventory items for tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Find all inventory for a specific tenant
     */
    List<Inventory> findByTenantId(String tenantId);
}
