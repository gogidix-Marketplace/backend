package com.gogidix.shared.warehousing.storage.space.domain.repository;

import com.gogidix.shared.warehousing.storage.space.domain.entity.StorageSpace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Storage Space entity
 */
@Repository
public interface StorageSpaceRepository extends MongoRepository<StorageSpace, String> {

    List<StorageSpace> findByTenantId(String tenantId);

    List<StorageSpace> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    List<StorageSpace> findByTenantIdAndWarehouseIdAndStatus(
            String tenantId, String warehouseId, StorageSpace.SpaceStatus status);

    List<StorageSpace> findByTenantIdAndWarehouseIdAndSpaceType(
            String tenantId, String warehouseId, StorageSpace.SpaceType spaceType);

    List<StorageSpace> findByTenantIdAndWarehouseIdAndTemperatureZone(
            String tenantId, String warehouseId, String temperatureZone);

    @Query("{ 'tenantId': ?0, 'warehouseId': ?1, 'capacity.volumeCapacity': { $gte: ?2 } }")
    List<StorageSpace> findSpacesWithMinimumCapacity(String tenantId, String warehouseId, Double minVolume);

    boolean existsByTenantIdAndSpaceCode(String tenantId, String spaceCode);

    @Query("{ 'tenantId': ?0, 'warehouseId': ?1, 'status': 'AVAILABLE' }")
    List<StorageSpace> findAvailableSpaces(String tenantId, String warehouseId);
}
