package com.gogidix.shared.warehousing.expiration.domain.repository;

import com.gogidix.shared.warehousing.expiration.domain.entity.ExpiryReport;
import com.gogidix.shared.warehousing.expiration.domain.entity.ExpiryReport.ReportType;
import com.gogidix.shared.warehousing.expiration.domain.entity.ExpiryReport.ReportStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Expiry Report Repository with Multi-Tenant Support
 */
@Repository
public interface ExpiryReportRepository extends MongoRepository<ExpiryReport, String> {

    /**
     * Find reports by tenant and type
     */
    List<ExpiryReport> findByTenantIdAndReportType(String tenantId, ReportType reportType);

    /**
     * Find reports by tenant and status
     */
    List<ExpiryReport> findByTenantIdAndStatus(String tenantId, ReportStatus status);

    /**
     * Find reports by report date
     */
    List<ExpiryReport> findByTenantIdAndReportDate(String tenantId, LocalDate reportDate);

    /**
     * Find reports by date range
     */
    List<ExpiryReport> findByTenantIdAndReportDateBetween(
        String tenantId, LocalDate startDate, LocalDate endDate);

    /**
     * Find latest report by type
     */
    Optional<ExpiryReport> findFirstByTenantIdAndReportTypeOrderByReportDateDesc(
        String tenantId, ReportType reportType);

    /**
     * Find all reports for a tenant
     */
    List<ExpiryReport> findByTenantId(String tenantId);
}
