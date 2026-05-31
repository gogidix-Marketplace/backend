package com.gogidix.foundation.devtools.domain.repository;

import com.gogidix.foundation.devtools.domain.entity.DatabaseQueryExecution;
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
 * Repository for DatabaseQueryExecution entity.
 */
@Repository
public interface DatabaseQueryExecutionRepository extends JpaRepository<DatabaseQueryExecution, Long> {

    Optional<DatabaseQueryExecution> findByUuid(UUID uuid);

    List<DatabaseQueryExecution> findByQueryId(Long queryId);

    Page<DatabaseQueryExecution> findByQueryId(Long queryId, Pageable pageable);

    List<DatabaseQueryExecution> findByStatus(String status);

    @Query("SELECT e FROM DatabaseQueryExecution e WHERE e.queryId = :queryId AND " +
           "e.executedAt BETWEEN :startDate AND :endDate ORDER BY e.executedAt DESC")
    List<DatabaseQueryExecution> findByQueryIdAndDateRange(
        @Param("queryId") Long queryId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(e) FROM DatabaseQueryExecution e WHERE e.queryId = :queryId AND e.status = :status")
    Long countByQueryIdAndStatus(@Param("queryId") Long queryId,
                                  @Param("status") String status);

    List<DatabaseQueryExecution> findTop10ByQueryIdOrderByExecutedAtDesc(Long queryId);
}
