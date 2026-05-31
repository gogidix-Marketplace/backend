package com.gogidix.shared.warehousing.storage.domain.repository;

import com.gogidix.shared.warehousing.storage.domain.entity.StorageSpace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB Repository for StorageSpace entity
 */
@Repository
public interface StorageSpaceRepository extends MongoRepository<StorageSpace, String> {

    /**
     * Find all storage spaces by tenant ID
     */
    List<StorageSpace> findByTenantId(String tenantId);

    /**
     * Find available spaces by tenant and space type
     */
    List<StorageSpace> findByTenantIdAndStatusAndSpaceType(
        String tenantId,
        String status,
        String spaceType
    );

    /**
     * Find spaces by tenant and facility zone
     */
    List<StorageSpace> findByTenantIdAndFacilityZone(String tenantId, String facilityZone);

    /**
     * Find space by tenant and space code
     */
    StorageSpace findByTenantIdAndSpaceCode(String tenantId, String spaceCode);

    /**
     * Check if space code exists for tenant
     */
    boolean existsByTenantIdAndSpaceCode(String tenantId, String spaceCode);

    /**
     * Find spaces requiring optimization (utilization below threshold)
     */
    @Query("{ 'tenantId': ?0, 'status': 'AVAILABLE', 'availableCapacityCubicMeters': { $gt: 0 } }")
    List<StorageSpace> findOptimizableSpaces(String tenantId);

    /**
     * Get utilization statistics for tenant
     */
    @Query(value = "{ 'tenantId': ?0 }", fields = "{ 'spaceType': 1, 'totalCapacityCubicMeters': 1, 'availableCapacityCubicMeters': 1 }")
    List<StorageSpace> findUtilizationData(String tenantId);
}
