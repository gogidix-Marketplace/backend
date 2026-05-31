package com.gogidix.universal.tracking.domain.port.out;

import com.gogidix.universal.tracking.domain.model.TrackingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Tracking Session repository operations.
 */
public interface TrackingSessionRepositoryPort {

    /**
     * Save a tracking session
     */
    TrackingSession save(TrackingSession session);

    /**
     * Find session by ID
     */
    Optional<TrackingSession> findById(UUID id);

    /**
     * Find session by session ID
     */
    Optional<TrackingSession> findBySessionId(String sessionId);

    /**
     * Find sessions by user ID
     */
    List<TrackingSession> findByUserId(String userId);

    /**
     * Find sessions by tenant ID with pagination
     */
    Page<TrackingSession> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find active sessions by tenant
     */
    List<TrackingSession> findActiveSessionsByTenant(String tenantId);

    /**
     * Find sessions that have timed out
     */
    List<TrackingSession> findTimedOutSessions(LocalDateTime timeoutThreshold);

    /**
     * Count active sessions by tenant
     */
    Long countActiveSessionsByTenant(String tenantId);

    /**
     * Delete by ID
     */
    void deleteById(UUID id);

    /**
     * Delete sessions older than specified date
     */
    void deleteByStartedAtBefore(LocalDateTime date);

    /**
     * Check if session exists by session ID
     */
    boolean existsBySessionId(String sessionId);

    /**
     * Count sessions by tenant and active status
     */
    Long countByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
