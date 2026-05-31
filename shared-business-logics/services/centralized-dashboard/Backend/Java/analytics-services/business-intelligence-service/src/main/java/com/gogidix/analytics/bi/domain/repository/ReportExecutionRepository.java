package com.gogidix.analytics.bi.domain.repository;

import com.gogidix.analytics.bi.domain.model.ReportExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for ReportExecution entity.
 */
@Repository
public interface ReportExecutionRepository extends JpaRepository<ReportExecution, String> {

    List<ReportExecution> findByReportDefinitionIdOrderByStartedAtDesc(String reportDefinitionId);

    List<ReportExecution> findByReportDefinitionIdAndStatus(String reportDefinitionId, ReportExecution.ExecutionStatus status);

    @Query("SELECT e FROM ReportExecution e WHERE e.reportDefinition.id = :reportId " +
           "AND e.status = :status ORDER BY e.createdAt ASC")
    List<ReportExecution> findByReportIdAndStatusOrderByCreatedAtAsc(@Param("reportId") String reportId, @Param("status") ReportExecution.ExecutionStatus status);

    @Query("SELECT e FROM ReportExecution e WHERE e.reportDefinition.tenantId = :tenantId " +
           "AND e.status IN ('PENDING', 'RUNNING') AND e.startedAt < :cutoffTime")
    List<ReportExecution> findStuckExecutions(@Param("tenantId") String tenantId, @Param("cutoffTime") LocalDateTime cutoffTime);

    void deleteByCompletedAtBefore(LocalDateTime cutoffDate);
}
