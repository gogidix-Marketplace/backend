package com.gogidix.globalbusinessmanagement.datavalidation.domain.repository;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.DataQualityReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DataQualityReport
 */
@Repository
public interface DataQualityReportRepository extends MongoRepository<DataQualityReport, String> {

    /**
     * Find a report by its unique report ID
     */
    Optional<DataQualityReport> findByReportId(String reportId);

    /**
     * Find reports by entity type
     */
    List<DataQualityReport> findByEntityTypeOrderByReportDateDesc(String entityType);

    /**
     * Find reports by entity type and entity ID
     */
    List<DataQualityReport> findByEntityTypeAndEntityIdOrderByReportDateDesc(
            String entityType, String entityId);

    /**
     * Find reports by tenant ID
     */
    Page<DataQualityReport> findByTenantIdOrderByReportDateDesc(String tenantId, Pageable pageable);

    /**
     * Find reports by report type
     */
    List<DataQualityReport> findByReportTypeOrderByReportDateDesc(DataQualityReport.ReportType reportType);

    /**
     * Find reports by data source
     */
    List<DataQualityReport> findByDataSourceOrderByReportDateDesc(String dataSource);

    /**
     * Find reports within a date range
     */
    List<DataQualityReport> findByReportDateBetweenOrderByReportDateDesc(
            LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find reports by period range
     */
    @Query("{ 'periodStart': { '$gte': ?0 }, 'periodEnd': { '$lte': ?1 } }")
    List<DataQualityReport> findByPeriodRange(LocalDateTime periodStart, LocalDateTime periodEnd);

    /**
     * Find reports by quality level
     */
    List<DataQualityReport> findByQualityLevelOrderByReportDateDesc(
            DataQualityReport.QualityLevel qualityLevel);

    /**
     * Find latest report for entity
     */
    Optional<DataQualityReport> findFirstByEntityTypeAndEntityIdOrderByReportDateDesc(
            String entityType, String entityId);

    /**
     * Find reports by batch ID
     */
    List<DataQualityReport> findByBatchId(String batchId);

    /**
     * Find archived reports
     */
    List<DataQualityReport> findByArchivedTrueOrderByReportDateDesc();

    /**
     * Find non-archived reports
     */
    Page<DataQualityReport> findByArchivedFalseOrderByReportDateDesc(Pageable pageable);

    /**
     * Find reports with quality score below threshold
     */
    @Query("{ 'qualityScore': { '$lt': ?0 } }")
    List<DataQualityReport> findByQualityScoreLessThan(double threshold);

    /**
     * Count reports by entity type
     */
    long countByEntityType(String entityType);

    /**
     * Delete reports older than a date
     */
    long deleteByReportDateBefore(LocalDateTime date);

    /**
     * Find reports for dashboard
     */
    @Query("{ 'archived': false, 'reportDate': { '$gte': ?0 } }")
    List<DataQualityReport> findRecentReportsForDashboard(LocalDateTime since);
}
