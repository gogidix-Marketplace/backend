package com.gogidix.universal.tracking.infrastructure.persistence.postgres;

import com.gogidix.universal.tracking.domain.model.TrackingSession;
import com.gogidix.universal.tracking.domain.port.out.TrackingSessionRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA Repository for TrackingSession entities.
 */
@Repository
public interface TrackingSessionRepository extends JpaRepository<TrackingSession, UUID>, TrackingSessionRepositoryPort {

    @Override
    TrackingSession save(TrackingSession session);

    @Override
    Optional<TrackingSession> findById(UUID id);

    Optional<TrackingSession> findBySessionId(String sessionId);

    List<TrackingSession> findByUserId(String userId);

    @Override
    Page<TrackingSession> findByTenantId(String tenantId, Pageable pageable);

    @Override
    @Query("SELECT s FROM TrackingSession s WHERE s.tenantId = :tenantId AND s.isActive = true")
    List<TrackingSession> findActiveSessionsByTenant(@Param("tenantId") String tenantId);

    @Override
    @Query("SELECT s FROM TrackingSession s WHERE s.isActive = true AND s.lastActivityAt < :timeoutThreshold")
    List<TrackingSession> findTimedOutSessions(@Param("timeoutThreshold") LocalDateTime timeoutThreshold);

    @Override
    @Query("SELECT COUNT(s) FROM TrackingSession s WHERE s.tenantId = :tenantId AND s.isActive = true")
    Long countActiveSessionsByTenant(@Param("tenantId") String tenantId);

    @Override
    void deleteById(UUID id);

    @Override
    @Query("DELETE FROM TrackingSession s WHERE s.startedAt < :date")
    void deleteByStartedAtBefore(@Param("date") LocalDateTime date);

    @Override
    boolean existsBySessionId(String sessionId);

    @Override
    Long countByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
