package com.gogidix.shared.warehousing.access.domain.repository;

import com.gogidix.shared.warehousing.access.domain.entity.AccessPermission;
import com.gogidix.shared.warehousing.access.domain.entity.AccessPermission.PermissionType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Access Permission Repository
 */
@Repository
public interface AccessPermissionRepository extends MongoRepository<AccessPermission, String> {

    /**
     * Find permissions by tenant and warehouse
     */
    List<AccessPermission> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    /**
     * Find permissions by user
     */
    List<AccessPermission> findByTenantIdAndUserId(String tenantId, String userId);

    /**
     * Find active permissions by user
     */
    @Query("{'tenantId': ?0, 'userId': ?1, 'active': true, $or: [{'validFrom': null}, {'validFrom': {'$lte': ?2}}], $or: [{'validUntil': null}, {'validUntil': {'$gte': ?2}}]}")
    List<AccessPermission> findActivePermissionsForUser(String tenantId, String userId, java.time.LocalDateTime now);

    /**
     * Find permissions by role
     */
    List<AccessPermission> findByTenantIdAndWarehouseIdAndRole(String tenantId, String warehouseId, String role);

    /**
     * Find permissions by type
     */
    List<AccessPermission> findByTenantIdAndPermissionType(String tenantId, PermissionType permissionType);

    /**
     * Find permissions requiring approval
     */
    List<AccessPermission> findByTenantIdAndWarehouseIdAndRequiresApprovalTrue(
            String tenantId, String warehouseId);
}
