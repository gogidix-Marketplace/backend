package com.gogidix.shared.warehousing.inventory.analytics.domain.repository;

import com.gogidix.shared.warehousing.inventory.analytics.domain.entity.ForecastData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ForecastDataRepository extends MongoRepository<ForecastData, String> {

    List<ForecastData> findByTenantIdAndSku(String tenantId, String sku);

    List<ForecastData> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    List<ForecastData> findByTenantIdAndSkuAndForecastDateAfter(
        String tenantId, String sku, LocalDateTime date);

    Optional<ForecastData> findFirstByTenantIdAndSkuOrderByForecastDateDesc(String tenantId, String sku);

    List<ForecastData> findByTenantIdAndTrendDirection(String tenantId, ForecastData.TrendDirection trend);

    List<ForecastData> findByTenantIdAndAccuracy(String tenantId, ForecastData.ForecastAccuracy accuracy);

    List<ForecastData> findByTenantId(String tenantId);
}
