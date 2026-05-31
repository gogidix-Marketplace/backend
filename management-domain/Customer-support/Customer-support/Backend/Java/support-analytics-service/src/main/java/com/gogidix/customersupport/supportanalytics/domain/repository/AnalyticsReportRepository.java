package com.gogidix.customersupport.supportanalytics.domain.repository;

import com.gogidix.customersupport.supportanalytics.domain.model.AnalyticsReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnalyticsReportRepository extends MongoRepository<AnalyticsReport, String> {

    List<AnalyticsReport> findByTenantId(String tenantId);

    Optional<AnalyticsReport> findByTenantIdAndId(String tenantId, String id);

    List<AnalyticsReport> findByTenantIdAndReportType(String tenantId, AnalyticsReport.ReportType reportType);

    List<AnalyticsReport> findByTenantIdAndStartDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    @Query("{ 'tenantId': ?0, 'reportType': ?1, 'startDate': { $gte: ?2, $lte: ?3 } }")
    List<AnalyticsReport> findByTenantIdAndReportTypeAndDateRange(String tenantId,
                                                                   AnalyticsReport.ReportType reportType,
                                                                   LocalDate startDate,
                                                                   LocalDate endDate);

    List<AnalyticsReport> findByTenantIdOrderByGeneratedAtDesc(String tenantId);

    @Query(value = "{ 'tenantId': ?0 }", sort = "{ 'generatedAt': -1 }")
    Optional<AnalyticsReport> findLatestReportByTenantId(String tenantId);

    void deleteByTenantIdAndId(String tenantId, String id);

    boolean existsByTenantIdAndReportName(String tenantId, String reportName);
}
