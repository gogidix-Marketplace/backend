package com.gogidix.shared.warehousing.inventory_analytics.domain.repository;

import com.gogidix.shared.warehousing.inventory_analytics.domain.entity.StockoutReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockoutReportRepository extends MongoRepository<StockoutReport, String> {

    List<StockoutReport> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    List<StockoutReport> findByTenantIdAndWarehouseIdAndReportPeriod(
            String tenantId, String warehouseId, StockoutReport.ReportPeriod period);

    List<StockoutReport> findFirstByTenantIdAndWarehouseIdOrderByReportDateDesc(
            String tenantId, String warehouseId);

    List<StockoutReport> findByTenantId(String tenantId);
}
