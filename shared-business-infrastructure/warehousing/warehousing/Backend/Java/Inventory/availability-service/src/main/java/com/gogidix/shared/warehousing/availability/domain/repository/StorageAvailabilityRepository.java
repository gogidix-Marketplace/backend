package com.gogidix.shared.warehousing.availability.domain.repository;

import com.gogidix.shared.warehousing.availability.domain.entity.StorageAvailability;
import com.gogidix.shared.warehousing.availability.domain.entity.StorageAvailability.AvailabilityStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Storage Availability Repository
 *
 * MongoDB repository for storage availability tracking
 */
@Repository
public interface StorageAvailabilityRepository extends MongoRepository<StorageAvailability, String> {

    /**
     * Find all availability for tenant
     */
    List<StorageAvailability> findByTenantId(String tenantId);

    /**
     * Find availability by tenant and warehouse
     */
    List<StorageAvailability> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    /**
     * Find availability by tenant, warehouse, and zone
     */
    Optional<StorageAvailability> findByTenantIdAndWarehouseIdAndZoneId(
            String tenantId, String warehouseId, String zoneId);

    /**
     * Find availability by status for tenant
     */
    List<StorageAvailability> findByTenantIdAndStatus(String tenantId, AvailabilityStatus status);

    /**
     * Find available storage zones (has capacity)
     */
    @Query("{'tenantId': ?0, 'warehouseId': ?1, 'availableCapacity': {'$gt': 0}}")
    List<StorageAvailability> findAvailableZones(String tenantId, String warehouseId);

    /**
     * Find low availability zones (< threshold %)
     */
    @Query("{'tenantId': ?0, 'warehouseId': ?1, 'utilizationPercentage': {'$gte': ?2}}")
    List<StorageAvailability> findLowAvailabilityZones(String tenantId, String warehouseId, double threshold);
}
