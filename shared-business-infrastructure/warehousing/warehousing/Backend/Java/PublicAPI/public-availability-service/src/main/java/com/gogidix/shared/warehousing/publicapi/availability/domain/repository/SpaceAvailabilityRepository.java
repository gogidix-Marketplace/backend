package com.gogidix.shared.warehousing.publicapi.availability.domain.repository;

import com.gogidix.shared.warehousing.publicapi.availability.domain.entity.SpaceAvailability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Space Availability entity
 */
@Repository
public interface SpaceAvailabilityRepository extends MongoRepository<SpaceAvailability, String> {

    List<SpaceAvailability> findByTenantId(String tenantId);

    List<SpaceAvailability> findByTenantIdAndStatus(String tenantId, SpaceAvailability.AvailabilityStatus status);

    @Query("{ 'tenantId': ?0, 'status': 'AVAILABLE', 'availableFrom': { $lte: ?1 }, $or: [ { 'availableUntil': null }, { 'availableUntil': { $gte: ?1 } } ] }")
    List<SpaceAvailability> findAvailableAtDate(String tenantId, LocalDateTime date);

    @Query("{ 'tenantId': ?0, 'status': 'AVAILABLE', 'climateControlled': true }")
    List<SpaceAvailability> findClimateControlled(String tenantId);

    @Query("{ 'tenantId': ?0, 'status': 'AVAILABLE', 'pricePerUnit': { $lte: ?1 } }")
    List<SpaceAvailability> findByMaxPrice(String tenantId, Double maxPrice);

    List<SpaceAvailability> findByTenantIdAndCity(String tenantId, String city);

    List<SpaceAvailability> findByTenantIdAndState(String tenantId, String state);

    @Query("{ 'tenantId': ?0, 'warehouseId': ?1, 'status': 'AVAILABLE' }")
    List<SpaceAvailability> findAvailableByWarehouse(String tenantId, String warehouseId);
}
