package com.gogidix.foundation.devtools.domain.repository;

import com.gogidix.foundation.devtools.domain.entity.DevToolLogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository for DevToolLogEntry entity.
 */
@Repository
public interface DevToolLogEntryRepository extends JpaRepository<DevToolLogEntry, Long> {

    List<DevToolLogEntry> findByLevel(String level);

    Page<DevToolLogEntry> findByLevel(String level, Pageable pageable);

    List<DevToolLogEntry> findBySource(String source);

    @Query("SELECT e FROM DevToolLogEntry e WHERE e.level = :level AND " +
           "e.createdAt BETWEEN :startDate AND :endDate ORDER BY e.createdAt DESC")
    List<DevToolLogEntry> findByLevelAndDateRange(
        @Param("level") String level,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM DevToolLogEntry e WHERE " +
           "e.createdAt BETWEEN :startDate AND :endDate ORDER BY e.createdAt DESC")
    List<DevToolLogEntry> findByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM DevToolLogEntry e WHERE e.sessionId = :sessionId ORDER BY e.createdAt DESC")
    List<DevToolLogEntry> findBySessionId(@Param("sessionId") String sessionId);

    @Query("SELECT e FROM DevToolLogEntry e WHERE e.requestId = :requestId ORDER BY e.createdAt DESC")
    List<DevToolLogEntry> findByRequestId(@Param("requestId") String requestId);

    @Query("SELECT COUNT(e) FROM DevToolLogEntry e WHERE e.level = :level")
    Long countByLevel(@Param("level") String level);

    @Query("SELECT e FROM DevToolLogEntry e WHERE " +
           "(LOWER(e.message) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.source) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "e.createdAt BETWEEN :startDate AND :endDate")
    Page<DevToolLogEntry> search(@Param("search") String search,
                                  @Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate,
                                  Pageable pageable);

    @Query("SELECT e FROM DevToolLogEntry e WHERE e.level IN :levels AND " +
           "e.createdAt BETWEEN :startDate AND :endDate ORDER BY e.createdAt DESC")
    List<DevToolLogEntry> findByLevelsAndDateRange(
        @Param("levels") List<String> levels,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    void deleteByCreatedAtBefore(LocalDateTime date);
}
