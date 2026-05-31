package com.gogidix.shared.warehousing.inventory.analytics.interfaces.rest;

import com.gogidix.shared.warehousing.inventory.analytics.application.dto.ForecastDataDTO;
import com.gogidix.shared.warehousing.inventory.analytics.application.dto.StockoutReportDTO;
import com.gogidix.shared.warehousing.inventory.analytics.application.dto.InventoryTurnoverDTO;
import com.gogidix.shared.warehousing.inventory.analytics.application.service.InventoryAnalyticsService;
import com.gogidix.shared.warehousing.inventory.analytics.domain.entity.ForecastData;
import com.gogidix.shared.warehousing.inventory.analytics.domain.entity.InventoryTurnover;
import com.gogidix.shared.warehousing.inventory.analytics.domain.entity.StockoutReport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Inventory Analytics", description = "Inventory turnover, stockout, and forecast analytics APIs")
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryAnalyticsController {

    private final InventoryAnalyticsService analyticsService;

    @Operation(summary = "Create turnover data", description = "Create inventory turnover data entry")
    @PostMapping("/turnover")
    public ResponseEntity<InventoryTurnoverDTO> createTurnover(@RequestBody InventoryTurnover turnover) {
        InventoryTurnoverDTO dto = analyticsService.createTurnover(turnover);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Get turnover by SKU", description = "Retrieve turnover data for a specific SKU")
    @GetMapping("/turnover/sku/{sku}")
    public ResponseEntity<List<InventoryTurnoverDTO>> getTurnoverBySku(
            @Parameter(description = "SKU") @PathVariable String sku) {
        List<InventoryTurnoverDTO> dtos = analyticsService.getTurnoverBySku(sku);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Get latest turnover", description = "Retrieve latest turnover data for a SKU")
    @GetMapping("/turnover/sku/{sku}/latest")
    public ResponseEntity<InventoryTurnoverDTO> getLatestTurnover(
            @Parameter(description = "SKU") @PathVariable String sku) {
        InventoryTurnoverDTO dto = analyticsService.getLatestTurnover(sku);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create stockout report", description = "Create stockout report entry")
    @PostMapping("/stockouts")
    public ResponseEntity<StockoutReportDTO> createStockoutReport(@RequestBody StockoutReport report) {
        StockoutReportDTO dto = analyticsService.createStockoutReport(report);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Get stockout reports", description = "Retrieve stockout reports for a warehouse")
    @GetMapping("/stockouts/warehouse/{warehouseId}")
    public ResponseEntity<List<StockoutReportDTO>> getStockoutReports(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        List<StockoutReportDTO> dtos = analyticsService.getStockoutReports(warehouseId);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Get latest stockout report", description = "Retrieve latest stockout report")
    @GetMapping("/stockouts/warehouse/{warehouseId}/latest")
    public ResponseEntity<StockoutReportDTO> getLatestStockoutReport(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        StockoutReportDTO dto = analyticsService.getLatestStockoutReport(warehouseId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create forecast", description = "Create demand forecast entry")
    @PostMapping("/forecast")
    public ResponseEntity<ForecastDataDTO> createForecast(@RequestBody ForecastData forecast) {
        ForecastDataDTO dto = analyticsService.createForecast(forecast);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Get forecasts by SKU", description = "Retrieve forecasts for a specific SKU")
    @GetMapping("/forecast/sku/{sku}")
    public ResponseEntity<List<ForecastDataDTO>> getForecastsBySku(
            @Parameter(description = "SKU") @PathVariable String sku) {
        List<ForecastDataDTO> dtos = analyticsService.getForecastsBySku(sku);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Generate forecast", description = "Generate demand forecast for an SKU")
    @PostMapping("/forecast/generate")
    public ResponseEntity<ForecastDataDTO> generateForecast(
            @Parameter(description = "SKU") @RequestParam String sku,
            @Parameter(description = "Warehouse ID") @RequestParam String warehouseId,
            @Parameter(description = "Forecast horizon in days") @RequestParam(defaultValue = "30") Integer horizonDays) {
        ForecastDataDTO dto = analyticsService.generateForecast(sku, warehouseId, horizonDays);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Get latest forecast", description = "Retrieve latest forecast for a SKU")
    @GetMapping("/forecast/sku/{sku}/latest")
    public ResponseEntity<ForecastDataDTO> getLatestForecast(
            @Parameter(description = "SKU") @PathVariable String sku) {
        ForecastDataDTO dto = analyticsService.getLatestForecast(sku);
        return ResponseEntity.ok(dto);
    }
}
