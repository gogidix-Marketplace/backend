package com.gogidix.shared.warehousing.access.domain.repository;

import com.gogidix.shared.warehousing.access.domain.entity.AccessRequest;
import com.gogidix.shared.warehousing.access.domain.entity.AccessRequest.RequestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Access Request Repository
 */
@Repository
public interface AccessRequestRepository extends MongoRepository<AccessRequest, String> {

    /**
     * Find requests by tenant and warehouse
     */
    List<AccessRequest> findByTenantIdAndWarehouseIdOrderByCreatedAtDesc(
            String tenantId, String warehouseId);

    /**
     * Find requests by tenant and user
     */
    List<AccessRequest> findByTenantIdAndRequestedByOrderByCreatedAtDesc(
            String tenantId, String requestedBy);

    /**
     * Find requests by status
     */
    List<AccessRequest> findByTenantIdAndStatus(String tenantId, RequestStatus status);

    /**
     * Find pending requests for warehouse
     */
    List<AccessRequest> findByTenantIdAndWarehouseIdAndStatusOrderByCreatedAtAsc(
            String tenantId, String warehouseId, RequestStatus status);

    /**
     * Find requests by request ID
     */
    List<AccessRequest> findByTenantIdAndRequestId(String tenantId, String requestId);

    /**
     * Find expired requests
     */
    @Query("{'tenantId': ?0, 'requestedEndTime': {'$lt': ?1}, 'status': {'$in': ['PENDING', 'APPROVED']}}")
    List<AccessRequest> findExpiredRequests(String tenantId, LocalDateTime now);

    /**
     * Find active requests (approved or in progress)
     */
    @Query("{'tenantId': ?0, 'warehouseId': ?1, 'status': {'$in': ['APPROVED', 'IN_PROGRESS']}}")
    List<AccessRequest> findActiveRequests(String tenantId, String warehouseId);
}
