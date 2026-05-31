package com.gogidix.shared.warehousing.access.domain.repository;

import com.gogidix.shared.warehousing.access.domain.entity.AccessLog;
import com.gogidix.shared.warehousing.access.domain.entity.AccessLog.AccessResult;
import com.gogidix.shared.warehousing.access.domain.entity.AccessLog.AccessType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Access Log Repository
 */
@Repository
public interface AccessLogRepository extends MongoRepository<AccessLog, String> {

    /**
     * Find logs by tenant and warehouse
     */
    List<AccessLog> findByTenantIdAndWarehouseIdOrderByAccessedAtDesc(
            String tenantId, String warehouseId);

    /**
     * Find logs by user
     */
    List<AccessLog> findByTenantIdAndUserIdOrderByAccessedAtDesc(
            String tenantId, String userId);

    /**
     * Find logs by time range
     */
    List<AccessLog> findByTenantIdAndWarehouseIdAndAccessedAtBetweenOrderByAccessedAtDesc(
            String tenantId, String warehouseId, LocalDateTime start, LocalDateTime end);

    /**
     * Find logs by access request ID
     */
    List<AccessLog> findByTenantIdAndAccessRequestId(String tenantId, String accessRequestId);

    /**
     * Find active entries (entries without exit)
     */
    List<AccessLog> findByTenantIdAndWarehouseIdAndAccessTypeAndExitAtIsNull(
            String tenantId, String warehouseId, AccessType accessType);

    /**
     * Find logs by result type
     */
    List<AccessLog> findByTenantIdAndResult(String tenantId, AccessResult result);

    /**
     * Find logs by zone
     */
    List<AccessLog> findByTenantIdAndZoneIdOrderByAccessedAtDesc(
            String tenantId, String zoneId);
}
