package com.gogidix.foundation.devtools.domain.repository;

import com.gogidix.foundation.devtools.domain.entity.DeploymentExecution;
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
 * Repository for DeploymentExecution entity.
 */
@Repository
public interface DeploymentExecutionRepository extends JpaRepository<DeploymentExecution, Long> {

    Optional<DeploymentExecution> findByUuid(UUID uuid);

    List<DeploymentExecution> findByJobId(Long jobId);

    Page<DeploymentExecution> findByJobId(Long jobId, Pageable pageable);

    List<DeploymentExecution> findByStatus(String status);

    @Query("SELECT e FROM DeploymentExecution e WHERE e.jobId = :jobId AND " +
           "e.executedAt BETWEEN :startDate AND :endDate ORDER BY e.executedAt DESC")
    List<DeploymentExecution> findByJobIdAndDateRange(
        @Param("jobId") Long jobId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(e) FROM DeploymentExecution e WHERE e.jobId = :jobId AND e.status = :status")
    Long countByJobIdAndStatus(@Param("jobId") Long jobId,
                                @Param("status") String status);

    List<DeploymentExecution> findTop10ByJobIdOrderByExecutedAtDesc(Long jobId);
}
