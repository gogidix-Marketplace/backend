package com.gogidix.shared.warehousing.warehouse.analytics.domain.repository;

import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.UtilizationReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Utilization Report Repository with Multi-Tenant Support
 */
@Repository
public interface UtilizationReportRepository extends MongoRepository<UtilizationReport, String> {

    /**
     * Find reports by tenant and warehouse
     */
    List<UtilizationReport> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    /**
     * Find reports by tenant, warehouse and date range
     */
    List<UtilizationReport> findByTenantIdAndWarehouseIdAndReportDateBetween(
        String tenantId, String warehouseId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find latest report for a warehouse
     */
    Optional<UtilizationReport> findFirstByTenantIdAndWarehouseIdOrderByReportDateDesc(
        String tenantId, String warehouseId);

    /**
     * Find all reports for a tenant
     */
    List<UtilizationReport> findByTenantId(String tenantId);

    /**
     * Find reports by status
     */
    List<UtilizationReport> findByTenantIdAndStatus(String tenantId, UtilizationReport.ReportStatus status);

    /**
     * Find reports by period
     */
    List<UtilizationReport> findByTenantIdAndPeriodStartBetween(
        String tenantId, LocalDateTime startDate, LocalDateTime endDate);
}
