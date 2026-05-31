package com.gogidix.shared.warehousing.inventory.analytics.domain.repository;

import com.gogidix.shared.warehousing.inventory.analytics.domain.entity.StockoutReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockoutReportRepository extends MongoRepository<StockoutReport, String> {

    List<StockoutReport> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    List<StockoutReport> findByTenantIdAndWarehouseIdAndReportDateBetween(
        String tenantId, String warehouseId, LocalDateTime startDate, LocalDateTime endDate);

    Optional<StockoutReport> findFirstByTenantIdAndWarehouseIdOrderByReportDateDesc(
        String tenantId, String warehouseId);

    List<StockoutReport> findByTenantIdAndTrend(String tenantId, StockoutReport.StockoutTrend trend);

    List<StockoutReport> findByTenantId(String tenantId);

    List<StockoutReport> findByTenantIdOrderByTotalRevenueImpactDesc(String tenantId);
}
