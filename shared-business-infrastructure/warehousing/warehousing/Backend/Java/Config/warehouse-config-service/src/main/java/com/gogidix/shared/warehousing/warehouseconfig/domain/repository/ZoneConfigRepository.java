package com.gogidix.shared.warehousing.warehouseconfig.domain.repository;

import com.gogidix.shared.warehousing.warehouseconfig.domain.entity.ZoneConfig;
import com.gogidix.shared.warehousing.warehouseconfig.domain.entity.ZoneConfig.ZoneStatus;
import com.gogidix.shared.warehousing.warehouseconfig.domain.entity.ZoneConfig.ZoneType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Zone Config Repository
 */
@Repository
public interface ZoneConfigRepository extends MongoRepository<ZoneConfig, String> {

    /**
     * Find zones by tenant and warehouse
     */
    List<ZoneConfig> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    /**
     * Find by tenant, warehouse, and zone ID
     */
    List<ZoneConfig> findByTenantIdAndWarehouseIdAndZoneId(String tenantId, String warehouseId, String zoneId);

    /**
     * Find by zone type
     */
    List<ZoneConfig> findByTenantIdAndWarehouseIdAndZoneType(String tenantId, String warehouseId, ZoneType zoneType);

    /**
     * Find active zones
     */
    List<ZoneConfig> findByTenantIdAndWarehouseIdAndStatus(String tenantId, String warehouseId, ZoneStatus status);
}
