package com.gogidix.shared.warehousing.spaceallocation.domain.repository;

import com.gogidix.shared.warehousing.spaceallocation.domain.entity.ZoneCapacity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Zone Capacity Repository
 */
@Repository
public interface ZoneCapacityRepository extends MongoRepository<ZoneCapacity, String> {

    /**
     * Find capacity by tenant, warehouse, and zone
     */
    Optional<ZoneCapacity> findByTenantIdAndWarehouseIdAndZoneId(
            String tenantId, String warehouseId, String zoneId);

    /**
     * Find all zones for warehouse
     */
    List<ZoneCapacity> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    /**
     * Find zones with available capacity
     */
    @Query("{'tenantId': ?0, 'warehouseId': ?1, 'availableVolume': {'$gt': 0}}")
    List<ZoneCapacity> findZonesWithCapacity(String tenantId, String warehouseId);

    /**
     * Find zones at or above capacity threshold
     */
    @Query("{'tenantId': ?0, 'warehouseId': ?1, 'utilizationPercentage': {'$gte': ?2}}")
    List<ZoneCapacity> findZonesAboveThreshold(String tenantId, String warehouseId, double threshold);
}
