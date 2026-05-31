package com.gogidix.courier.assignmentservice.infrastructure.persistence.repository;

import com.gogidix.courier.assignmentservice.domain.entity.AssignmentHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data MongoDB repository for AssignmentHistory.
 */
@Repository
public interface MongoAssignmentHistoryRepository extends MongoRepository<AssignmentHistory, String> {

    /**
     * Find history by assignment ID.
     */
    List<AssignmentHistory> findByAssignmentId(String assignmentId);

    /**
     * Find history by assignment ID and tenant ID.
     */
    List<AssignmentHistory> findByAssignmentIdAndTenantId(String assignmentId, String tenantId);

    /**
     * Find history by driver ID and tenant ID with limit.
     */
    @Query("{ 'driverId': ?0, 'tenantId': ?1 }")
    List<AssignmentHistory> findByDriverIdAndTenantId(String driverId, String tenantId);

    /**
     * Find history by dispatch ID and tenant ID.
     */
    List<AssignmentHistory> findByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Find history by event type and tenant ID with limit.
     */
    List<AssignmentHistory> findByEventTypeAndTenantId(
            AssignmentHistory.HistoryEventType eventType, String tenantId);

    /**
     * Find history by tenant ID within date range.
     */
    List<AssignmentHistory> findByTenantIdAndTimestampBetween(
            String tenantId, Instant fromDate, Instant toDate);

    /**
     * Count history records by assignment ID.
     */
    long countByAssignmentId(String assignmentId);

    /**
     * Delete history by assignment ID.
     */
    void deleteByAssignmentId(String assignmentId);

    /**
     * Find recent history by driver ID and tenant ID.
     */
    @Query("{ 'driverId': ?0, 'tenantId': ?1, 'timestamp': { $gte: ?2 } }")
    List<AssignmentHistory> findRecentByDriverIdAndTenantId(
            String driverId, String tenantId, Instant since);
}
