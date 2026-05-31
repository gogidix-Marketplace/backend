package com.gogidix.shared.warehousing.inventory_analytics.domain.repository;

import com.gogidix.shared.warehousing.inventory_analytics.domain.entity.TurnoverReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurnoverReportRepository extends MongoRepository<TurnoverReport, String> {

    List<TurnoverReport> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    List<TurnoverReport> findByTenantIdAndWarehouseIdAndReportPeriod(
            String tenantId, String warehouseId, TurnoverReport.ReportPeriod period);

    List<TurnoverReport> findFirstByTenantIdAndWarehouseIdOrderByReportDateDesc(
            String tenantId, String warehouseId);

    List<TurnoverReport> findByTenantId(String tenantId);
}
