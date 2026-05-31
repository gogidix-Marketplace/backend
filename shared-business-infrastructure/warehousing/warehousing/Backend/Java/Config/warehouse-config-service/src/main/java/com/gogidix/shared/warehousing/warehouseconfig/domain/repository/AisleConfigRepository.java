package com.gogidix.shared.warehousing.warehouseconfig.domain.repository;

import com.gogidix.shared.warehousing.warehouseconfig.domain.entity.AisleConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Aisle Config Repository
 */
@Repository
public interface AisleConfigRepository extends MongoRepository<AisleConfig, String> {

    /**
     * Find aisles by tenant and warehouse
     */
    List<AisleConfig> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    /**
     * Find aisles by zone
     */
    List<AisleConfig> findByTenantIdAndWarehouseIdAndZoneId(String tenantId, String warehouseId, String zoneId);

    /**
     * Find by aisle ID
     */
    List<AisleConfig> findByTenantIdAndWarehouseIdAndZoneIdAndAisleId(
            String tenantId, String warehouseId, String zoneId, String aisleId);

    /**
     * Find by status
     */
    List<AisleConfig> findByTenantIdAndWarehouseIdAndStatus(
            String tenantId, String warehouseId, AisleConfig.AisleStatus status);
}
