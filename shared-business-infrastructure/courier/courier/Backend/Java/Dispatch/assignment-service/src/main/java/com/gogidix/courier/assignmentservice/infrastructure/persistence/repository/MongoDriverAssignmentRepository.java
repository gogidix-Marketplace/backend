package com.gogidix.courier.assignmentservice.infrastructure.persistence.repository;

import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data MongoDB repository for DriverAssignment.
 */
@Repository
public interface MongoDriverAssignmentRepository extends MongoRepository<DriverAssignment, String> {

    /**
     * Find assignment by dispatch ID.
     */
    List<DriverAssignment> findByDispatchId(String dispatchId);

    /**
     * Find assignment by dispatch ID and tenant ID.
     */
    List<DriverAssignment> findByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Find active assignments by dispatch ID and tenant ID.
     */
    @Query("{ 'dispatchId': ?0, 'tenantId': ?1, 'status': { $in: ['PENDING', 'ACCEPTED', 'IN_PROGRESS'] } }")
    List<DriverAssignment> findActiveByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Find assignment by driver ID and tenant ID.
     */
    List<DriverAssignment> findByDriverIdAndTenantId(String driverId, String tenantId);

    /**
     * Find active assignments by driver ID and tenant ID.
     */
    @Query("{ 'driverId': ?0, 'tenantId': ?1, 'status': { $in: ['PENDING', 'ACCEPTED', 'IN_PROGRESS'] } }")
    List<DriverAssignment> findActiveByDriverIdAndTenantId(String driverId, String tenantId);

    /**
     * Find assignments by status and tenant ID.
     */
    List<DriverAssignment> findByStatusAndTenantId(DriverAssignment.AssignmentStatus status, String tenantId);

    /**
     * Find assignments by status, created before timestamp, and tenant ID.
     */
    List<DriverAssignment> findByStatusAndCreatedAtBeforeAndTenantId(
            DriverAssignment.AssignmentStatus status, Instant timestamp, String tenantId);

    /**
     * Find all assignments by tenant ID.
     */
    List<DriverAssignment> findByTenantId(String tenantId);

    /**
     * Find assignments by tenant ID with pagination.
     */
    Page<DriverAssignment> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Count assignments by tenant ID.
     */
    long countByTenantId(String tenantId);

    /**
     * Count assignments by status and tenant ID.
     */
    long countByStatusAndTenantId(DriverAssignment.AssignmentStatus status, String tenantId);

    /**
     * Count active assignments by driver ID and tenant ID.
     */
    @Query("{ 'driverId': ?0, 'tenantId': ?1, 'status': { $in: ['PENDING', 'ACCEPTED', 'IN_PROGRESS'] } }")
    long countActiveByDriverIdAndTenantId(String driverId, String tenantId);

    /**
     * Check if active assignment exists by dispatch ID and tenant ID.
     */
    @Query(value = "{ 'dispatchId': ?0, 'tenantId': ?1, 'status': { $in: ['PENDING', 'ACCEPTED', 'IN_PROGRESS'] } }", exists = true)
    boolean existsActiveByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Find pending assignments older than timestamp.
     */
    @Query("{ 'status': 'PENDING', 'createdAt': { $lt: ?0 }, 'tenantId': ?1 }")
    List<DriverAssignment> findPendingAssignmentsOlderThan(Instant timestamp, String tenantId);
}
