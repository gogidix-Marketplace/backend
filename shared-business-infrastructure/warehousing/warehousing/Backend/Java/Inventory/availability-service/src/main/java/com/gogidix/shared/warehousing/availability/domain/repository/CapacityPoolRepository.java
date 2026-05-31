package com.gogidix.shared.warehousing.availability.domain.repository;

import com.gogidix.shared.warehousing.availability.domain.entity.CapacityPool;
import com.gogidix.shared.warehousing.availability.domain.entity.CapacityPool.PoolType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Capacity Pool Repository
 *
 * MongoDB repository for capacity pools
 */
@Repository
public interface CapacityPoolRepository extends MongoRepository<CapacityPool, String> {

    /**
     * Find all pools for tenant and warehouse
     */
    List<CapacityPool> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    /**
     * Find pool by tenant, warehouse, and name
     */
    Optional<CapacityPool> findByTenantIdAndWarehouseIdAndPoolName(
            String tenantId, String warehouseId, String poolName);

    /**
     * Find pools by type
     */
    List<CapacityPool> findByTenantIdAndWarehouseIdAndPoolType(
            String tenantId, String warehouseId, PoolType poolType);

    /**
     * Find pools with available capacity
     */
    @Query("{'tenantId': ?0, 'warehouseId': ?1, 'availableCapacity': {'$gt': 0}}")
    List<CapacityPool> findPoolsWithCapacity(String tenantId, String warehouseId);

    /**
     * Find pools at or above threshold
     */
    @Query("{'tenantId': ?0, 'warehouseId': ?1, $or: [{'reservationThreshold': null}, {'reservationThreshold': {'$gte': ?2}}]}")
    List<CapacityPool> findPoolsAtOrAboveThreshold(String tenantId, String warehouseId, double threshold);
}
