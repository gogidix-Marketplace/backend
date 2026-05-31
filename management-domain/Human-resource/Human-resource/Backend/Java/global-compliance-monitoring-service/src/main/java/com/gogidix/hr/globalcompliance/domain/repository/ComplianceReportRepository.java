package com.gogidix.hr.globalcompliance.domain.repository;

import com.gogidix.hr.globalcompliance.domain.model.ComplianceReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Compliance Report Repository Interface (Port)
 * Defines the contract for compliance report persistence operations
 */
public interface ComplianceReportRepository {

    ComplianceReport save(ComplianceReport report);

    List<ComplianceReport> saveAll(List<ComplianceReport> reports);

    Optional<ComplianceReport> findById(String id);

    Optional<ComplianceReport> findByReportIdAndTenantId(String reportId, String tenantId);

    Optional<ComplianceReport> findByReportNumberAndTenantId(String reportNumber, String tenantId);

    List<ComplianceReport> findByTenantId(String tenantId);

    List<ComplianceReport> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<ComplianceReport> findByTenantIdAndReportType(String tenantId, String reportType);

    List<ComplianceReport> findByTenantIdAndStatus(String tenantId, String status);

    List<ComplianceReport> findByTenantIdAndPreparedBy(String tenantId, String preparedBy);

    List<ComplianceReport> findByTenantIdAndApprovedBy(String tenantId, String approvedBy);

    List<ComplianceReport> findByTenantIdAndPeriodBetween(String tenantId, LocalDate periodStart, LocalDate periodEnd);

    List<ComplianceReport> findByTenantIdAndPeriodStartBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<ComplianceReport> findByTenantIdAndDepartment(String tenantId, String department);

    List<ComplianceReport> findDraftReports(String tenantId);

    List<ComplianceReport> findSubmittedReports(String tenantId);

    List<ComplianceReport> findApprovedReports(String tenantId);

    List<ComplianceReport> findPublishedReports(String tenantId);

    List<ComplianceReport> findByTenantIdAndRegion(String tenantId, String region);

    List<ComplianceReport> findReportsByComplianceScoreRange(String tenantId, Double minScore, Double maxScore);

    List<ComplianceReport> findReportsWithCriticalIssues(String tenantId);

    List<ComplianceReport> findRecentReports(String tenantId, int limit);

    boolean existsByReportNumberAndTenantId(String reportNumber, String tenantId);

    boolean existsByReportIdAndTenantId(String reportId, String tenantId);

    void deleteById(String id);

    void deleteByReportIdAndTenantId(String reportId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, String status);

    long countByTenantIdAndReportType(String tenantId, String reportType);

    Double getAverageComplianceScore(String tenantId);

    List<ComplianceReport> searchBySummary(String tenantId, String searchTerm);
}
