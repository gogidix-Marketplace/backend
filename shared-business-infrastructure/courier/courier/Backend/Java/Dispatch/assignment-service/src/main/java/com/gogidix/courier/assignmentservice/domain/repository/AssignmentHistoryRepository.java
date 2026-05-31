package com.gogidix.courier.assignmentservice.domain.repository;

import com.gogidix.courier.assignmentservice.domain.entity.AssignmentHistory;

import java.time.Instant;
import java.util.List;

/**
 * Repository interface for AssignmentHistory aggregates.
 * Defines the contract for assignment history persistence operations.
 */
public interface AssignmentHistoryRepository {

    /**
     * Save an assignment history record.
     *
     * @param history the history record to save
     * @return the saved history record
     */
    AssignmentHistory save(AssignmentHistory history);

    /**
     * Save all history records.
     *
     * @param historyList the history records to save
     * @return the saved history records
     */
    List<AssignmentHistory> saveAll(List<AssignmentHistory> historyList);

    /**
     * Find history by assignment ID.
     *
     * @param assignmentId the assignment ID
     * @return list of history records
     */
    List<AssignmentHistory> findByAssignmentId(String assignmentId);

    /**
     * Find history by assignment ID and tenant ID.
     *
     * @param assignmentId the assignment ID
     * @param tenantId     the tenant ID
     * @return list of history records
     */
    List<AssignmentHistory> findByAssignmentIdAndTenantId(String assignmentId, String tenantId);

    /**
     * Find history by driver ID.
     *
     * @param driverId the driver ID
     * @param tenantId the tenant ID
     * @param limit    maximum number of records
     * @return list of history records
     */
    List<AssignmentHistory> findByDriverIdAndTenantId(String driverId, String tenantId, int limit);

    /**
     * Find history by dispatch ID.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @return list of history records
     */
    List<AssignmentHistory> findByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Find history by event type and tenant.
     *
     * @param eventType the event type
     * @param tenantId  the tenant ID
     * @param limit     maximum number of records
     * @return list of history records
     */
    List<AssignmentHistory> findByEventTypeAndTenantId(
            AssignmentHistory.HistoryEventType eventType, String tenantId, int limit);

    /**
     * Find history within date range for a tenant.
     *
     * @param tenantId the tenant ID
     * @param fromDate the start date
     * @param toDate   the end date
     * @return list of history records
     */
    List<AssignmentHistory> findByTenantIdAndTimestampBetween(
            String tenantId, Instant fromDate, Instant toDate);

    /**
     * Count history records by assignment ID.
     *
     * @param assignmentId the assignment ID
     * @return the count
     */
    long countByAssignmentId(String assignmentId);

    /**
     * Delete history by assignment ID.
     *
     * @param assignmentId the assignment ID
     */
    void deleteByAssignmentId(String assignmentId);

    /**
     * Find recent history for a driver.
     *
     * @param driverId the driver ID
     * @param tenantId the tenant ID
     * @param days     number of days to look back
     * @return list of history records
     */
    List<AssignmentHistory> findRecentByDriverIdAndTenantId(
            String driverId, String tenantId, int days);
}
