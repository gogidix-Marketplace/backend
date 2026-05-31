package com.gogidix.courier.gpstrackingservice.domain.repository;

import com.gogidix.courier.gpstrackingservice.domain.entity.DriverTrackingSession;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DriverTrackingSession aggregates.
 * Defines the contract for tracking session persistence operations.
 * All implementations MUST filter by tenantId for multi-tenancy.
 */
public interface DriverTrackingSessionRepository {

    /**
     * Save a tracking session.
     *
     * @param session the session to save
     * @return the saved session
     */
    DriverTrackingSession save(DriverTrackingSession session);

    /**
     * Find a session by ID.
     *
     * @param id the session ID
     * @return the session if found
     */
    Optional<DriverTrackingSession> findById(String id);

    /**
     * Find a session by session ID.
     *
     * @param sessionId the session identifier
     * @return the session if found
     */
    Optional<DriverTrackingSession> findBySessionId(String sessionId);

    /**
     * Find a session by session ID within a tenant.
     *
     * @param tenantId the tenant ID
     * @param sessionId the session identifier
     * @return the session if found
     */
    Optional<DriverTrackingSession> findByTenantIdAndSessionId(String tenantId, String sessionId);

    /**
     * Find the active session for a driver.
     *
     * @param driverId the driver ID
     * @return the active session if found
     */
    Optional<DriverTrackingSession> findActiveByDriverId(String driverId);

    /**
     * Find the active session for a driver within a tenant.
     *
     * @param tenantId the tenant ID
     * @param driverId the driver ID
     * @return the active session if found
     */
    Optional<DriverTrackingSession> findActiveByTenantIdAndDriverId(String tenantId, String driverId);

    /**
     * Find all sessions for a driver.
     *
     * @param driverId the driver ID
     * @return list of sessions
     */
    List<DriverTrackingSession> findByDriverId(String driverId);

    /**
     * Find all sessions for a driver within a tenant.
     *
     * @param tenantId the tenant ID
     * @param driverId the driver ID
     * @return list of sessions
     */
    List<DriverTrackingSession> findByTenantIdAndDriverId(String tenantId, String driverId);

    /**
     * Find all sessions with a specific status.
     *
     * @param status the session status
     * @return list of sessions
     */
    List<DriverTrackingSession> findByStatus(DriverTrackingSession.SessionStatus status);

    /**
     * Find all active sessions.
     *
     * @return list of active sessions
     */
    List<DriverTrackingSession> findAllActive();

    /**
     * Find all active sessions within a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of active sessions
     */
    List<DriverTrackingSession> findActiveByTenantId(String tenantId);

    /**
     * Find sessions within a time range.
     *
     * @param startTime the start time
     * @param endTime the end time
     * @return list of sessions
     */
    List<DriverTrackingSession> findByStartTimeBetween(Instant startTime, Instant endTime);

    /**
     * Find sessions for an order.
     *
     * @param orderId the order ID
     * @return list of sessions
     */
    List<DriverTrackingSession> findByOrderId(String orderId);

    /**
     * Find sessions within a date range paginated.
     *
     * @param page the page number (0-indexed)
     * @param size the page size
     * @return list of sessions
     */
    List<DriverTrackingSession> findAllPaginated(int page, int size);

    /**
     * Count all sessions.
     *
     * @return the count
     */
    long count();

    /**
     * Count sessions by status.
     *
     * @param status the session status
     * @return the count
     */
    long countByStatus(DriverTrackingSession.SessionStatus status);

    /**
     * Count sessions for a driver.
     *
     * @param driverId the driver ID
     * @return the count
     */
    long countByDriverId(String driverId);

    /**
     * Delete a session by ID.
     *
     * @param id the session ID
     */
    void deleteById(String id);

    /**
     * Delete old sessions older than specified date.
     *
     * @param before the cutoff date
     * @return the number of deleted sessions
     */
    long deleteOlderThan(Instant before);

    /**
     * Find sessions with stale location data.
     *
     * @param staleMinutes the threshold in minutes
     * @return list of sessions with stale data
     */
    List<DriverTrackingSession> findWithStaleLocation(int staleMinutes);

    /**
     * Find sessions with stale location data within a tenant.
     *
     * @param tenantId the tenant ID
     * @param staleMinutes the threshold in minutes
     * @return list of sessions with stale data
     */
    List<DriverTrackingSession> findWithStaleLocationByTenantId(String tenantId, int staleMinutes);
}
