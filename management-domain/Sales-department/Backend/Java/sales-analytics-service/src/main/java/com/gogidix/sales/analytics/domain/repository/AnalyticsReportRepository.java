package com.gogidix.sales.analytics.domain.repository;

import com.gogidix.sales.analytics.domain.model.AnalyticsReport;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Analytics Report Repository Interface (Port)
 * Defines the contract for analytics report persistence operations
 */
public interface AnalyticsReportRepository {

    AnalyticsReport save(AnalyticsReport report);

    List<AnalyticsReport> saveAll(List<AnalyticsReport> reports);

    Optional<AnalyticsReport> findById(String id);

    Optional<AnalyticsReport> findByReportIdAndTenantId(String reportId, String tenantId);

    List<AnalyticsReport> findByTenantId(String tenantId);

    List<AnalyticsReport> findByTenantIdAndStatus(String tenantId, AnalyticsReport.ReportStatus status);

    List<AnalyticsReport> findByTenantIdAndReportType(String tenantId, AnalyticsReport.ReportType reportType);

    List<AnalyticsReport> findByTenantIdAndGeneratedBy(String tenantId, String generatedBy);

    List<AnalyticsReport> findByTenantIdAndDateRange(String tenantId, Instant startDate, Instant endDate);

    List<AnalyticsReport> findByTenantIdAndScheduleId(String tenantId, String scheduleId);

    List<AnalyticsReport> findExpiredReports(String tenantId);

    List<AnalyticsReport> findByTenantIdAndTagsContaining(String tenantId, String tag);

    boolean existsByReportIdAndTenantId(String reportId, String tenantId);

    void deleteById(String id);

    void deleteByReportIdAndTenantId(String reportId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteExpiredReports(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, AnalyticsReport.ReportStatus status);
}
