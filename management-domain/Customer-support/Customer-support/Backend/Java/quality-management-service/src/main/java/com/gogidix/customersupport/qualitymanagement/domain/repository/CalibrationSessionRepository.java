package com.gogidix.customersupport.qualitymanagement.domain.repository;

import com.gogidix.customersupport.qualitymanagement.domain.model.CalibrationSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CalibrationSession entity
 */
@Repository
public interface CalibrationSessionRepository extends MongoRepository<CalibrationSession, String> {

    /**
     * Find session by session ID
     */
    Optional<CalibrationSession> findBySessionId(String sessionId);

    /**
     * Find sessions by tenant ID
     */
    List<CalibrationSession> findByTenantIdOrderByScheduledDateDesc(String tenantId);

    /**
     * Find sessions by tenant ID with pagination
     */
    Page<CalibrationSession> findByTenantIdOrderByScheduledDateDesc(String tenantId, Pageable pageable);

    /**
     * Find sessions by status
     */
    List<CalibrationSession> findByTenantIdAndSessionStatusOrderByScheduledDateDesc(
            String tenantId, CalibrationSession.SessionStatus sessionStatus);

    /**
     * Find sessions by type
     */
    List<CalibrationSession> findByTenantIdAndSessionTypeOrderByScheduledDateDesc(
            String tenantId, CalibrationSession.SessionType sessionType);

    /**
     * Find sessions by facilitator
     */
    List<CalibrationSession> findByTenantIdAndFacilitatorIdOrderByScheduledDateDesc(String tenantId, String facilitatorId);

    /**
     * Find upcoming sessions
     */
    @Query("{ 'tenantId': ?0, 'sessionStatus': 'SCHEDULED', 'scheduledDate': { $gte: ?1 } }")
    List<CalibrationSession> findUpcomingSessions(String tenantId, Instant now);

    /**
     * Find past sessions
     */
    @Query("{ 'tenantId': ?0, $or: [ { 'sessionStatus': 'COMPLETED' }, { 'scheduledDate': { $lt: ?1 } } ] }")
    List<CalibrationSession> findPastSessions(String tenantId, Instant now);

    /**
     * Find sessions in date range
     */
    List<CalibrationSession> findByTenantIdAndScheduledDateBetweenOrderByScheduledDateDesc(
            String tenantId, Instant startDate, Instant endDate);

    /**
     * Find active sessions
     */
    List<CalibrationSession> findByTenantIdAndSessionStatus(String tenantId, CalibrationSession.SessionStatus sessionStatus);

    /**
     * Find sessions requiring follow-up
     */
    @Query("{ 'tenantId': ?0, 'sessionStatus': 'COMPLETED', 'followUpRequired': true, $or: [ " +
           "{ 'followUpDate': null }, { 'followUpDate': { $lte: ?1 } } ] }")
    List<CalibrationSession> findSessionsRequiringFollowUp(String tenantId, Instant now);

    /**
     * Find sessions by scorecard template
     */
    List<CalibrationSession> findByTenantIdAndScorecardTemplateIdOrderByScheduledDateDesc(
            String tenantId, String templateId);

    /**
     * Find sessions where agent was a participant
     */
    @Query("{ 'tenantId': ?0, 'participants.participantId': ?1 }")
    List<CalibrationSession> findSessionsByParticipant(String tenantId, String participantId);

    /**
     * Count sessions by status
     */
    long countByTenantIdAndSessionStatus(String tenantId, CalibrationSession.SessionStatus sessionStatus);

    /**
     * Count sessions by facilitator
     */
    long countByTenantIdAndFacilitatorId(String tenantId, String facilitatorId);

    /**
     * Find completed sessions with calibration score
     */
    @Query("{ 'tenantId': ?0, 'sessionStatus': 'COMPLETED', 'calibrationScore': { $ne: null } }")
    List<CalibrationSession> findCompletedSessionsWithScores(String tenantId);

    /**
     * Find sessions by tags
     */
    @Query("{ 'tenantId': ?0, 'tags': { $in: ?1 } }")
    List<CalibrationSession> findByTenantIdAndTagsContaining(String tenantId, List<String> tags);

    /**
     * Find virtual sessions
     */
    List<CalibrationSession> findByTenantIdAndIsVirtualTrueOrderByScheduledDateDesc(String tenantId);

    /**
     * Find in-person sessions
     */
    List<CalibrationSession> findByTenantIdAndIsVirtualFalseOrderByScheduledDateDesc(String tenantId);

    /**
     * Delete sessions older than specified date
     */
    void deleteByTenantIdAndUpdatedAtBefore(String tenantId, Instant cutoffDate);

    /**
     * Find sessions with action items pending
     */
    @Query("{ 'tenantId': ?0, 'actionItems.status': { $in: ['PENDING', 'IN_PROGRESS'] } }")
    List<CalibrationSession> findSessionsWithPendingActionItems(String tenantId);

    /**
     * Find recent completed sessions
     */
    @Query("{ 'tenantId': ?0, 'sessionStatus': 'COMPLETED' }")
    List<CalibrationSession> findRecentCompletedSessions(String tenantId);

    /**
     * Get sessions statistics
     */
    @Query(value = "{ 'tenantId': ?0 }", count = true)
    long countAllSessions(String tenantId);

    /**
     * Find sessions that passed calibration
     */
    @Query("{ 'tenantId': ?0, 'sessionStatus': 'COMPLETED', 'calibrationPassed': true }")
    List<CalibrationSession> findPassedCalibrations(String tenantId);

    /**
     * Find sessions that failed calibration
     */
    @Query("{ 'tenantId': ?0, 'sessionStatus': 'COMPLETED', 'calibrationPassed': false }")
    List<CalibrationSession> findFailedCalibrations(String tenantId);
}
