package com.gogidix.shared.warehousing.inventory.analytics.application.service;

import com.gogidix.shared.warehousing.inventory.analytics.application.dto.*;
import com.gogidix.shared.warehousing.inventory.analytics.application.mapper.InventoryAnalyticsMapper;
import com.gogidix.shared.warehousing.inventory.analytics.domain.entity.*;
import com.gogidix.shared.warehousing.inventory.analytics.domain.exception.EntityNotFoundException;
import com.gogidix.shared.warehousing.inventory.analytics.domain.repository.*;
import com.gogidix.shared.warehousing.inventory.analytics.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryAnalyticsService {

    private final InventoryTurnoverRepository turnoverRepository;
    private final StockoutReportRepository stockoutReportRepository;
    private final ForecastDataRepository forecastDataRepository;
    private final InventoryAnalyticsMapper mapper;

    public InventoryTurnoverDTO createTurnover(InventoryTurnover turnover) {
        String tenantId = TenantContext.getCurrentTenantId();
        turnover.setTenantId(tenantId);
        turnover.setId(UUID.randomUUID().toString());
        InventoryTurnover saved = turnoverRepository.save(turnover);
        return mapper.toTurnoverDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<InventoryTurnoverDTO> getTurnoverBySku(String sku) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<InventoryTurnover> turnoverList = turnoverRepository.findByTenantIdAndSku(tenantId, sku);
        return mapper.toTurnoverDTOList(turnoverList);
    }

    @Transactional(readOnly = true)
    public InventoryTurnoverDTO getLatestTurnover(String sku) {
        String tenantId = TenantContext.getCurrentTenantId();
        InventoryTurnover turnover = turnoverRepository
            .findFirstByTenantIdAndSkuOrderByPeriodEndDesc(tenantId, sku)
            .orElseThrow(() -> new EntityNotFoundException("No turnover data found for SKU: " + sku));
        return mapper.toTurnoverDTO(turnover);
    }

    public StockoutReportDTO createStockoutReport(StockoutReport report) {
        String tenantId = TenantContext.getCurrentTenantId();
        report.setTenantId(tenantId);
        report.setId(UUID.randomUUID().toString());
        StockoutReport saved = stockoutReportRepository.save(report);
        return mapper.toStockoutReportDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<StockoutReportDTO> getStockoutReports(String warehouseId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<StockoutReport> reports = stockoutReportRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId);
        return mapper.toStockoutReportDTOList(reports);
    }

    @Transactional(readOnly = true)
    public StockoutReportDTO getLatestStockoutReport(String warehouseId) {
        String tenantId = TenantContext.getCurrentTenantId();
        StockoutReport report = stockoutReportRepository
            .findFirstByTenantIdAndWarehouseIdOrderByReportDateDesc(tenantId, warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("No stockout report found for warehouse: " + warehouseId));
        return mapper.toStockoutReportDTO(report);
    }

    public ForecastDataDTO createForecast(ForecastData forecast) {
        String tenantId = TenantContext.getCurrentTenantId();
        forecast.setTenantId(tenantId);
        forecast.setId(UUID.randomUUID().toString());
        forecast.setForecastGenerated(LocalDateTime.now());
        ForecastData saved = forecastDataRepository.save(forecast);
        return mapper.toForecastDataDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ForecastDataDTO> getForecastsBySku(String sku) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<ForecastData> forecasts = forecastDataRepository.findByTenantIdAndSku(tenantId, sku);
        return mapper.toForecastDataDTOList(forecasts);
    }

    @Transactional(readOnly = true)
    public ForecastDataDTO getLatestForecast(String sku) {
        String tenantId = TenantContext.getCurrentTenantId();
        ForecastData forecast = forecastDataRepository
            .findFirstByTenantIdAndSkuOrderByForecastDateDesc(tenantId, sku)
            .orElseThrow(() -> new EntityNotFoundException("No forecast data found for SKU: " + sku));
        return mapper.toForecastDataDTO(forecast);
    }

    public ForecastDataDTO generateForecast(String sku, String warehouseId, Integer horizonDays) {
        String tenantId = TenantContext.getCurrentTenantId();

        ForecastData forecast = ForecastData.builder()
            .tenantId(tenantId)
            .sku(sku)
            .warehouseId(warehouseId)
            .forecastDate(LocalDateTime.now())
            .forecastGenerated(LocalDateTime.now())
            .forecastHorizonDays(horizonDays)
            .predictedDemand(0.0)
            .confidenceLevel(0.5)
            .accuracy(ForecastData.ForecastAccuracy.MEDIUM)
            .trendDirection(ForecastData.TrendDirection.STABLE)
            .trendStrength(0.0)
            .build();

        ForecastData saved = forecastDataRepository.save(forecast);
        return mapper.toForecastDataDTO(saved);
    }
}
