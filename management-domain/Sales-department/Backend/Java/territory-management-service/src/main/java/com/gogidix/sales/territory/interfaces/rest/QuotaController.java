package com.gogidix.sales.territory.interfaces.rest;

import com.gogidix.sales.territory.application.dto.response.QuotaResponseDto;
import com.gogidix.sales.territory.application.service.QuotaCommandService;
import com.gogidix.sales.territory.application.service.QuotaQueryService;
import com.gogidix.sales.territory.domain.model.Quota;
import com.gogidix.sales.territory.domain.port.in.QuotaCommand;
import com.gogidix.sales.territory.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Quota REST Controller
 * Handles HTTP requests for quota operations
 */
@RestController
@RequestMapping("/quotas")
@RequiredArgsConstructor
@Tag(name = "Quotas", description = "Quota management endpoints")
public class QuotaController {

    private final QuotaCommandService quotaCommandService;
    private final QuotaQueryService quotaQueryService;

    @PostMapping
    @Operation(summary = "Create a new quota")
    public ResponseEntity<QuotaResponseDto> createQuota(
            @Valid @RequestBody CreateQuotaRequestDto request) {
        QuotaCommand.CreateQuotaCommand command = new QuotaCommand.CreateQuotaCommand(
            RequestContextHolder.getTenantId(),
            request.getTerritoryId(),
            request.getSalesRepresentativeId(),
            request.getType(),
            request.getAmount(),
            request.getCurrency(),
            request.getPeriod(),
            request.getStartDate(),
            request.getEndDate()
        );

        Quota quota = quotaCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(quota));
    }

    @GetMapping("/{quotaId}")
    @Operation(summary = "Get quota by ID")
    public ResponseEntity<QuotaResponseDto> getQuota(
            @Parameter(description = "Quota ID") @PathVariable String quotaId) {
        Quota quota = quotaQueryService.getById(quotaId);
        return ResponseEntity.ok(toDto(quota));
    }

    @GetMapping
    @Operation(summary = "Get all quotas for tenant")
    public ResponseEntity<List<QuotaResponseDto>> getAllQuotas() {
        List<Quota> quotas = quotaQueryService.getAllForTenant();
        return ResponseEntity.ok(quotas.stream().map(this::toDto).toList());
    }

    @GetMapping("/territory/{territoryId}")
    @Operation(summary = "Get quotas by territory")
    public ResponseEntity<List<QuotaResponseDto>> getQuotasByTerritory(
            @Parameter(description = "Territory ID") @PathVariable String territoryId) {
        List<Quota> quotas = quotaQueryService.getByTerritoryId(territoryId);
        return ResponseEntity.ok(quotas.stream().map(this::toDto).toList());
    }

    @GetMapping("/territory/{territoryId}/active")
    @Operation(summary = "Get active quotas by territory")
    public ResponseEntity<List<QuotaResponseDto>> getActiveQuotasByTerritory(
            @Parameter(description = "Territory ID") @PathVariable String territoryId) {
        List<Quota> quotas = quotaQueryService.getActiveByTerritoryId(territoryId);
        return ResponseEntity.ok(quotas.stream().map(this::toDto).toList());
    }

    @GetMapping("/representative/{salesRepresentativeId}")
    @Operation(summary = "Get quotas by sales representative")
    public ResponseEntity<List<QuotaResponseDto>> getQuotasByRepresentative(
            @Parameter(description = "Sales Representative ID") @PathVariable String salesRepresentativeId) {
        List<Quota> quotas = quotaQueryService.getBySalesRepresentativeId(salesRepresentativeId);
        return ResponseEntity.ok(quotas.stream().map(this::toDto).toList());
    }

    @GetMapping("/representative/{salesRepresentativeId}/active")
    @Operation(summary = "Get active quotas by sales representative")
    public ResponseEntity<List<QuotaResponseDto>> getActiveQuotasByRepresentative(
            @Parameter(description = "Sales Representative ID") @PathVariable String salesRepresentativeId) {
        List<Quota> quotas = quotaQueryService.getActiveBySalesRepresentativeId(salesRepresentativeId);
        return ResponseEntity.ok(quotas.stream().map(this::toDto).toList());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get quotas by status")
    public ResponseEntity<List<QuotaResponseDto>> getQuotasByStatus(
            @Parameter(description = "Quota Status") @PathVariable Quota.QuotaStatus status) {
        List<Quota> quotas = quotaQueryService.getByStatus(status);
        return ResponseEntity.ok(quotas.stream().map(this::toDto).toList());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get quotas by type")
    public ResponseEntity<List<QuotaResponseDto>> getQuotasByType(
            @Parameter(description = "Quota Type") @PathVariable Quota.QuotaType type) {
        List<Quota> quotas = quotaQueryService.getByType(type);
        return ResponseEntity.ok(quotas.stream().map(this::toDto).toList());
    }

    @GetMapping("/period/{period}")
    @Operation(summary = "Get quotas by period")
    public ResponseEntity<List<QuotaResponseDto>> getQuotasByPeriod(
            @Parameter(description = "Quota Period") @PathVariable Quota.QuotaPeriod period) {
        List<Quota> quotas = quotaQueryService.getByPeriod(period);
        return ResponseEntity.ok(quotas.stream().map(this::toDto).toList());
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Get quotas by year")
    public ResponseEntity<List<QuotaResponseDto>> getQuotasByYear(
            @Parameter(description = "Year") @PathVariable Integer year) {
        List<Quota> quotas = quotaQueryService.getByYear(year);
        return ResponseEntity.ok(quotas.stream().map(this::toDto).toList());
    }

    @GetMapping("/year/{year}/month/{month}")
    @Operation(summary = "Get quotas by year and month")
    public ResponseEntity<List<QuotaResponseDto>> getQuotasByYearAndMonth(
            @Parameter(description = "Year") @PathVariable Integer year,
            @Parameter(description = "Month") @PathVariable Integer month) {
        List<Quota> quotas = quotaQueryService.getByYearAndMonth(year, month);
        return ResponseEntity.ok(quotas.stream().map(this::toDto).toList());
    }

    @GetMapping("/territory/{territoryId}/type/{type}")
    @Operation(summary = "Get active quota by territory and type")
    public ResponseEntity<QuotaResponseDto> getActiveQuotaByTerritoryAndType(
            @Parameter(description = "Territory ID") @PathVariable String territoryId,
            @Parameter(description = "Quota Type") @PathVariable Quota.QuotaType type) {
        Quota quota = quotaQueryService.getActiveByTerritoryIdAndType(territoryId, type);
        return ResponseEntity.ok(toDto(quota));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get quota summary")
    public ResponseEntity<QuotaQueryService.QuotaSummary> getSummary() {
        QuotaQueryService.QuotaSummary summary = quotaQueryService.getSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/search")
    @Operation(summary = "Search quotas")
    public ResponseEntity<Page<QuotaResponseDto>> searchQuotas(
            @Parameter(description = "Territory ID") @RequestParam(required = false) String territoryId,
            @Parameter(description = "Sales Representative ID") @RequestParam(required = false) String salesRepresentativeId,
            @Parameter(description = "Quota Type") @RequestParam(required = false) Quota.QuotaType type,
            @Parameter(description = "Quota Status") @RequestParam(required = false) Quota.QuotaStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Quota> quotas = quotaQueryService.searchQuotas(territoryId, salesRepresentativeId, type, status, pageable);
        return ResponseEntity.ok(quotas.map(this::toDto));
    }

    @PostMapping("/{quotaId}/activate")
    @Operation(summary = "Activate quota")
    public ResponseEntity<Void> activateQuota(
            @Parameter(description = "Quota ID") @PathVariable String quotaId) {

        QuotaCommand.ActivateQuotaCommand command = new QuotaCommand.ActivateQuotaCommand(
            RequestContextHolder.getTenantId(),
            quotaId,
            RequestContextHolder.getUserId()
        );

        quotaCommandService.activate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{quotaId}/pause")
    @Operation(summary = "Pause quota")
    public ResponseEntity<Void> pauseQuota(
            @Parameter(description = "Quota ID") @PathVariable String quotaId) {

        QuotaCommand.PauseQuotaCommand command = new QuotaCommand.PauseQuotaCommand(
            RequestContextHolder.getTenantId(), quotaId);

        quotaCommandService.pause(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{quotaId}/resume")
    @Operation(summary = "Resume quota")
    public ResponseEntity<Void> resumeQuota(
            @Parameter(description = "Quota ID") @PathVariable String quotaId) {

        QuotaCommand.ResumeQuotaCommand command = new QuotaCommand.ResumeQuotaCommand(
            RequestContextHolder.getTenantId(), quotaId);

        quotaCommandService.resume(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{quotaId}/cancel")
    @Operation(summary = "Cancel quota")
    public ResponseEntity<Void> cancelQuota(
            @Parameter(description = "Quota ID") @PathVariable String quotaId,
            @RequestBody CancelQuotaRequestDto request) {

        QuotaCommand.CancelQuotaCommand command = new QuotaCommand.CancelQuotaCommand(
            RequestContextHolder.getTenantId(), quotaId, request.getReason());

        quotaCommandService.cancel(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{quotaId}/adjust")
    @Operation(summary = "Adjust quota amount")
    public ResponseEntity<Void> adjustQuota(
            @Parameter(description = "Quota ID") @PathVariable String quotaId,
            @RequestBody AdjustQuotaRequestDto request) {

        QuotaCommand.AdjustQuotaCommand command = new QuotaCommand.AdjustQuotaCommand(
            RequestContextHolder.getTenantId(),
            quotaId,
            request.getNewAmount(),
            RequestContextHolder.getUserId(),
            request.getReason()
        );

        quotaCommandService.adjust(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{quotaId}/achievement")
    @Operation(summary = "Update quota achievement")
    public ResponseEntity<Void> updateAchievement(
            @Parameter(description = "Quota ID") @PathVariable String quotaId,
            @RequestBody UpdateAchievementRequestDto request) {

        QuotaCommand.UpdateAchievementCommand command = new QuotaCommand.UpdateAchievementCommand(
            RequestContextHolder.getTenantId(),
            quotaId,
            request.getAchievement()
        );

        quotaCommandService.updateAchievement(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{quotaId}/breakdown")
    @Operation(summary = "Add quota breakdown")
    public ResponseEntity<Void> addBreakdown(
            @Parameter(description = "Quota ID") @PathVariable String quotaId,
            @RequestBody AddBreakdownRequestDto request) {

        QuotaCommand.AddQuotaBreakdownCommand command = new QuotaCommand.AddQuotaBreakdownCommand(
            RequestContextHolder.getTenantId(),
            quotaId,
            request.getCategory(),
            request.getAmount(),
            request.getDescription(),
            request.getProductId(),
            request.getProductCategoryId()
        );

        quotaCommandService.addBreakdown(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{quotaId}")
    @Operation(summary = "Delete quota")
    public ResponseEntity<Void> deleteQuota(
            @Parameter(description = "Quota ID") @PathVariable String quotaId) {

        QuotaCommand.DeleteQuotaCommand command = new QuotaCommand.DeleteQuotaCommand(
            RequestContextHolder.getTenantId(), quotaId);

        quotaCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    private QuotaResponseDto toDto(Quota quota) {
        return QuotaResponseDto.builder()
            .id(quota.getId())
            .quotaId(quota.getQuotaId())
            .tenantId(quota.getTenantId())
            .territoryId(quota.getTerritoryId())
            .salesRepresentativeId(quota.getSalesRepresentativeId())
            .type(mapType(quota.getType()))
            .amount(quota.getAmount())
            .currency(quota.getCurrency())
            .period(mapPeriod(quota.getPeriod()))
            .year(quota.getYear())
            .month(quota.getMonth())
            .startDate(quota.getStartDate())
            .endDate(quota.getEndDate())
            .breakdown(quota.getBreakdown() != null ? quota.getBreakdown().stream().map(this::mapBreakdown).toList() : null)
            .status(mapStatus(quota.getStatus()))
            .currentAchievement(quota.getCurrentAchievement())
            .attainmentPercentage(quota.getAttainmentPercentage())
            .lastCalculatedAt(quota.getLastCalculatedAt())
            .approvedBy(quota.getApprovedBy())
            .approvedAt(quota.getApprovedAt())
            .createdAt(quota.getCreatedAt())
            .updatedAt(quota.getUpdatedAt())
            .build();
    }

    private QuotaResponseDto.QuotaTypeDto mapType(Quota.QuotaType type) {
        return type != null ? QuotaResponseDto.QuotaTypeDto.valueOf(type.name()) : null;
    }

    private QuotaResponseDto.QuotaPeriodDto mapPeriod(Quota.QuotaPeriod period) {
        return period != null ? QuotaResponseDto.QuotaPeriodDto.valueOf(period.name()) : null;
    }

    private QuotaResponseDto.QuotaStatusDto mapStatus(Quota.QuotaStatus status) {
        return status != null ? QuotaResponseDto.QuotaStatusDto.valueOf(status.name()) : null;
    }

    private QuotaResponseDto.QuotaBreakdownDto mapBreakdown(Quota.QuotaBreakdown breakdown) {
        return QuotaResponseDto.QuotaBreakdownDto.builder()
            .category(breakdown.getCategory())
            .amount(breakdown.getAmount())
            .description(breakdown.getDescription())
            .productId(breakdown.getProductId())
            .productCategoryId(breakdown.getProductCategoryId())
            .build();
    }

    @Data
    public static class CreateQuotaRequestDto {
        public String territoryId;
        public String salesRepresentativeId;
        public Quota.QuotaType type;
        public BigDecimal amount;
        public String currency;
        public Quota.QuotaPeriod period;
        public LocalDate startDate;
        public LocalDate endDate;
    }

    @Data
    public static class CancelQuotaRequestDto {
        public String reason;
    }

    @Data
    public static class AdjustQuotaRequestDto {
        public BigDecimal newAmount;
        public String reason;
    }

    @Data
    public static class UpdateAchievementRequestDto {
        public BigDecimal achievement;
    }

    @Data
    public static class AddBreakdownRequestDto {
        public String category;
        public BigDecimal amount;
        public String description;
        public String productId;
        public String productCategoryId;
    }
}
