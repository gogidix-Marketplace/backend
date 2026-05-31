package com.gogidix.courier.assignmentservice.domain.repository;

import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DriverAssignment aggregates.
 * Defines the contract for driver assignment persistence operations.
 * All implementations MUST filter by tenantId for multi-tenancy.
 */
public interface DriverAssignmentRepository {

    /**
     * Save a driver assignment.
     *
     * @param assignment the assignment to save
     * @return the saved assignment
     */
    DriverAssignment save(DriverAssignment assignment);

    /**
     * Save all assignments.
     *
     * @param assignments the assignments to save
     * @return the saved assignments
     */
    List<DriverAssignment> saveAll(List<DriverAssignment> assignments);

    /**
     * Find an assignment by ID.
     *
     * @param id the assignment ID
     * @return the assignment if found
     */
    Optional<DriverAssignment> findById(String id);

    /**
     * Find an assignment by ID and tenant ID.
     *
     * @param id       the assignment ID
     * @param tenantId the tenant ID
     * @return the assignment if found
     */
    Optional<DriverAssignment> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find assignments by dispatch ID.
     *
     * @param dispatchId the dispatch ID
     * @return list of assignments
     */
    List<DriverAssignment> findByDispatchId(String dispatchId);

    /**
     * Find assignments by dispatch ID and tenant ID.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @return list of assignments
     */
    List<DriverAssignment> findByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Find active assignment by dispatch ID.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @return the active assignment if found
     */
    Optional<DriverAssignment> findActiveByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Find assignments by driver ID.
     *
     * @param driverId the driver ID
     * @return list of assignments
     */
    List<DriverAssignment> findByDriverId(String driverId);

    /**
     * Find assignments by driver ID and tenant ID.
     *
     * @param driverId the driver ID
     * @param tenantId the tenant ID
     * @return list of assignments
     */
    List<DriverAssignment> findByDriverIdAndTenantId(String driverId, String tenantId);

    /**
     * Find active assignments by driver ID.
     *
     * @param driverId the driver ID
     * @param tenantId the tenant ID
     * @return list of active assignments
     */
    List<DriverAssignment> findActiveByDriverIdAndTenantId(String driverId, String tenantId);

    /**
     * Find assignments by status.
     *
     * @param status   the assignment status
     * @param tenantId the tenant ID
     * @return list of assignments
     */
    List<DriverAssignment> findByStatusAndTenantId(DriverAssignment.AssignmentStatus status, String tenantId);

    /**
     * Find assignments by status and created before timestamp.
     *
     * @param status    the assignment status
     * @param timestamp the timestamp
     * @param tenantId  the tenant ID
     * @return list of assignments
     */
    List<DriverAssignment> findByStatusAndCreatedAtBeforeAndTenantId(
            DriverAssignment.AssignmentStatus status, Instant timestamp, String tenantId);

    /**
     * Find all assignments for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of all assignments
     */
    List<DriverAssignment> findByTenantId(String tenantId);

    /**
     * Find assignments with pagination.
     *
     * @param tenantId the tenant ID
     * @param page     the page number (0-indexed)
     * @param size     the page size
     * @return list of assignments
     */
    List<DriverAssignment> findByTenantIdPaginated(String tenantId, int page, int size);

    /**
     * Count all assignments for a tenant.
     *
     * @param tenantId the tenant ID
     * @return the count
     */
    long countByTenantId(String tenantId);

    /**
     * Count assignments by status and tenant.
     *
     * @param status   the assignment status
     * @param tenantId the tenant ID
     * @return the count
     */
    long countByStatusAndTenantId(DriverAssignment.AssignmentStatus status, String tenantId);

    /**
     * Count active assignments by driver.
     *
     * @param driverId the driver ID
     * @param tenantId the tenant ID
     * @return the count
     */
    long countActiveByDriverIdAndTenantId(String driverId, String tenantId);

    /**
     * Check if an active assignment exists for dispatch.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @return true if exists, false otherwise
     */
    boolean existsActiveByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Delete an assignment by ID.
     *
     * @param id the assignment ID
     */
    void deleteById(String id);

    /**
     * Find assignments pending for timeout.
     *
     * @param beforeTimestamp the timestamp
     * @param tenantId        the tenant ID
     * @return list of timed out assignments
     */
    List<DriverAssignment> findPendingAssignmentsOlderThan(Instant beforeTimestamp, String tenantId);
}
