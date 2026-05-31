package com.gogidix.finance.cashflow.interfaces.rest;

import com.gogidix.finance.cashflow.application.dto.response.CashflowForecastResponseDto;
import com.gogidix.finance.cashflow.application.service.CashflowForecastService;
import com.gogidix.finance.cashflow.application.service.CashflowQueryService;
import com.gogidix.finance.cashflow.domain.model.CashflowForecast;
import com.gogidix.finance.cashflow.domain.port.in.CashflowForecastCommand;
import com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Cashflow Forecast REST Controller
 * Handles HTTP requests for cashflow forecast operations
 */
@RestController
@RequestMapping("/cashflow-forecasts")
@RequiredArgsConstructor
@Tag(name = "Cashflow Forecasts", description = "Cashflow forecast management endpoints")
public class CashflowForecastController {

    private final CashflowForecastService cashflowForecastService;
    private final CashflowQueryService cashflowQueryService;

    @PostMapping
    @Operation(summary = "Create a new cashflow forecast")
    public ResponseEntity<CashflowForecastResponseDto> createForecast(
            @Valid @RequestBody CreateForecastRequestDto request) {
        CashflowForecastCommand.CreateForecastCommand command = new CashflowForecastCommand.CreateForecastCommand(
                RequestContextHolder.getTenantId(),
                request.getName(),
                request.getDescription(),
                request.getStartDate(),
                request.getEndDate(),
                request.getPeriod(),
                request.getScenario(),
                RequestContextHolder.getUserIdOrDefault(null),
                request.getOpeningBalance(),
                request.getConfidenceLevel(),
                request.getTags(),
                request.getNotes(),
                request.getIsBaseline(),
                request.getParentForecastId()
        );

        CashflowForecast forecast = cashflowForecastService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(forecast));
    }

    @GetMapping("/{forecastId}")
    @Operation(summary = "Get forecast by ID")
    public ResponseEntity<CashflowForecastResponseDto> getForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {
        CashflowForecast forecast = cashflowQueryService.getForecastById(forecastId);
        return ResponseEntity.ok(toDto(forecast));
    }

    @GetMapping
    @Operation(summary = "Get all forecasts for tenant")
    public ResponseEntity<List<CashflowForecastResponseDto>> getAllForecasts() {
        List<CashflowForecast> forecasts = cashflowQueryService.getAllForecastsForTenant();
        return ResponseEntity.ok(forecasts.stream().map(this::toDto).toList());
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get forecasts by date range")
    public ResponseEntity<Page<CashflowForecastResponseDto>> getForecastsByDateRange(
            @Parameter(description = "Start Date") @RequestParam LocalDate startDate,
            @Parameter(description = "End Date") @RequestParam LocalDate endDate,
            @RequestParam(required = false) CashflowForecast.ForecastScenario scenario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<CashflowForecast> forecasts = cashflowQueryService.getForecastsByDateRange(
                startDate, endDate, scenario, page, size);

        return ResponseEntity.ok(forecasts.map(this::toDto));
    }

    @GetMapping("/scenario/{scenario}")
    @Operation(summary = "Get forecasts by scenario")
    public ResponseEntity<Page<CashflowForecastResponseDto>> getForecastsByScenario(
            @Parameter(description = "Forecast Scenario") @PathVariable CashflowForecast.ForecastScenario scenario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<CashflowForecast> forecasts = cashflowQueryService.getForecastsByScenario(
                scenario, page, size);

        return ResponseEntity.ok(forecasts.map(this::toDto));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get forecasts by status")
    public ResponseEntity<Page<CashflowForecastResponseDto>> getForecastsByStatus(
            @Parameter(description = "Forecast Status") @PathVariable CashflowForecast.ForecastStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<CashflowForecast> forecasts = cashflowQueryService.getForecastsByStatus(
                status, page, size);

        return ResponseEntity.ok(forecasts.map(this::toDto));
    }

    @PutMapping("/{forecastId}")
    @Operation(summary = "Update forecast")
    public ResponseEntity<CashflowForecastResponseDto> updateForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @Valid @RequestBody UpdateForecastRequestDto request) {

        CashflowForecastCommand.UpdateForecastCommand command = new CashflowForecastCommand.UpdateForecastCommand(
                RequestContextHolder.getTenantId(),
                forecastId,
                request.getName(),
                request.getDescription(),
                request.getOpeningBalance(),
                request.getConfidenceLevel(),
                request.getTags(),
                request.getNotes()
        );

        CashflowForecast forecast = cashflowForecastService.update(command);
        return ResponseEntity.ok(toDto(forecast));
    }

    @PostMapping("/{forecastId}/generate")
    @Operation(summary = "Generate forecast calculations")
    public ResponseEntity<Void> generateForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @RequestBody GenerateForecastRequestDto request) {

        CashflowForecastCommand.GenerateForecastCommand command = new CashflowForecastCommand.GenerateForecastCommand(
                RequestContextHolder.getTenantId(),
                forecastId,
                request.getStartDate(),
                request.getEndDate(),
                request.getItemCategories(),
                request.getCostCenters(),
                request.getProjects()
        );

        cashflowForecastService.generate(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{forecastId}/approve")
    @Operation(summary = "Approve forecast")
    public ResponseEntity<Void> approveForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        CashflowForecastCommand.ApproveForecastCommand command = new CashflowForecastCommand.ApproveForecastCommand(
                RequestContextHolder.getTenantId(),
                forecastId,
                RequestContextHolder.getUserIdOrDefault(null)
        );

        cashflowForecastService.approve(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{forecastId}/reject")
    @Operation(summary = "Reject forecast")
    public ResponseEntity<Void> rejectForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @RequestBody RejectRequestDto request) {

        CashflowForecastCommand.RejectForecastCommand command = new CashflowForecastCommand.RejectForecastCommand(
                RequestContextHolder.getTenantId(),
                forecastId,
                RequestContextHolder.getUserIdOrDefault(null),
                request.getReason()
        );

        cashflowForecastService.reject(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{forecastId}/archive")
    @Operation(summary = "Archive forecast")
    public ResponseEntity<Void> archiveForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        CashflowForecastCommand.ArchiveForecastCommand command = new CashflowForecastCommand.ArchiveForecastCommand(
                RequestContextHolder.getTenantId(), forecastId);

        cashflowForecastService.archive(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{forecastId}/new-version")
    @Operation(summary = "Create new version of forecast")
    public ResponseEntity<CashflowForecastResponseDto> createNewVersion(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        CashflowForecastCommand.CreateNewVersionCommand command = new CashflowForecastCommand.CreateNewVersionCommand(
                RequestContextHolder.getTenantId(),
                forecastId,
                RequestContextHolder.getUserIdOrDefault(null)
        );

        CashflowForecast forecast = cashflowForecastService.createNewVersion(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(forecast));
    }

    @PostMapping("/{forecastId}/variance")
    @Operation(summary = "Calculate variance for forecast")
    public ResponseEntity<Void> calculateVariance(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @RequestBody VarianceRequestDto request) {

        CashflowForecastCommand.CalculateVarianceCommand command = new CashflowForecastCommand.CalculateVarianceCommand(
                RequestContextHolder.getTenantId(),
                forecastId,
                request.getCategory(),
                request.getForecastedAmount(),
                request.getActualAmount()
        );

        cashflowForecastService.calculateVariance(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{forecastId}/confidence")
    @Operation(summary = "Set confidence level for forecast")
    public ResponseEntity<Void> setConfidence(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @RequestBody ConfidenceRequestDto request) {

        CashflowForecastCommand.SetConfidenceCommand command = new CashflowForecastCommand.SetConfidenceCommand(
                RequestContextHolder.getTenantId(),
                forecastId,
                request.getConfidenceLevel(),
                request.getVariancePercentage()
        );

        cashflowForecastService.setConfidence(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{forecastId}")
    @Operation(summary = "Delete forecast")
    public ResponseEntity<Void> deleteForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        CashflowForecastCommand.DeleteForecastCommand command = new CashflowForecastCommand.DeleteForecastCommand(
                RequestContextHolder.getTenantId(), forecastId);

        cashflowForecastService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    @Operation(summary = "Get cashflow summary")
    public ResponseEntity<CashflowQueryService.CashflowSummary> getCashflowSummary(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String costCenter,
            @RequestParam(required = false) String project) {

        CashflowQueryService.CashflowSummary summary = cashflowQueryService.getCashflowSummary(
                startDate, endDate, costCenter, project);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/position")
    @Operation(summary = "Get cashflow position")
    public ResponseEntity<CashflowQueryService.CashflowPosition> getCashflowPosition(
            @RequestParam(required = false) LocalDate asOfDate) {

        CashflowQueryService.CashflowPosition position = cashflowQueryService.getCashflowPosition(asOfDate);

        return ResponseEntity.ok(position);
    }

    private CashflowForecastResponseDto toDto(CashflowForecast forecast) {
        List<CashflowForecastResponseDto.ForecastPeriodDataDto> periodDataDtos = null;
        if (forecast.getPeriodData() != null) {
            periodDataDtos = forecast.getPeriodData().stream()
                    .map(pd -> CashflowForecastResponseDto.ForecastPeriodDataDto.builder()
                            .periodStart(pd.getPeriodStart())
                            .periodEnd(pd.getPeriodEnd())
                            .openingBalance(pd.getOpeningBalance())
                            .inflow(pd.getInflow())
                            .outflow(pd.getOutflow())
                            .netCashflow(pd.getNetCashflow())
                            .closingBalance(pd.getClosingBalance())
                            .transactionCount(pd.getTransactionCount())
                            .build())
                    .toList();
        }

        List<CashflowForecastResponseDto.ForecastVarianceDto> varianceDtos = null;
        if (forecast.getVariances() != null) {
            varianceDtos = forecast.getVariances().stream()
                    .map(v -> CashflowForecastResponseDto.ForecastVarianceDto.builder()
                            .category(v.getCategory())
                            .forecastedAmount(v.getForecastedAmount())
                            .actualAmount(v.getActualAmount())
                            .variance(v.getVariance())
                            .variancePercentage(v.getVariancePercentage())
                            .periodDate(v.getPeriodDate())
                            .build())
                    .toList();
        }

        return CashflowForecastResponseDto.builder()
                .id(forecast.getId())
                .forecastId(forecast.getForecastId())
                .tenantId(forecast.getTenantId())
                .name(forecast.getName())
                .description(forecast.getDescription())
                .startDate(forecast.getStartDate())
                .endDate(forecast.getEndDate())
                .period(mapPeriod(forecast.getPeriod()))
                .scenario(mapScenario(forecast.getScenario()))
                .status(mapStatus(forecast.getStatus()))
                .generatedBy(forecast.getGeneratedBy())
                .generatedAt(forecast.getGeneratedAt())
                .lastUpdated(forecast.getLastUpdated())
                .totalInflow(forecast.getTotalInflow())
                .totalOutflow(forecast.getTotalOutflow())
                .netCashflow(forecast.getNetCashflow())
                .openingBalance(forecast.getOpeningBalance())
                .closingBalance(forecast.getClosingBalance())
                .minimumBalance(forecast.getMinimumBalance())
                .maximumBalance(forecast.getMaximumBalance())
                .minimumBalanceDate(forecast.getMinimumBalanceDate())
                .maximumBalanceDate(forecast.getMaximumBalanceDate())
                .version(forecast.getVersion())
                .parentForecastId(forecast.getParentForecastId())
                .isBaseline(forecast.getIsBaseline())
                .periodData(periodDataDtos)
                .variances(varianceDtos)
                .tags(forecast.getTags())
                .notes(forecast.getNotes())
                .confidenceLevel(mapConfidenceLevel(forecast.getConfidenceLevel()))
                .variancePercentage(forecast.getVariancePercentage())
                .createdAt(forecast.getCreatedAt())
                .updatedAt(forecast.getUpdatedAt())
                .build();
    }

    private CashflowForecastResponseDto.ForecastPeriodDto mapPeriod(CashflowForecast.ForecastPeriod period) {
        return period != null ? CashflowForecastResponseDto.ForecastPeriodDto.valueOf(period.name()) : null;
    }

    private CashflowForecastResponseDto.ForecastScenarioDto mapScenario(CashflowForecast.ForecastScenario scenario) {
        return scenario != null ? CashflowForecastResponseDto.ForecastScenarioDto.valueOf(scenario.name()) : null;
    }

    private CashflowForecastResponseDto.ForecastStatusDto mapStatus(CashflowForecast.ForecastStatus status) {
        return status != null ? CashflowForecastResponseDto.ForecastStatusDto.valueOf(status.name()) : null;
    }

    private CashflowForecastResponseDto.ConfidenceLevelDto mapConfidenceLevel(CashflowForecast.ConfidenceLevel level) {
        return level != null ? CashflowForecastResponseDto.ConfidenceLevelDto.valueOf(level.name()) : null;
    }

    // Request DTOs
    @Data
    public static class CreateForecastRequestDto {
        public String name;
        public String description;
        public LocalDate startDate;
        public LocalDate endDate;
        public CashflowForecast.ForecastPeriod period;
        public CashflowForecast.ForecastScenario scenario;
        public BigDecimal openingBalance;
        public CashflowForecast.ConfidenceLevel confidenceLevel;
        public List<String> tags;
        public String notes;
        public Boolean isBaseline;
        public String parentForecastId;
    }

    @Data
    public static class UpdateForecastRequestDto {
        public String name;
        public String description;
        public BigDecimal openingBalance;
        public CashflowForecast.ConfidenceLevel confidenceLevel;
        public List<String> tags;
        public String notes;
    }

    @Data
    public static class GenerateForecastRequestDto {
        public LocalDate startDate;
        public LocalDate endDate;
        public List<String> itemCategories;
        public List<String> costCenters;
        public List<String> projects;
    }

    @Data
    public static class RejectRequestDto {
        public String reason;
    }

    @Data
    public static class VarianceRequestDto {
        public String category;
        public BigDecimal forecastedAmount;
        public BigDecimal actualAmount;
    }

    @Data
    public static class ConfidenceRequestDto {
        public CashflowForecast.ConfidenceLevel confidenceLevel;
        public BigDecimal variancePercentage;
    }
}
