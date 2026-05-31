package com.gogidix.courier.gpstrackingservice.infrastructure.persistence.repository;

import com.gogidix.courier.gpstrackingservice.domain.entity.DriverTrackingSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository for DriverTrackingSession entities.
 */
@Repository
public interface MongoDriverTrackingSessionRepository extends MongoRepository<DriverTrackingSession, String> {

    /**
     * Find session by session ID.
     */
    Optional<DriverTrackingSession> findBySessionId(String sessionId);

    /**
     * Find session by tenant and session ID.
     */
    Optional<DriverTrackingSession> findByTenantIdAndSessionId(String tenantId, String sessionId);

    /**
     * Find active session by driver ID.
     */
    Optional<DriverTrackingSession> findByDriverIdAndStatus(String driverId, DriverTrackingSession.SessionStatus status);

    /**
     * Find active session by tenant and driver ID.
     */
    Optional<DriverTrackingSession> findByTenantIdAndDriverIdAndStatus(
            String tenantId, String driverId, DriverTrackingSession.SessionStatus status);

    /**
     * Find all sessions by driver ID.
     */
    List<DriverTrackingSession> findByDriverIdOrderByStartTimeDesc(String driverId);

    /**
     * Find all sessions by tenant and driver ID.
     */
    List<DriverTrackingSession> findByTenantIdAndDriverIdOrderByStartTimeDesc(String tenantId, String driverId);

    /**
     * Find all sessions by status.
     */
    List<DriverTrackingSession> findByStatus(DriverTrackingSession.SessionStatus status);

    /**
     * Find all active sessions.
     */
    List<DriverTrackingSession> findByStatusOrderByStartTimeDesc(DriverTrackingSession.SessionStatus status);

    /**
     * Find active sessions by tenant.
     */
    List<DriverTrackingSession> findByTenantIdAndStatusOrderByStartTimeDesc(
            String tenantId, DriverTrackingSession.SessionStatus status);

    /**
     * Find sessions within time range.
     */
    List<DriverTrackingSession> findByStartTimeBetweenOrderByStartTimeDesc(
            Instant startTime, Instant endTime);

    /**
     * Find sessions containing an order ID.
     */
    @Query("{ 'orderIds': { $in: [ ?0 ] } }")
    List<DriverTrackingSession> findByOrderId(String orderId);

    /**
     * Find sessions with stale location data.
     */
    @Query("{ 'status': 'ACTIVE', 'lastLocationTime': { $lt: ?0 } }")
    List<DriverTrackingSession> findByStatusAndLastLocationTimeBefore(
            DriverTrackingSession.SessionStatus status, Instant threshold);

    /**
     * Find sessions with stale location data by tenant.
     */
    @Query("{ 'tenantId': ?0, 'status': 'ACTIVE', 'lastLocationTime': { $lt: ?1 } }")
    List<DriverTrackingSession> findByTenantIdAndStatusAndLastLocationTimeBefore(
            String tenantId, DriverTrackingSession.SessionStatus status, Instant threshold);

    /**
     * Count sessions by status.
     */
    long countByStatus(DriverTrackingSession.SessionStatus status);

    /**
     * Count sessions by driver ID.
     */
    long countByDriverId(String driverId);

    /**
     * Delete sessions older than specified date.
     */
    long deleteByEndTimeBefore(Instant before);

    /**
     * Find all paginated.
     */
    List<DriverTrackingSession> findAllByOrderByStartTimeDesc(Pageable pageable);
}
