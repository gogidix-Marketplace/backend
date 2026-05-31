package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.application.dto.response.TaxRateResponseDto;
import com.gogidix.finance.tax.application.service.TaxRateCommandService;
import com.gogidix.finance.tax.application.service.TaxRateQueryService;
import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.port.in.TaxRateCommand;
import com.gogidix.finance.tax.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Tax Rate REST Controller
 * Handles HTTP requests for tax rate operations
 */
@RestController
@RequestMapping("/tax-rates")
@RequiredArgsConstructor
@Tag(name = "Tax Rates", description = "Tax rate management endpoints")
public class TaxRateController {

    private final TaxRateCommandService taxRateCommandService;
    private final TaxRateQueryService taxRateQueryService;

    @PostMapping
    @Operation(summary = "Create a new tax rate")
    public ResponseEntity<TaxRateResponseDto> createTaxRate(@Valid @RequestBody CreateTaxRateRequestDto request) {
        TaxRateCommand.CreateTaxRateCommand command = new TaxRateCommand.CreateTaxRateCommand(
            RequestContextHolder.getTenantId(),
            request.getJurisdiction(),
            request.getTaxType(),
            request.getTaxCode(),
            request.getRatePercentage(),
            request.getEffectiveDate(),
            request.getExpiryDate(),
            request.getDescription(),
            request.getIsCompound(),
            request.getIsRecoverable(),
            request.getRecoveryRate(),
            request.getMinThreshold(),
            request.getMaxThreshold(),
            RequestContextHolder.getUserIdOrDefault(),
            request.getNotes()
        );

        TaxRate taxRate = taxRateCommandService.createTaxRate(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(taxRate));
    }

    @GetMapping("/{taxRateId}")
    @Operation(summary = "Get tax rate by ID")
    public ResponseEntity<TaxRateResponseDto> getTaxRate(
            @Parameter(description = "Tax Rate ID") @PathVariable String taxRateId) {
        TaxRate taxRate = taxRateQueryService.getById(taxRateId);
        return ResponseEntity.ok(toDto(taxRate));
    }

    @GetMapping
    @Operation(summary = "Get all tax rates for tenant")
    public ResponseEntity<List<TaxRateResponseDto>> getAllTaxRates() {
        List<TaxRate> taxRates = taxRateQueryService.getAllForTenant();
        return ResponseEntity.ok(taxRates.stream().map(this::toDto).toList());
    }

    @GetMapping("/jurisdiction/{jurisdiction}")
    @Operation(summary = "Get tax rates by jurisdiction")
    public ResponseEntity<List<TaxRateResponseDto>> getByJurisdiction(
            @Parameter(description = "Jurisdiction") @PathVariable String jurisdiction) {
        List<TaxRate> taxRates = taxRateQueryService.getByJurisdiction(jurisdiction);
        return ResponseEntity.ok(taxRates.stream().map(this::toDto).toList());
    }

    @GetMapping("/type/{taxType}")
    @Operation(summary = "Get tax rates by tax type")
    public ResponseEntity<List<TaxRateResponseDto>> getByTaxType(
            @Parameter(description = "Tax Type") @PathVariable String taxType) {
        List<TaxRate> taxRates = taxRateQueryService.getByTaxType(taxType);
        return ResponseEntity.ok(taxRates.stream().map(this::toDto).toList());
    }

    @GetMapping("/jurisdiction/{jurisdiction}/type/{taxType}")
    @Operation(summary = "Get tax rates by jurisdiction and type")
    public ResponseEntity<List<TaxRateResponseDto>> getByJurisdictionAndType(
            @Parameter(description = "Jurisdiction") @PathVariable String jurisdiction,
            @Parameter(description = "Tax Type") @PathVariable String taxType) {
        List<TaxRate> taxRates = taxRateQueryService.getByJurisdictionAndType(jurisdiction, taxType);
        return ResponseEntity.ok(taxRates.stream().map(this::toDto).toList());
    }

    @GetMapping("/effective")
    @Operation(summary = "Get effective tax rate for a given date")
    public ResponseEntity<TaxRateResponseDto> getEffectiveRate(
            @Parameter(description = "Jurisdiction") @RequestParam String jurisdiction,
            @Parameter(description = "Tax Type") @RequestParam String taxType,
            @Parameter(description = "Tax Code") @RequestParam(required = false) String taxCode,
            @Parameter(description = "Date") @RequestParam(required = false) String date) {
        LocalDate queryDate = date != null ? LocalDate.parse(date) : null;
        TaxRate taxRate = taxRateQueryService.getEffectiveRate(jurisdiction, taxType, taxCode, queryDate);
        return ResponseEntity.ok(toDto(taxRate));
    }

    @GetMapping("/active/{jurisdiction}")
    @Operation(summary = "Get active tax rates for jurisdiction")
    public ResponseEntity<List<TaxRateResponseDto>> getActiveRatesForJurisdiction(
            @Parameter(description = "Jurisdiction") @PathVariable String jurisdiction) {
        List<TaxRate> taxRates = taxRateQueryService.getActiveRatesForJurisdiction(jurisdiction);
        return ResponseEntity.ok(taxRates.stream().map(this::toDto).toList());
    }

    @GetMapping("/search")
    @Operation(summary = "Search tax rates")
    public ResponseEntity<PageImpl<TaxRateResponseDto>> search(
            @Parameter(description = "Search term") @RequestParam(required = false) String searchTerm,
            @Parameter(description = "Jurisdiction") @RequestParam(required = false) String jurisdiction,
            @Parameter(description = "Tax Type") @RequestParam(required = false) String taxType,
            @Parameter(description = "Status") @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageImpl<TaxRate> result = taxRateQueryService.search(searchTerm, jurisdiction, taxType, status, page, size);
        return ResponseEntity.ok(new PageImpl<>(
            result.getContent().stream().map(this::toDto).toList(),
            result.getPageable(),
            result.getTotalElements()
        ));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get tax rate summary")
    public ResponseEntity<TaxRateQueryService.TaxRateSummary> getSummary() {
        TaxRateQueryService.TaxRateSummary summary = taxRateQueryService.getSummary();
        return ResponseEntity.ok(summary);
    }

    @PutMapping("/{taxRateId}")
    @Operation(summary = "Update tax rate")
    public ResponseEntity<TaxRateResponseDto> updateTaxRate(
            @Parameter(description = "Tax Rate ID") @PathVariable String taxRateId,
            @RequestBody UpdateTaxRateRequestDto request) {
        TaxRateCommand.UpdateTaxRateCommand command = new TaxRateCommand.UpdateTaxRateCommand(
            RequestContextHolder.getTenantId(),
            taxRateId,
            request.getNewRate(),
            request.getDescription(),
            request.getNewExpiryDate(),
            request.getIsCompound(),
            request.getIsRecoverable(),
            request.getRecoveryRate(),
            request.getNotes()
        );

        TaxRate taxRate = taxRateCommandService.updateTaxRate(command);
        return ResponseEntity.ok(toDto(taxRate));
    }

    @PostMapping("/{taxRateId}/activate")
    @Operation(summary = "Activate tax rate")
    public ResponseEntity<Void> activateTaxRate(
            @Parameter(description = "Tax Rate ID") @PathVariable String taxRateId) {
        TaxRateCommand.ActivateTaxRateCommand command = new TaxRateCommand.ActivateTaxRateCommand(
            RequestContextHolder.getTenantId(),
            taxRateId,
            RequestContextHolder.getUserIdOrDefault()
        );

        taxRateCommandService.activateTaxRate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taxRateId}/expire")
    @Operation(summary = "Expire tax rate")
    public ResponseEntity<Void> expireTaxRate(
            @Parameter(description = "Tax Rate ID") @PathVariable String taxRateId) {
        TaxRateCommand.ExpireTaxRateCommand command = new TaxRateCommand.ExpireTaxRateCommand(
            RequestContextHolder.getTenantId(),
            taxRateId
        );

        taxRateCommandService.expireTaxRate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taxRateId}/archive")
    @Operation(summary = "Archive tax rate")
    public ResponseEntity<Void> archiveTaxRate(
            @Parameter(description = "Tax Rate ID") @PathVariable String taxRateId) {
        TaxRateCommand.ArchiveTaxRateCommand command = new TaxRateCommand.ArchiveTaxRateCommand(
            RequestContextHolder.getTenantId(),
            taxRateId
        );

        taxRateCommandService.archiveTaxRate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taxRateId}/new-version")
    @Operation(summary = "Create new version of tax rate")
    public ResponseEntity<TaxRateResponseDto> createNewVersion(
            @Parameter(description = "Tax Rate ID") @PathVariable String taxRateId,
            @RequestBody NewVersionRequestDto request) {
        TaxRateCommand.CreateNewVersionCommand command = new TaxRateCommand.CreateNewVersionCommand(
            RequestContextHolder.getTenantId(),
            taxRateId,
            request.getNewRate(),
            request.getNewEffectiveDate(),
            RequestContextHolder.getUserIdOrDefault()
        );

        TaxRate taxRate = taxRateCommandService.createNewVersion(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(taxRate));
    }

    @DeleteMapping("/{taxRateId}")
    @Operation(summary = "Delete tax rate")
    public ResponseEntity<Void> deleteTaxRate(
            @Parameter(description = "Tax Rate ID") @PathVariable String taxRateId) {
        TaxRateCommand.DeleteTaxRateCommand command = new TaxRateCommand.DeleteTaxRateCommand(
            RequestContextHolder.getTenantId(),
            taxRateId
        );

        taxRateCommandService.deleteTaxRate(command);
        return ResponseEntity.noContent().build();
    }

    private TaxRateResponseDto toDto(TaxRate taxRate) {
        return TaxRateResponseDto.builder()
            .id(taxRate.getId())
            .taxRateId(taxRate.getTaxRateId())
            .tenantId(taxRate.getTenantId())
            .jurisdiction(mapJurisdiction(taxRate.getJurisdiction()))
            .taxType(mapTaxType(taxRate.getTaxType()))
            .taxCode(taxRate.getTaxCode())
            .ratePercentage(taxRate.getRatePercentage())
            .effectiveDate(taxRate.getEffectiveDate())
            .expiryDate(taxRate.getExpiryDate())
            .description(taxRate.getDescription())
            .isCompound(taxRate.getIsCompound())
            .isRecoverable(taxRate.getIsRecoverable())
            .recoveryRate(taxRate.getRecoveryRate())
            .minThreshold(taxRate.getMinThreshold())
            .maxThreshold(taxRate.getMaxThreshold())
            .status(mapStatus(taxRate.getStatus()))
            .createdBy(taxRate.getCreatedBy())
            .approvedBy(taxRate.getApprovedBy())
            .approvedAt(taxRate.getApprovedAt())
            .version(taxRate.getVersion())
            .notes(taxRate.getNotes())
            .createdAt(taxRate.getCreatedAt())
            .updatedAt(taxRate.getUpdatedAt())
            .build();
    }

    private TaxRateResponseDto.JurisdictionDto mapJurisdiction(TaxRate.Jurisdiction jurisdiction) {
        return jurisdiction != null ? TaxRateResponseDto.JurisdictionDto.valueOf(jurisdiction.name()) : null;
    }

    private TaxRateResponseDto.TaxTypeDto mapTaxType(TaxRate.TaxType taxType) {
        return taxType != null ? TaxRateResponseDto.TaxTypeDto.valueOf(taxType.name()) : null;
    }

    private TaxRateResponseDto.TaxRateStatusDto mapStatus(TaxRate.TaxRateStatus status) {
        return status != null ? TaxRateResponseDto.TaxRateStatusDto.valueOf(status.name()) : null;
    }

    // Request DTOs
    @lombok.Data
    public static class CreateTaxRateRequestDto {
        private TaxRate.Jurisdiction jurisdiction;
        private TaxRate.TaxType taxType;
        private String taxCode;
        private BigDecimal ratePercentage;
        private LocalDate effectiveDate;
        private LocalDate expiryDate;
        private String description;
        private Boolean isCompound;
        private Boolean isRecoverable;
        private BigDecimal recoveryRate;
        private BigDecimal minThreshold;
        private BigDecimal maxThreshold;
        private String notes;
    }

    @lombok.Data
    public static class UpdateTaxRateRequestDto {
        private BigDecimal newRate;
        private String description;
        private LocalDate newExpiryDate;
        private Boolean isCompound;
        private Boolean isRecoverable;
        private BigDecimal recoveryRate;
        private String notes;
    }

    @lombok.Data
    public static class NewVersionRequestDto {
        private BigDecimal newRate;
        private LocalDate newEffectiveDate;
    }
}
