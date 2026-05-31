package com.gogidix.shared.warehousing.inventory.analytics.application.mapper;

import com.gogidix.shared.warehousing.inventory.analytics.application.dto.*;
import com.gogidix.shared.warehousing.inventory.analytics.domain.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryAnalyticsMapper {

    public InventoryTurnoverDTO toTurnoverDTO(InventoryTurnover entity) {
        if (entity == null) return null;
        return InventoryTurnoverDTO.builder()
                .productId(entity.getSku())
                .productName(entity.getWarehouseName())
                .tenantId(entity.getTenantId())
                .turnoverRatio(entity.getTurnoverRate() != null ? java.math.BigDecimal.valueOf(entity.getTurnoverRate()) : null)
                .daysToSell(entity.getDaysInInventory() != null ? entity.getDaysInInventory().intValue() : null)
                .averageInventoryValue(entity.getAverageInventoryValue() != null ? java.math.BigDecimal.valueOf(entity.getAverageInventoryValue()) : null)
                .costOfGoodsSold(entity.getCostOfGoodsSold() != null ? java.math.BigDecimal.valueOf(entity.getCostOfGoodsSold()) : null)
                .periodStart(entity.getPeriodStart())
                .periodEnd(entity.getPeriodEnd())
                .category(entity.getCategory() != null ? entity.getCategory().name() : null)
                .build();
    }

    public List<InventoryTurnoverDTO> toTurnoverDTOList(List<InventoryTurnover> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toTurnoverDTO).collect(Collectors.toList());
    }

    public StockoutReportDTO toStockoutReportDTO(StockoutReport entity) {
        if (entity == null) return null;
        List<StockoutReportDTO.StockoutItem> items = null;
        if (entity.getStockoutItems() != null) {
            items = entity.getStockoutItems().stream()
                    .map(this::toStockoutItemDTO)
                    .collect(Collectors.toList());
        }
        return StockoutReportDTO.builder()
                .reportId(entity.getId())
                .tenantId(entity.getTenantId())
                .reportDate(entity.getReportDate())
                .warehouseId(entity.getWarehouseId())
                .stockoutItems(items)
                .totalStockouts(entity.getTotalStockouts())
                .criticalItems(entity.getUniqueSkusAffected())
                .build();
    }

    private StockoutReportDTO.StockoutItem toStockoutItemDTO(StockoutReport.StockoutItem item) {
        if (item == null) return null;
        return StockoutReportDTO.StockoutItem.builder()
                .productId(item.getSku())
                .productName(item.getSkuName())
                .sku(item.getSku())
                .quantity(item.getStockoutCount())
                .stockoutDate(item.getLastStockoutDate())
                .category(item.getImpactLevel() != null ? item.getImpactLevel().name() : null)
                .isCritical(item.getImpactLevel() == StockoutReport.ImpactLevel.CRITICAL)
                .build();
    }

    public List<StockoutReportDTO> toStockoutReportDTOList(List<StockoutReport> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toStockoutReportDTO).collect(Collectors.toList());
    }

    public ForecastDataDTO toForecastDataDTO(ForecastData entity) {
        if (entity == null) return null;
        List<ForecastDataDTO.ForecastPoint> historicalData = null;
        if (entity.getForecastPeriods() != null) {
            historicalData = entity.getForecastPeriods().stream()
                    .map(this::toForecastPoint)
                    .collect(Collectors.toList());
        }
        return ForecastDataDTO.builder()
                .forecastId(entity.getId())
                .productId(entity.getSku())
                .productName(entity.getSkuName())
                .tenantId(entity.getTenantId())
                .forecastDate(entity.getForecastDate() != null ? entity.getForecastDate().toLocalDate() : null)
                .predictedDemand(entity.getPredictedDemand() != null ? java.math.BigDecimal.valueOf(entity.getPredictedDemand()) : null)
                .confidenceLevel(entity.getConfidenceLevel() != null ? java.math.BigDecimal.valueOf(entity.getConfidenceLevel()) : null)
                .trend(entity.getTrendDirection() != null ? entity.getTrendDirection().name() : null)
                .historicalData(historicalData)
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private ForecastDataDTO.ForecastPoint toForecastPoint(ForecastData.ForecastPeriod period) {
        if (period == null) return null;
        return ForecastDataDTO.ForecastPoint.builder()
                .date(period.getPeriodDate() != null ? period.getPeriodDate().toLocalDate() : null)
                .value(period.getPredictedDemand() != null ? java.math.BigDecimal.valueOf(period.getPredictedDemand()) : null)
                .build();
    }

    public List<ForecastDataDTO> toForecastDataDTOList(List<ForecastData> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toForecastDataDTO).collect(Collectors.toList());
    }
}
