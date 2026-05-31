package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.application.dto.response.TaxCalculationResponseDto;
import com.gogidix.finance.tax.application.service.TaxCalculationQueryService;
import com.gogidix.finance.tax.application.service.TaxCalculationService;
import com.gogidix.finance.tax.domain.model.TaxCalculation;
import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.port.in.TaxCalculationCommand;
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
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * Tax Calculation REST Controller
 * Handles HTTP requests for tax calculation operations
 */
@RestController
@RequestMapping("/tax-calculations")
@RequiredArgsConstructor
@Tag(name = "Tax Calculations", description = "Tax calculation management endpoints")
public class TaxCalculationController {

    private final TaxCalculationService taxCalculationService;
    private final TaxCalculationQueryService taxCalculationQueryService;

    @PostMapping
    @Operation(summary = "Create a new tax calculation")
    public ResponseEntity<TaxCalculationResponseDto> createCalculation(
            @Valid @RequestBody CreateCalculationRequestDto request) {
        TaxCalculationCommand.CreateCalculationCommand command = new TaxCalculationCommand.CreateCalculationCommand(
            RequestContextHolder.getTenantId(),
            request.getTransactionId(),
            request.getTransactionType(),
            request.getTransactionDate(),
            request.getJurisdiction(),
            request.getCurrency(),
            request.getBaseAmount(),
            RequestContextHolder.getUserIdOrDefault(),
            request.getContext()
        );

        TaxCalculation calculation = taxCalculationService.createCalculation(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(calculation));
    }

    @PostMapping("/calculate")
    @Operation(summary = "Calculate tax for a transaction")
    public ResponseEntity<TaxCalculationResponseDto> calculateTax(
            @Valid @RequestBody CalculateTaxRequestDto request) {
        TaxCalculationCommand.CalculateTaxCommand command = new TaxCalculationCommand.CalculateTaxCommand(
            RequestContextHolder.getTenantId(),
            request.getTransactionId(),
            request.getTransactionType(),
            request.getTransactionDate(),
            request.getJurisdiction(),
            request.getTaxType(),
            request.getCurrency(),
            request.getAmount(),
            RequestContextHolder.getUserIdOrDefault(),
            request.getCategory(),
            request.getEntityCode(),
            request.getAdditionalContext()
        );

        TaxCalculation calculation = taxCalculationService.calculateTax(command);
        return ResponseEntity.ok(toDto(calculation));
    }

    @PostMapping("/batch-calculate")
    @Operation(summary = "Batch calculate tax for multiple transactions")
    public ResponseEntity<List<TaxCalculationResponseDto>> batchCalculate(
            @Valid @RequestBody BatchCalculateRequestDto request) {
        TaxCalculationCommand.BatchCalculateCommand command = new TaxCalculationCommand.BatchCalculateCommand(
            RequestContextHolder.getTenantId(),
            request.getTransactions(),
            RequestContextHolder.getUserIdOrDefault()
        );

        List<TaxCalculation> calculations = taxCalculationService.batchCalculate(command);
        return ResponseEntity.ok(calculations.stream().map(this::toDto).toList());
    }

    @GetMapping("/{calculationId}")
    @Operation(summary = "Get tax calculation by ID")
    public ResponseEntity<TaxCalculationResponseDto> getCalculation(
            @Parameter(description = "Calculation ID") @PathVariable String calculationId) {
        TaxCalculation calculation = taxCalculationQueryService.getById(calculationId);
        return ResponseEntity.ok(toDto(calculation));
    }

    @GetMapping("/transaction/{transactionId}")
    @Operation(summary = "Get calculations by transaction ID")
    public ResponseEntity<List<TaxCalculationResponseDto>> getByTransactionId(
            @Parameter(description = "Transaction ID") @PathVariable String transactionId) {
        List<TaxCalculation> calculations = taxCalculationQueryService.getByTransactionId(transactionId);
        return ResponseEntity.ok(calculations.stream().map(this::toDto).toList());
    }

    @GetMapping
    @Operation(summary = "Get calculations by date range")
    public ResponseEntity<List<TaxCalculationResponseDto>> getByDateRange(
            @Parameter(description = "Start Date") @RequestParam String startDate,
            @Parameter(description = "End Date") @RequestParam String endDate,
            @Parameter(description = "Status") @RequestParam(required = false) String status) {
        List<TaxCalculation> calculations = taxCalculationQueryService.getByDateRange(
            LocalDate.parse(startDate), LocalDate.parse(endDate), status);
        return ResponseEntity.ok(calculations.stream().map(this::toDto).toList());
    }

    @GetMapping("/period/{period}")
    @Operation(summary = "Get calculations by period")
    public ResponseEntity<List<TaxCalculationResponseDto>> getByPeriod(
            @Parameter(description = "Period (yyyy-MM)") @PathVariable String period,
            @Parameter(description = "Jurisdiction") @RequestParam(required = false) String jurisdiction,
            @Parameter(description = "Tax Type") @RequestParam(required = false) String taxType) {
        List<TaxCalculation> calculations = taxCalculationQueryService.getByPeriod(
            YearMonth.parse(period), jurisdiction, taxType);
        return ResponseEntity.ok(calculations.stream().map(this::toDto).toList());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get calculations by status")
    public ResponseEntity<List<TaxCalculationResponseDto>> getByStatus(
            @Parameter(description = "Status") @PathVariable String status) {
        List<TaxCalculation> calculations = taxCalculationQueryService.getByStatus(status);
        return ResponseEntity.ok(calculations.stream().map(this::toDto).toList());
    }

    @GetMapping("/summary")
    @Operation(summary = "Get tax summary")
    public ResponseEntity<TaxCalculationQueryService.TaxSummary> getSummary(
            @Parameter(description = "Start Date") @RequestParam(required = false) String startDate,
            @Parameter(description = "End Date") @RequestParam(required = false) String endDate,
            @Parameter(description = "Jurisdiction") @RequestParam(required = false) String jurisdiction,
            @Parameter(description = "Tax Type") @RequestParam(required = false) String taxType,
            @Parameter(description = "Currency") @RequestParam(required = false) String currency) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

        TaxCalculationQueryService.TaxSummary summary =
            taxCalculationQueryService.getTaxSummary(start, end, jurisdiction, taxType, currency);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/liability-report")
    @Operation(summary = "Get tax liability report")
    public ResponseEntity<TaxCalculationQueryService.TaxLiabilityReport> getLiabilityReport(
            @Parameter(description = "Period (yyyy-MM)") @RequestParam String period,
            @Parameter(description = "Jurisdiction") @RequestParam(required = false) String jurisdiction,
            @Parameter(description = "Tax Type") @RequestParam(required = false) String taxType,
            @Parameter(description = "Include Pending") @RequestParam(required = false, defaultValue = "true") Boolean includePending,
            @Parameter(description = "Include Verified") @RequestParam(required = false, defaultValue = "true") Boolean includeVerified) {
        TaxCalculationQueryService.TaxLiabilityReport report =
            taxCalculationQueryService.getTaxLiabilityReport(
                YearMonth.parse(period), jurisdiction, taxType, includePending, includeVerified);

        return ResponseEntity.ok(report);
    }

    @PostMapping("/{calculationId}/tax-rates")
    @Operation(summary = "Add tax rate to calculation")
    public ResponseEntity<TaxCalculationResponseDto> addTaxRate(
            @Parameter(description = "Calculation ID") @PathVariable String calculationId,
            @RequestBody AddTaxRateRequestDto request) {
        TaxCalculationCommand.AddTaxRateCommand command = new TaxCalculationCommand.AddTaxRateCommand(
            RequestContextHolder.getTenantId(),
            calculationId,
            request.getTaxCode(),
            request.getTaxType(),
            request.getRate(),
            request.getIsRecoverable(),
            request.getDescription(),
            request.getIsCompound()
        );

        TaxCalculation calculation = taxCalculationService.addTaxRate(command);
        return ResponseEntity.ok(toDto(calculation));
    }

    @PostMapping("/{calculationId}/exemptions")
    @Operation(summary = "Add exemption to calculation")
    public ResponseEntity<TaxCalculationResponseDto> addExemption(
            @Parameter(description = "Calculation ID") @PathVariable String calculationId,
            @RequestBody AddExemptionRequestDto request) {
        TaxCalculationCommand.AddExemptionCommand command = new TaxCalculationCommand.AddExemptionCommand(
            RequestContextHolder.getTenantId(),
            calculationId,
            request.getExemptionCode(),
            request.getExemptionType(),
            request.getAmount(),
            request.getReason(),
            request.getCertificateNumber(),
            request.getCertificateExpiry()
        );

        TaxCalculation calculation = taxCalculationService.addExemption(command);
        return ResponseEntity.ok(toDto(calculation));
    }

    @PostMapping("/{calculationId}/deductions")
    @Operation(summary = "Add deduction to calculation")
    public ResponseEntity<TaxCalculationResponseDto> addDeduction(
            @Parameter(description = "Calculation ID") @PathVariable String calculationId,
            @RequestBody AddDeductionRequestDto request) {
        TaxCalculationCommand.AddDeductionCommand command = new TaxCalculationCommand.AddDeductionCommand(
            RequestContextHolder.getTenantId(),
            calculationId,
            request.getDeductionType(),
            request.getAmount(),
            request.getDescription(),
            request.getReference()
        );

        TaxCalculation calculation = taxCalculationService.addDeduction(command);
        return ResponseEntity.ok(toDto(calculation));
    }

    @PostMapping("/{calculationId}/finalize")
    @Operation(summary = "Finalize calculation")
    public ResponseEntity<TaxCalculationResponseDto> finalizeCalculation(
            @Parameter(description = "Calculation ID") @PathVariable String calculationId) {
        TaxCalculationCommand.FinalizeCalculationCommand command = new TaxCalculationCommand.FinalizeCalculationCommand(
            RequestContextHolder.getTenantId(),
            calculationId
        );

        TaxCalculation calculation = taxCalculationService.finalizeCalculation(command);
        return ResponseEntity.ok(toDto(calculation));
    }

    @PostMapping("/{calculationId}/verify")
    @Operation(summary = "Verify calculation")
    public ResponseEntity<TaxCalculationResponseDto> verifyCalculation(
            @Parameter(description = "Calculation ID") @PathVariable String calculationId) {
        TaxCalculationCommand.VerifyCalculationCommand command = new TaxCalculationCommand.VerifyCalculationCommand(
            RequestContextHolder.getTenantId(),
            calculationId,
            RequestContextHolder.getUserIdOrDefault()
        );

        TaxCalculation calculation = taxCalculationService.verifyCalculation(command);
        return ResponseEntity.ok(toDto(calculation));
    }

    @PostMapping("/{calculationId}/apply")
    @Operation(summary = "Mark calculation as applied")
    public ResponseEntity<Void> markAsApplied(
            @Parameter(description = "Calculation ID") @PathVariable String calculationId) {
        TaxCalculationCommand.MarkAsAppliedCommand command = new TaxCalculationCommand.MarkAsAppliedCommand(
            RequestContextHolder.getTenantId(),
            calculationId
        );

        taxCalculationService.markAsApplied(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{calculationId}/reverse")
    @Operation(summary = "Reverse calculation")
    public ResponseEntity<Void> reverseCalculation(
            @Parameter(description = "Calculation ID") @PathVariable String calculationId,
            @RequestBody ReverseRequestDto request) {
        TaxCalculationCommand.ReverseCalculationCommand command = new TaxCalculationCommand.ReverseCalculationCommand(
            RequestContextHolder.getTenantId(),
            calculationId,
            request.getReason()
        );

        taxCalculationService.reverseCalculation(command);
        return ResponseEntity.ok().build();
    }

    private TaxCalculationResponseDto toDto(TaxCalculation calculation) {
        return TaxCalculationResponseDto.builder()
            .id(calculation.getId())
            .calculationId(calculation.getCalculationId())
            .tenantId(calculation.getTenantId())
            .transactionId(calculation.getTransactionId())
            .transactionType(mapTransactionType(calculation.getTransactionType()))
            .transactionDate(calculation.getTransactionDate())
            .jurisdiction(mapJurisdiction(calculation.getJurisdiction()))
            .currency(calculation.getCurrency())
            .baseAmount(calculation.getBaseAmount())
            .taxableAmount(calculation.getTaxableAmount())
            .totalTax(calculation.getTotalTax())
            .netAmount(calculation.getNetAmount())
            .taxBreakdown(calculation.getTaxBreakdown().stream().map(this::mapTaxLineItem).toList())
            .exemptions(calculation.getExemptions().stream().map(this::mapExemption).toList())
            .deductions(calculation.getDeductions().stream().map(this::mapDeduction).toList())
            .effectiveTaxRate(calculation.getEffectiveTaxRate())
            .calculationMethod(mapCalculationMethod(calculation.getCalculationMethod()))
            .status(mapCalculationStatus(calculation.getStatus()))
            .calculatedAt(calculation.getCalculatedAt())
            .calculatedBy(calculation.getCalculatedBy())
            .verifiedAt(calculation.getVerifiedAt())
            .verifiedBy(calculation.getVerifiedBy())
            .referenceNumber(calculation.getReferenceNumber())
            .notes(calculation.getNotes())
            .context(calculation.getContext())
            .createdAt(calculation.getCreatedAt())
            .updatedAt(calculation.getUpdatedAt())
            .build();
    }

    private TaxCalculationResponseDto.TransactionTypeDto mapTransactionType(TaxCalculation.TransactionType type) {
        return type != null ? TaxCalculationResponseDto.TransactionTypeDto.valueOf(type.name()) : null;
    }

    private TaxCalculationResponseDto.JurisdictionDto mapJurisdiction(TaxRate.Jurisdiction jurisdiction) {
        return jurisdiction != null ? TaxCalculationResponseDto.JurisdictionDto.valueOf(jurisdiction.name()) : null;
    }

    private TaxCalculationResponseDto.CalculationMethodDto mapCalculationMethod(TaxCalculation.CalculationMethod method) {
        return method != null ? TaxCalculationResponseDto.CalculationMethodDto.valueOf(method.name()) : null;
    }

    private TaxCalculationResponseDto.CalculationStatusDto mapCalculationStatus(TaxCalculation.CalculationStatus status) {
        return status != null ? TaxCalculationResponseDto.CalculationStatusDto.valueOf(status.name()) : null;
    }

    private TaxCalculationResponseDto.TaxLineItemDto mapTaxLineItem(TaxCalculation.TaxLineItem item) {
        return TaxCalculationResponseDto.TaxLineItemDto.builder()
            .taxCode(item.getTaxCode())
            .taxType(mapTaxType(item.getTaxType()))
            .rate(item.getRate())
            .baseAmount(item.getBaseAmount())
            .taxAmount(item.getTaxAmount())
            .isRecoverable(item.getIsRecoverable())
            .recoverableAmount(item.getRecoverableAmount())
            .description(item.getDescription())
            .build();
    }

    private TaxCalculationResponseDto.TaxTypeDto mapTaxType(TaxRate.TaxType taxType) {
        return taxType != null ? TaxCalculationResponseDto.TaxTypeDto.valueOf(taxType.name()) : null;
    }

    private TaxCalculationResponseDto.ExemptionDto mapExemption(TaxCalculation.Exemption exemption) {
        return TaxCalculationResponseDto.ExemptionDto.builder()
            .exemptionCode(exemption.getExemptionCode())
            .exemptionType(exemption.getExemptionType())
            .amount(exemption.getAmount())
            .reason(exemption.getReason())
            .certificateNumber(exemption.getCertificateNumber())
            .certificateExpiry(exemption.getCertificateExpiry())
            .build();
    }

    private TaxCalculationResponseDto.DeductionDto mapDeduction(TaxCalculation.Deduction deduction) {
        return TaxCalculationResponseDto.DeductionDto.builder()
            .deductionType(deduction.getDeductionType())
            .amount(deduction.getAmount())
            .description(deduction.getDescription())
            .reference(deduction.getReference())
            .build();
    }

    // Request DTOs
    @lombok.Data
    public static class CreateCalculationRequestDto {
        private String transactionId;
        private TaxCalculation.TransactionType transactionType;
        private LocalDate transactionDate;
        private TaxRate.Jurisdiction jurisdiction;
        private String currency;
        private BigDecimal baseAmount;
        private Map<String, Object> context;
    }

    @lombok.Data
    public static class CalculateTaxRequestDto {
        private String transactionId;
        private TaxCalculation.TransactionType transactionType;
        private LocalDate transactionDate;
        private TaxRate.Jurisdiction jurisdiction;
        private TaxRate.TaxType taxType;
        private String currency;
        private BigDecimal amount;
        private String category;
        private String entityCode;
        private Map<String, Object> additionalContext;
    }

    @lombok.Data
    public static class BatchCalculateRequestDto {
        private List<TaxCalculationCommand.BatchCalculateCommand.TransactionForCalculation> transactions;
    }

    @lombok.Data
    public static class AddTaxRateRequestDto {
        private String taxCode;
        private TaxRate.TaxType taxType;
        private BigDecimal rate;
        private Boolean isRecoverable;
        private String description;
        private Boolean isCompound;
    }

    @lombok.Data
    public static class AddExemptionRequestDto {
        private String exemptionCode;
        private String exemptionType;
        private BigDecimal amount;
        private String reason;
        private String certificateNumber;
        private LocalDate certificateExpiry;
    }

    @lombok.Data
    public static class AddDeductionRequestDto {
        private String deductionType;
        private BigDecimal amount;
        private String description;
        private String reference;
    }

    @lombok.Data
    public static class ReverseRequestDto {
        private String reason;
    }
}
