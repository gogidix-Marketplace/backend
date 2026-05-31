package com.gogidix.shared.warehousing.warehouseconfig.domain.repository;

import com.gogidix.shared.warehousing.warehouseconfig.domain.entity.WarehouseConfig;
import com.gogidix.shared.warehousing.warehouseconfig.domain.entity.WarehouseConfig.WarehouseStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Warehouse Config Repository
 */
@Repository
public interface WarehouseConfigRepository extends MongoRepository<WarehouseConfig, String> {

    /**
     * Find by tenant and warehouse ID
     */
    Optional<WarehouseConfig> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    /**
     * Find all configs for tenant
     */
    List<WarehouseConfig> findByTenantId(String tenantId);

    /**
     * Find active configs for tenant
     */
    List<WarehouseConfig> findByTenantIdAndActiveTrue(String tenantId);

    /**
     * Find by status
     */
    List<WarehouseConfig> findByTenantIdAndStatus(String tenantId, WarehouseStatus status);

    /**
     * Find by warehouse code
     */
    Optional<WarehouseConfig> findByTenantIdAndWarehouseCode(String tenantId, String warehouseCode);
}
