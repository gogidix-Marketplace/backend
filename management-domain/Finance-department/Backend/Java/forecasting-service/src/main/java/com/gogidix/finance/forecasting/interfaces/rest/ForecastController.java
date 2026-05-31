package com.gogidix.finance.forecasting.interfaces.rest;

import com.gogidix.finance.forecasting.application.dto.response.ErrorResponseDto;
import com.gogidix.finance.forecasting.application.dto.response.ForecastMetricsDto;
import com.gogidix.finance.forecasting.application.dto.response.ForecastResponseDto;
import com.gogidix.finance.forecasting.application.service.ForecastCommandService;
import com.gogidix.finance.forecasting.application.service.ForecastQueryService;
import com.gogidix.finance.forecasting.domain.model.Forecast;
import com.gogidix.finance.forecasting.domain.model.ForecastMetric;
import com.gogidix.finance.forecasting.domain.port.in.ForecastCommand;
import com.gogidix.finance.forecasting.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Forecast REST Controller
 * Handles HTTP requests for forecast operations
 */
@RestController
@RequestMapping("/forecasts")
@RequiredArgsConstructor
@Tag(name = "Forecasts", description = "Financial forecasting API endpoints")
public class ForecastController {

    private final ForecastCommandService forecastCommandService;
    private final ForecastQueryService forecastQueryService;

    /**
     * Create a new forecast
     *
     * @param request the create forecast request
     * @return the created forecast
     */
    @PostMapping
    @Operation(summary = "Create a new forecast", description = "Creates a new financial forecast with the provided details")
    public ResponseEntity<ForecastResponseDto> createForecast(
            @Valid @RequestBody CreateForecastRequestDto request) {

        ForecastCommand.CreateForecastCommand command = new ForecastCommand.CreateForecastCommand(
                RequestContextHolder.getTenantId(),
                RequestContextHolder.getUserId().orElse("system"),
                request.getName(),
                request.getDescription(),
                request.getForecastType(),
                request.getForecastHorizon(),
                request.getStartDate(),
                request.getEndDate(),
                request.getCurrency(),
                request.getDepartment(),
                request.getCategory(),
                request.getScenario(),
                request.getConfidenceLevel(),
                request.getDataSource(),
                request.getMetrics(),
                request.getNotes(),
                request.getInitialAmount()
        );

        Forecast forecast = forecastCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(forecast));
    }

    /**
     * Get forecast by ID
     *
     * @param forecastId the forecast ID
     * @return the forecast
     */
    @GetMapping("/{forecastId}")
    @Operation(summary = "Get forecast by ID", description = "Retrieves a specific forecast by its ID")
    public ResponseEntity<ForecastResponseDto> getForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        Forecast forecast = forecastQueryService.getById(forecastId);
        return ResponseEntity.ok(toDto(forecast));
    }

    /**
     * Get all forecasts for tenant
     *
     * @return list of forecasts
     */
    @GetMapping
    @Operation(summary = "Get all forecasts", description = "Retrieves all forecasts for the current tenant")
    public ResponseEntity<List<ForecastResponseDto>> getAllForecasts() {
        List<Forecast> forecasts = forecastQueryService.getAllForTenant();
        return ResponseEntity.ok(forecasts.stream()
                .map(ForecastResponseDto::fromEntity)
                .toList());
    }

    /**
     * Update forecast
     *
     * @param forecastId the forecast ID
     * @param request the update request
     * @return the updated forecast
     */
    @PutMapping("/{forecastId}")
    @Operation(summary = "Update forecast", description = "Updates an existing forecast")
    public ResponseEntity<ForecastResponseDto> updateForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @Valid @RequestBody UpdateForecastRequestDto request) {

        ForecastCommand.UpdateForecastCommand command = new ForecastCommand.UpdateForecastCommand(
                RequestContextHolder.getTenantId(),
                forecastId,
                request.getName(),
                request.getDescription(),
                request.getStartDate(),
                request.getEndDate(),
                request.getDepartment(),
                request.getCategory(),
                request.getScenario(),
                request.getConfidenceLevel(),
                request.getDataSource(),
                request.getNotes(),
                request.getMetrics()
        );

        Forecast forecast = forecastCommandService.update(command);
        return ResponseEntity.ok(toDto(forecast));
    }

    /**
     * Submit forecast for approval
     *
     * @param forecastId the forecast ID
     * @return 202 Accepted
     */
    @PostMapping("/{forecastId}/submit")
    @Operation(summary = "Submit forecast for approval", description = "Submits a draft forecast for approval")
    public ResponseEntity<Void> submitForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        forecastCommandService.submitForApproval(forecastId);
        return ResponseEntity.accepted().build();
    }

    /**
     * Approve forecast
     *
     * @param forecastId the forecast ID
     * @param request the approval request
     * @return 200 OK
     */
    @PostMapping("/{forecastId}/approve")
    @Operation(summary = "Approve forecast", description = "Approves a pending forecast")
    public ResponseEntity<Void> approveForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @RequestBody ApprovalRequestDto request) {

        forecastCommandService.approveForecast(forecastId, request.getComments());
        return ResponseEntity.ok().build();
    }

    /**
     * Reject forecast
     *
     * @param forecastId the forecast ID
     * @param request the rejection request
     * @return 200 OK
     */
    @PostMapping("/{forecastId}/reject")
    @Operation(summary = "Reject forecast", description = "Rejects a pending forecast with a reason")
    public ResponseEntity<Void> rejectForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @Valid @RequestBody RejectionRequestDto request) {

        forecastCommandService.rejectForecast(forecastId, request.getReason());
        return ResponseEntity.ok().build();
    }

    /**
     * Delete forecast
     *
     * @param forecastId the forecast ID
     * @return 204 No Content
     */
    @DeleteMapping("/{forecastId}")
    @Operation(summary = "Delete forecast", description = "Deletes a draft or rejected forecast")
    public ResponseEntity<Void> deleteForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        forecastCommandService.deleteForecast(forecastId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Regenerate forecast
     *
     * @param forecastId the forecast ID
     * @param request the regeneration request
     * @return the regenerated forecast
     */
    @PostMapping("/{forecastId}/regenerate")
    @Operation(summary = "Regenerate forecast", description = "Regenerates a forecast with new data")
    public ResponseEntity<ForecastResponseDto> regenerateForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @RequestBody RegenerateForecastRequestDto request) {

        ForecastCommand.RegenerateForecastCommand command = new ForecastCommand.RegenerateForecastCommand(
                RequestContextHolder.getTenantId(),
                forecastId,
                RequestContextHolder.getUserId().orElse("system"),
                request.getNewMetrics(),
                request.getNewTotalAmount(),
                request.getDataSource(),
                request.getNewConfidenceLevel(),
                request.getScenario()
        );

        Forecast forecast = forecastCommandService.regenerate(command);
        return ResponseEntity.ok(toDto(forecast));
    }

    /**
     * Archive forecast
     *
     * @param forecastId the forecast ID
     * @return 200 OK
     */
    @PostMapping("/{forecastId}/archive")
    @Operation(summary = "Archive forecast", description = "Archives an approved or rejected forecast")
    public ResponseEntity<Void> archiveForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        ForecastCommand.ArchiveForecastCommand command =
                new ForecastCommand.ArchiveForecastCommand(tenantId, forecastId, userId);

        forecastCommandService.archive(command);
        return ResponseEntity.ok().build();
    }

    /**
     * Update actual amount
     *
     * @param forecastId the forecast ID
     * @param request the actual amount update request
     * @return 200 OK
     */
    @PostMapping("/{forecastId}/actual")
    @Operation(summary = "Update actual amount", description = "Updates the actual amount for a forecast")
    public ResponseEntity<Void> updateActualAmount(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @Valid @RequestBody UpdateActualAmountRequestDto request) {

        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        ForecastCommand.UpdateActualAmountCommand command =
                new ForecastCommand.UpdateActualAmountCommand(tenantId, forecastId,
                        request.getActualAmount(), userId);

        forecastCommandService.updateActualAmount(command);
        return ResponseEntity.ok().build();
    }

    /**
     * Add metric to forecast
     *
     * @param forecastId the forecast ID
     * @param request the add metric request
     * @return 200 OK
     */
    @PostMapping("/{forecastId}/metrics")
    @Operation(summary = "Add metric to forecast", description = "Adds a new metric to a forecast")
    public ResponseEntity<Void> addMetric(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @Valid @RequestBody AddMetricRequestDto request) {

        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        ForecastCommand.AddMetricCommand command =
                new ForecastCommand.AddMetricCommand(tenantId, forecastId,
                        request.getMetric(), userId);

        forecastCommandService.addMetric(command);
        return ResponseEntity.ok().build();
    }

    /**
     * Get forecasts by type
     *
     * @param forecastType the forecast type
     * @param page page number
     * @param size page size
     * @param sortBy sort field
     * @param sortDirection sort direction
     * @return page of forecasts
     */
    @GetMapping("/by-type/{forecastType}")
    @Operation(summary = "Get forecasts by type", description = "Retrieves forecasts filtered by type")
    public ResponseEntity<Page<ForecastResponseDto>> getForecastsByType(
            @Parameter(description = "Forecast type") @PathVariable Forecast.ForecastType forecastType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<Forecast> forecasts = forecastQueryService.getByType(forecastType, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(forecasts.map(this::toDto));
    }

    /**
     * Get forecasts by status
     *
     * @param status the forecast status
     * @param page page number
     * @param size page size
     * @param sortBy sort field
     * @param sortDirection sort direction
     * @return page of forecasts
     */
    @GetMapping("/by-status/{status}")
    @Operation(summary = "Get forecasts by status", description = "Retrieves forecasts filtered by status")
    public ResponseEntity<Page<ForecastResponseDto>> getForecastsByStatus(
            @Parameter(description = "Forecast status") @PathVariable Forecast.ForecastStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<Forecast> forecasts = forecastQueryService.getByStatus(status, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(forecasts.map(this::toDto));
    }

    /**
     * Get forecasts by date range
     *
     * @param startDate start date
     * @param endDate end date
     * @param statuses comma-separated status list
     * @param types comma-separated type list
     * @param page page number
     * @param size page size
     * @return page of forecasts
     */
    @GetMapping("/by-date-range")
    @Operation(summary = "Get forecasts by date range", description = "Retrieves forecasts within a date range")
    public ResponseEntity<Page<ForecastResponseDto>> getForecastsByDateRange(
            @Parameter(description = "Start date") @RequestParam Instant startDate,
            @Parameter(description = "End date") @RequestParam Instant endDate,
            @RequestParam(required = false) List<Forecast.ForecastStatus> statuses,
            @RequestParam(required = false) List<Forecast.ForecastType> types,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Forecast> forecasts = forecastQueryService.getByDateRange(startDate, endDate, statuses, types, page, size);
        return ResponseEntity.ok(forecasts.map(this::toDto));
    }

    /**
     * Get forecast summary
     *
     * @param startDate optional start date
     * @param endDate optional end date
     * @param department optional department filter
     * @param forecastType optional forecast type filter
     * @param scenario optional scenario filter
     * @return forecast summary
     */
    @GetMapping("/summary")
    @Operation(summary = "Get forecast summary", description = "Retrieves a summary of forecasts")
    public ResponseEntity<ForecastQueryService.ForecastSummary> getSummary(
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Forecast.ForecastType forecastType,
            @RequestParam(required = false) String scenario) {

        ForecastQueryService.ForecastSummary summary =
                forecastQueryService.getSummary(startDate, endDate, department, forecastType, scenario);

        return ResponseEntity.ok(summary);
    }

    /**
     * Get pending approvals
     *
     * @param department optional department filter
     * @param forecastType optional forecast type filter
     * @param page page number
     * @param size page size
     * @return page of pending forecasts
     */
    @GetMapping("/pending-approval")
    @Operation(summary = "Get pending approvals", description = "Retrieves forecasts pending approval")
    public ResponseEntity<Page<ForecastResponseDto>> getPendingApprovals(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Forecast.ForecastType forecastType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Forecast> forecasts = forecastQueryService.getPendingApproval(department, forecastType, page, size);
        return ResponseEntity.ok(forecasts.map(this::toDto));
    }

    /**
     * Get variance analysis
     *
     * @param forecastId the base forecast ID
     * @param comparisonForecastId the comparison forecast ID
     * @return variance analysis
     */
    @GetMapping("/variance-analysis")
    @Operation(summary = "Get variance analysis", description = "Compares two forecasts and returns variance analysis")
    public ResponseEntity<ForecastQueryService.VarianceAnalysis> getVarianceAnalysis(
            @RequestParam String forecastId,
            @RequestParam String comparisonForecastId) {

        ForecastQueryService.VarianceAnalysis analysis =
                forecastQueryService.getVarianceAnalysis(forecastId, comparisonForecastId);

        return ResponseEntity.ok(analysis);
    }

    /**
     * Converts Forecast entity to DTO
     *
     * @param forecast the forecast entity
     * @return the forecast response DTO
     */
    private ForecastResponseDto toDto(Forecast forecast) {
        return ForecastResponseDto.fromEntity(forecast);
    }

    // Request DTOs

    /**
     * Create Forecast Request DTO
     */
    @lombok.Data
    public static class CreateForecastRequestDto {
        @NotBlank(message = "Name is required")
        private String name;

        private String description;

        @NotNull(message = "Forecast type is required")
        private Forecast.ForecastType forecastType;

        @NotNull(message = "Forecast horizon is required")
        private Forecast.ForecastHorizon forecastHorizon;

        @NotNull(message = "Start date is required")
        private Instant startDate;

        @NotNull(message = "End date is required")
        private Instant endDate;

        @NotBlank(message = "Currency is required")
        private String currency;

        private String department;

        private String category;

        private String scenario;

        private Integer confidenceLevel;

        private String dataSource;

        private List<ForecastMetric> metrics;

        private String notes;

        private BigDecimal initialAmount;
    }

    /**
     * Update Forecast Request DTO
     */
    @lombok.Data
    public static class UpdateForecastRequestDto {
        private String name;
        private String description;
        private Instant startDate;
        private Instant endDate;
        private String department;
        private String category;
        private String scenario;
        private Integer confidenceLevel;
        private String dataSource;
        private String notes;
        private List<ForecastMetric> metrics;
    }

    /**
     * Approval Request DTO
     */
    @lombok.Data
    public static class ApprovalRequestDto {
        private String comments;
    }

    /**
     * Rejection Request DTO
     */
    @lombok.Data
    public static class RejectionRequestDto {
        @NotBlank(message = "Rejection reason is required")
        private String reason;
    }

    /**
     * Regenerate Forecast Request DTO
     */
    @lombok.Data
    public static class RegenerateForecastRequestDto {
        private List<ForecastMetric> newMetrics;
        private BigDecimal newTotalAmount;
        private String dataSource;
        private Integer newConfidenceLevel;
        private String scenario;
    }

    /**
     * Update Actual Amount Request DTO
     */
    @lombok.Data
    public static class UpdateActualAmountRequestDto {
        @NotNull(message = "Actual amount is required")
        private BigDecimal actualAmount;
    }

    /**
     * Add Metric Request DTO
     */
    @lombok.Data
    public static class AddMetricRequestDto {
        @NotNull(message = "Metric is required")
        private ForecastMetric metric;
    }
}
