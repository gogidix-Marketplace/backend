package com.gogidix.shared.warehousing.spaceallocation.domain.repository;

import com.gogidix.shared.warehousing.spaceallocation.domain.entity.SpaceAllocation;
import com.gogidix.shared.warehousing.spaceallocation.domain.entity.SpaceAllocation.AllocationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Space Allocation Repository
 */
@Repository
public interface SpaceAllocationRepository extends MongoRepository<SpaceAllocation, String> {

    /**
     * Find allocations by tenant and warehouse
     */
    List<SpaceAllocation> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    /**
     * Find allocations by zone
     */
    List<SpaceAllocation> findByTenantIdAndWarehouseIdAndZoneId(
            String tenantId, String warehouseId, String zoneId);

    /**
     * Find allocations by status
     */
    List<SpaceAllocation> findByTenantIdAndStatus(String tenantId, AllocationStatus status);

    /**
     * Find allocations by item
     */
    List<SpaceAllocation> findByTenantIdAndItemId(String tenantId, String itemId);

    /**
     * Find allocations by SKU
     */
    List<SpaceAllocation> findByTenantIdAndSku(String tenantId, String sku);

    /**
     * Find active allocations in zone
     */
    @Query("{'tenantId': ?0, 'warehouseId': ?1, 'zoneId': ?2, 'status': {'$in': ['ALLOCATED', 'OCCUPIED']}}")
    List<SpaceAllocation> findActiveAllocationsInZone(String tenantId, String warehouseId, String zoneId);

    /**
     * Find expired allocations
     */
    @Query("{'tenantId': ?0, 'expiresAt': {'$lt': ?1}, 'status': {'$in': ['ALLOCATED', 'OCCUPIED']}}")
    List<SpaceAllocation> findExpiredAllocations(String tenantId, java.time.LocalDateTime now);

    /**
     * Find allocations by reference
     */
    List<SpaceAllocation> findByTenantIdAndReferenceIdAndReferenceType(
            String tenantId, String referenceId, String referenceType);
}
