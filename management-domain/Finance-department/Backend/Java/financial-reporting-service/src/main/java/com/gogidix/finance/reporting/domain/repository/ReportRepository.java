package com.gogidix.finance.reporting.domain.repository;

import com.gogidix.finance.reporting.domain.model.Report;

import java.time.LocalDate;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Report Repository Interface (Port)
 */
public interface ReportRepository {

    Report save(Report report);

    List<Report> saveAll(List<Report> reports);

    Optional<Report> findById(String id);

    Optional<Report> findByReportIdAndTenantId(String reportId, String tenantId);

    List<Report> findByTenantId(String tenantId);

    List<Report> findByTenantIdAndStatus(String tenantId, Report.ReportStatus status);

    List<Report> findByTenantIdAndReportType(String tenantId, Report.ReportType reportType);

    List<Report> findByTenantIdAndGeneratedBy(String tenantId, String generatedBy);

    List<Report> findByTenantIdAndReportDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Report> findByTenantIdAndCreatedAtBetween(String tenantId, Instant startDate, Instant endDate);

    List<Report> findScheduledReportsDueBefore(Instant threshold);

    boolean existsByReportIdAndTenantId(String reportId, String tenantId);

    void deleteById(String id);

    void deleteByReportIdAndTenantId(String reportId, String tenantId);

    void deleteOlderThan(Instant cutoff);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Report.ReportStatus status);

    List<Report> findByTenantIdAndScheduleId(String tenantId, String scheduleId);
}
