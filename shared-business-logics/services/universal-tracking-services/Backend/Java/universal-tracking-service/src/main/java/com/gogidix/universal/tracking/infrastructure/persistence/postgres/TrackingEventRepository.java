package com.gogidix.universal.tracking.infrastructure.persistence.postgres;

import com.gogidix.universal.tracking.domain.model.TrackingEvent;
import com.gogidix.universal.tracking.domain.port.out.TrackingEventRepositoryPort;
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
 * JPA Repository for TrackingEvent entities.
 */
@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent, UUID>, TrackingEventRepositoryPort {

    @Override
    TrackingEvent save(TrackingEvent event);

    @Override
    List<TrackingEvent> saveAll(List<TrackingEvent> events);

    @Override
    Optional<TrackingEvent> findById(UUID id);

    List<TrackingEvent> findBySessionId(String sessionId);

    @Override
    Page<TrackingEvent> findByTenantId(String tenantId, Pageable pageable);

    @Override
    @Query("SELECT e FROM TrackingEvent e WHERE " +
           "(:tenantId IS NULL OR e.tenantId = :tenantId) AND " +
           "(:eventType IS NULL OR e.eventType = :eventType) AND " +
           "(:sessionId IS NULL OR e.sessionId = :sessionId) AND " +
           "(:userId IS NULL OR e.userId = :userId) AND " +
           "(:source IS NULL OR e.source = :source) AND " +
           "(:startDate IS NULL OR e.timestamp >= :startDate) AND " +
           "(:endDate IS NULL OR e.timestamp <= :endDate) AND " +
           "(:processed IS NULL OR e.processed = :processed)")
    Page<TrackingEvent> findByFilters(
        @Param("tenantId") String tenantId,
        @Param("eventType") String eventType,
        @Param("sessionId") String sessionId,
        @Param("userId") String userId,
        @Param("source") String source,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("processed") Boolean processed,
        Pageable pageable
    );

    @Override
    @Query("SELECT e FROM TrackingEvent e WHERE e.processed = false ORDER BY e.timestamp ASC")
    List<TrackingEvent> findUnprocessedEvents(Pageable pageable);

    @Override
    Long countByEventTypeAndTenantId(String eventType, String tenantId);

    @Override
    Long countByTimestampBetweenAndTenantId(LocalDateTime start, LocalDateTime end, String tenantId);

    @Override
    void deleteById(UUID id);

    @Override
    @Query("DELETE FROM TrackingEvent e WHERE e.timestamp < :date")
    void deleteByTimestampBefore(@Param("date") LocalDateTime date);
}
