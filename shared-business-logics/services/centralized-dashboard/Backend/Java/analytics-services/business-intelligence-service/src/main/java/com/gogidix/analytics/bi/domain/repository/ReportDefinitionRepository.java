package com.gogidix.analytics.bi.domain.repository;

import com.gogidix.analytics.bi.domain.model.ReportDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for ReportDefinition aggregate.
 */
@Repository
public interface ReportDefinitionRepository extends JpaRepository<ReportDefinition, String> {

    List<ReportDefinition> findByTenantId(String tenantId);

    List<ReportDefinition> findByTenantIdAndOwnerId(String tenantId, String ownerId);

    List<ReportDefinition> findByTenantIdAndEnabledTrue(String tenantId);

    List<ReportDefinition> findByTenantIdAndScheduleType(String tenantId, ReportDefinition.ScheduleType scheduleType);

    @Query("SELECT r FROM ReportDefinition r WHERE r.tenantId = :tenantId " +
           "AND r.enabled = true AND r.scheduleType IS NOT NULL " +
           "AND (r.nextRunAt IS NULL OR r.nextRunAt <= :now)")
    List<ReportDefinition> findScheduledReportsReadyToRun(@Param("tenantId") String tenantId, @Param("now") LocalDateTime now);

    @Query("SELECT r FROM ReportDefinition r WHERE r.tenantId = :tenantId AND " +
           "(LOWER(r.reportName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<ReportDefinition> searchByTenantId(@Param("tenantId") String tenantId, @Param("search") String search);
}
