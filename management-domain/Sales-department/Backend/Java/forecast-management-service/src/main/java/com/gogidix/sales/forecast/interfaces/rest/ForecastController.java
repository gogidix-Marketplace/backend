package com.gogidix.sales.forecast.interfaces.rest;

import com.gogidix.sales.forecast.application.dto.response.ForecastResponseDto;
import com.gogidix.sales.forecast.application.dto.response.ForecastSummaryDto;
import com.gogidix.sales.forecast.application.service.ForecastCommandService;
import com.gogidix.sales.forecast.application.service.ForecastQueryService;
import com.gogidix.sales.forecast.domain.model.Forecast;
import com.gogidix.sales.forecast.domain.model.ForecastLineItem;
import com.gogidix.sales.forecast.domain.port.in.ForecastCommand;
import com.gogidix.sales.forecast.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * Forecast REST Controller
 * Handles HTTP requests for forecast operations
 */
@RestController
@RequestMapping("/forecasts")
@RequiredArgsConstructor
@Tag(name = "Forecasts", description = "Sales forecast management endpoints")
public class ForecastController {

    private final ForecastCommandService forecastCommandService;
    private final ForecastQueryService forecastQueryService;

    @PostMapping
    @Operation(summary = "Create a new forecast")
    public ResponseEntity<ForecastResponseDto> createForecast(
            @Valid @RequestBody CreateForecastRequestDto request) {
        ForecastCommand.CreateForecastCommand command = new ForecastCommand.CreateForecastCommand(
            RequestContextHolder.getTenantId(),
            request.getName(),
            request.getDescription(),
            request.getPeriod(),
            request.getStartDate(),
            request.getEndDate(),
            RequestContextHolder.getUserId(),
            request.getCurrency(),
            request.getRegion(),
            request.getTerritory(),
            request.getBusinessUnit()
        );

        Forecast forecast = forecastCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(forecast));
    }

    @GetMapping("/{forecastId}")
    @Operation(summary = "Get forecast by ID")
    public ResponseEntity<ForecastResponseDto> getForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {
        Forecast forecast = forecastQueryService.getById(forecastId);
        return ResponseEntity.ok(toDto(forecast));
    }

    @GetMapping
    @Operation(summary = "Get all forecasts for tenant")
    public ResponseEntity<List<ForecastResponseDto>> getAllForecasts() {
        List<Forecast> forecasts = forecastQueryService.getAllForTenant();
        return ResponseEntity.ok(forecasts.stream().map(this::toDto).toList());
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated forecasts")
    public ResponseEntity<Page<ForecastResponseDto>> getPaginatedForecasts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<Forecast> forecasts = forecastQueryService.getPaginated(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(forecasts.map(this::toDto));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get forecasts by status")
    public ResponseEntity<List<ForecastResponseDto>> getForecastsByStatus(
            @Parameter(description = "Forecast status") @PathVariable String status) {
        Forecast.ForecastStatus statusEnum = Forecast.ForecastStatus.valueOf(status.toUpperCase());
        List<Forecast> forecasts = forecastQueryService.getByStatus(statusEnum);
        return ResponseEntity.ok(forecasts.stream().map(this::toDto).toList());
    }

    @GetMapping("/period/{period}")
    @Operation(summary = "Get forecasts by period")
    public ResponseEntity<List<ForecastResponseDto>> getForecastsByPeriod(
            @Parameter(description = "Forecast period") @PathVariable String period) {
        Forecast.ForecastPeriod periodEnum = Forecast.ForecastPeriod.valueOf(period.toUpperCase());
        List<Forecast> forecasts = forecastQueryService.getByPeriod(periodEnum);
        return ResponseEntity.ok(forecasts.stream().map(this::toDto).toList());
    }

    @PutMapping("/{forecastId}")
    @Operation(summary = "Update a forecast")
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
            request.getRegion(),
            request.getTerritory(),
            request.getBusinessUnit()
        );

        Forecast forecast = forecastCommandService.update(command);
        return ResponseEntity.ok(toDto(forecast));
    }

    @PostMapping("/{forecastId}/submit")
    @Operation(summary = "Submit forecast for approval")
    public ResponseEntity<Void> submitForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        ForecastCommand.SubmitForecastCommand command = new ForecastCommand.SubmitForecastCommand(
            RequestContextHolder.getTenantId(), forecastId);

        forecastCommandService.submit(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{forecastId}/approve")
    @Operation(summary = "Approve forecast")
    public ResponseEntity<Void> approveForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @RequestBody ApprovalRequestDto request) {

        ForecastCommand.ApproveForecastCommand command = new ForecastCommand.ApproveForecastCommand(
            RequestContextHolder.getTenantId(),
            forecastId,
            RequestContextHolder.getUserId(),
            request.getApprovalLevel()
        );

        forecastCommandService.approve(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{forecastId}/reject")
    @Operation(summary = "Reject forecast")
    public ResponseEntity<Void> rejectForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @RequestBody RejectionRequestDto request) {

        ForecastCommand.RejectForecastCommand command = new ForecastCommand.RejectForecastCommand(
            RequestContextHolder.getTenantId(),
            forecastId,
            RequestContextHolder.getUserId(),
            request.getReason()
        );

        forecastCommandService.reject(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{forecastId}/publish")
    @Operation(summary = "Publish forecast")
    public ResponseEntity<Void> publishForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        ForecastCommand.PublishForecastCommand command = new ForecastCommand.PublishForecastCommand(
            RequestContextHolder.getTenantId(), forecastId);

        forecastCommandService.publish(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{forecastId}/archive")
    @Operation(summary = "Archive forecast")
    public ResponseEntity<Void> archiveForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        ForecastCommand.ArchiveForecastCommand command = new ForecastCommand.ArchiveForecastCommand(
            RequestContextHolder.getTenantId(), forecastId);

        forecastCommandService.archive(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{forecastId}/lock")
    @Operation(summary = "Lock forecast")
    public ResponseEntity<Void> lockForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        ForecastCommand.LockForecastCommand command = new ForecastCommand.LockForecastCommand(
            RequestContextHolder.getTenantId(), forecastId);

        forecastCommandService.lock(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{forecastId}/unlock")
    @Operation(summary = "Unlock forecast")
    public ResponseEntity<Void> unlockForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        ForecastCommand.UnlockForecastCommand command = new ForecastCommand.UnlockForecastCommand(
            RequestContextHolder.getTenantId(), forecastId);

        forecastCommandService.unlock(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{forecastId}")
    @Operation(summary = "Delete forecast")
    public ResponseEntity<Void> deleteForecast(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        ForecastCommand.DeleteForecastCommand command = new ForecastCommand.DeleteForecastCommand(
            RequestContextHolder.getTenantId(), forecastId);

        forecastCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    // Line Item Operations

    @PostMapping("/{forecastId}/line-items")
    @Operation(summary = "Add line item to forecast")
    public ResponseEntity<ForecastResponseDto.ForecastLineItemDto> addLineItem(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @Valid @RequestBody AddLineItemRequestDto request) {

        ForecastCommand.AddLineItemCommand command = new ForecastCommand.AddLineItemCommand(
            RequestContextHolder.getTenantId(),
            forecastId,
            request.getName(),
            request.getDescription(),
            request.getCategory(),
            request.getType(),
            request.getBestCase(),
            request.getLikely(),
            request.getWorstCase(),
            request.getCurrency(),
            request.getProductId(),
            request.getProductName(),
            request.getTerritoryId(),
            request.getTerritoryName(),
            request.getCustomerSegmentId(),
            request.getCustomerSegmentName(),
            request.getSalesChannel(),
            request.getNotes(),
            request.getOwner()
        );

        ForecastLineItem lineItem = forecastCommandService.addLineItem(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toLineItemDto(lineItem));
    }

    @PutMapping("/{forecastId}/line-items/{lineItemId}")
    @Operation(summary = "Update line item")
    public ResponseEntity<Void> updateLineItem(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @Parameter(description = "Line Item ID") @PathVariable String lineItemId,
            @RequestBody UpdateLineItemRequestDto request) {

        ForecastCommand.UpdateLineItemCommand command = new ForecastCommand.UpdateLineItemCommand(
            RequestContextHolder.getTenantId(),
            forecastId,
            lineItemId,
            request.getName(),
            request.getCategory(),
            request.getBestCase(),
            request.getLikely(),
            request.getWorstCase(),
            request.getNotes()
        );

        forecastCommandService.updateLineItem(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{forecastId}/line-items/{lineItemId}")
    @Operation(summary = "Remove line item")
    public ResponseEntity<Void> removeLineItem(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId,
            @Parameter(description = "Line Item ID") @PathVariable String lineItemId) {

        ForecastCommand.RemoveLineItemCommand command = new ForecastCommand.RemoveLineItemCommand(
            RequestContextHolder.getTenantId(), forecastId, lineItemId);

        forecastCommandService.removeLineItem(command);
        return ResponseEntity.ok().build();
    }

    // Versioning

    @PostMapping("/{forecastId}/versions")
    @Operation(summary = "Create new version of forecast")
    public ResponseEntity<ForecastResponseDto> createVersion(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {

        ForecastCommand.CreateVersionCommand command = new ForecastCommand.CreateVersionCommand(
            RequestContextHolder.getTenantId(),
            forecastId,
            RequestContextHolder.getUserId()
        );

        Forecast newVersion = forecastCommandService.createVersion(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(newVersion));
    }

    @GetMapping("/{forecastId}/versions")
    @Operation(summary = "Get forecast versions")
    public ResponseEntity<List<ForecastResponseDto>> getVersions(
            @Parameter(description = "Forecast ID") @PathVariable String forecastId) {
        List<Forecast> versions = forecastQueryService.getVersions(forecastId);
        return ResponseEntity.ok(versions.stream().map(this::toDto).toList());
    }

    // Summary and Analytics

    @GetMapping("/summary")
    @Operation(summary = "Get forecast summary")
    public ResponseEntity<ForecastSummaryDto> getSummary(
            @RequestParam(required = false) YearMonth startDate,
            @RequestParam(required = false) YearMonth endDate,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String territory) {

        ForecastQueryService.ForecastSummary summary =
            forecastQueryService.getSummary(startDate, endDate, region, territory);

        return ResponseEntity.ok(ForecastSummaryDto.builder()
            .totalCount(summary.totalCount())
            .totalBestCase(summary.totalBestCase())
            .totalLikely(summary.totalLikely())
            .totalWorstCase(summary.totalWorstCase())
            .draftCount(summary.draftCount())
            .submittedCount(summary.submittedCount())
            .approvedCount(summary.approvedCount())
            .publishedCount(summary.publishedCount())
            .build());
    }

    @GetMapping("/analytics/by-region")
    @Operation(summary = "Get forecast totals by region")
    public ResponseEntity<Map<String, BigDecimal>> getForecastByRegion() {
        return ResponseEntity.ok(forecastQueryService.getForecastByRegion());
    }

    @GetMapping("/analytics/by-territory")
    @Operation(summary = "Get forecast totals by territory")
    public ResponseEntity<Map<String, BigDecimal>> getForecastByTerritory() {
        return ResponseEntity.ok(forecastQueryService.getForecastByTerritory());
    }

    private ForecastResponseDto toDto(Forecast forecast) {
        List<ForecastResponseDto.ForecastLineItemDto> lineItemDtos = null;
        if (forecast.getLineItems() != null) {
            lineItemDtos = forecast.getLineItems().stream()
                .map(this::toLineItemDto)
                .toList();
        }

        return ForecastResponseDto.builder()
            .id(forecast.getId())
            .forecastId(forecast.getForecastId())
            .tenantId(forecast.getTenantId())
            .name(forecast.getName())
            .description(forecast.getDescription())
            .period(mapPeriod(forecast.getPeriod()))
            .startDate(forecast.getStartDate())
            .endDate(forecast.getEndDate())
            .status(mapStatus(forecast.getStatus()))
            .createdBy(forecast.getCreatedBy())
            .approvedBy(forecast.getApprovedBy())
            .version(forecast.getVersion())
            .parentForecastId(forecast.getParentForecastId())
            .totalBestCase(forecast.getTotalBestCase())
            .totalLikely(forecast.getTotalLikely())
            .totalWorstCase(forecast.getTotalWorstCase())
            .currency(forecast.getCurrency())
            .region(forecast.getRegion())
            .territory(forecast.getTerritory())
            .businessUnit(forecast.getBusinessUnit())
            .lineItems(lineItemDtos)
            .currentApprovalLevel(mapApprovalLevel(forecast.getCurrentApprovalLevel()))
            .rejectionReason(forecast.getRejectionReason())
            .locked(forecast.getLocked())
            .createdAt(forecast.getCreatedAt())
            .updatedAt(forecast.getUpdatedAt())
            .build();
    }

    private ForecastResponseDto.ForecastLineItemDto toLineItemDto(ForecastLineItem item) {
        return ForecastResponseDto.ForecastLineItemDto.builder()
            .lineItemId(item.getLineItemId())
            .name(item.getName())
            .description(item.getDescription())
            .category(item.getCategory() != null ? item.getCategory().name() : null)
            .type(item.getType() != null ? item.getType().name() : null)
            .bestCase(item.getBestCase())
            .likely(item.getLikely())
            .worstCase(item.getWorstCase())
            .currency(item.getCurrency())
            .productId(item.getProductId())
            .productName(item.getProductName())
            .territoryId(item.getTerritoryId())
            .territoryName(item.getTerritoryName())
            .customerSegmentId(item.getCustomerSegmentId())
            .customerSegmentName(item.getCustomerSegmentName())
            .salesChannel(item.getSalesChannel())
            .notes(item.getNotes())
            .owner(item.getOwner())
            .active(item.getActive())
            .build();
    }

    private ForecastResponseDto.ForecastPeriodDto mapPeriod(Forecast.ForecastPeriod period) {
        return period != null ? ForecastResponseDto.ForecastPeriodDto.valueOf(period.name()) : null;
    }

    private ForecastResponseDto.ForecastStatusDto mapStatus(Forecast.ForecastStatus status) {
        return status != null ? ForecastResponseDto.ForecastStatusDto.valueOf(status.name()) : null;
    }

    private ForecastResponseDto.ApprovalLevelDto mapApprovalLevel(Forecast.ApprovalLevel level) {
        return level != null ? ForecastResponseDto.ApprovalLevelDto.valueOf(level.name()) : null;
    }

    // Request DTOs
    @Data
    public static class CreateForecastRequestDto {
        public String name;
        public String description;
        public Forecast.ForecastPeriod period;
        public YearMonth startDate;
        public YearMonth endDate;
        public String currency;
        public String region;
        public String territory;
        public String businessUnit;
    }

    @Data

    public static class UpdateForecastRequestDto {
        public String name;
        public String description;
        public YearMonth startDate;
        public YearMonth endDate;
        public String region;
        public String territory;
        public String businessUnit;
    }

    @Data

    public static class ApprovalRequestDto {
        public Forecast.ApprovalLevel approvalLevel;
    }

    @Data

    public static class RejectionRequestDto {
        public String reason;
    }

    @Data

    public static class AddLineItemRequestDto {
        public String name;
        public String description;
        public ForecastLineItem.ForecastCategory category;
        public ForecastLineItem.LineItemType type;
        public BigDecimal bestCase;
        public BigDecimal likely;
        public BigDecimal worstCase;
        public String currency;
        public String productId;
        public String productName;
        public String territoryId;
        public String territoryName;
        public String customerSegmentId;
        public String customerSegmentName;
        public String salesChannel;
        public String notes;
        public String owner;
    }

    @Data

    public static class UpdateLineItemRequestDto {
        public String name;
        public ForecastLineItem.ForecastCategory category;
        public BigDecimal bestCase;
        public BigDecimal likely;
        public BigDecimal worstCase;
        public String notes;
    }
}
