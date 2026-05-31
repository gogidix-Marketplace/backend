package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.application.dto.response.TaxFilingResponseDto;
import com.gogidix.finance.tax.application.service.TaxFilingQueryService;
import com.gogidix.finance.tax.application.service.TaxFilingService;
import com.gogidix.finance.tax.domain.model.TaxFiling;
import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.port.in.TaxFilingCommand;
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

/**
 * Tax Filing REST Controller
 * Handles HTTP requests for tax filing operations
 */
@RestController
@RequestMapping("/tax-filings")
@RequiredArgsConstructor
@Tag(name = "Tax Filings", description = "Tax filing management endpoints")
public class TaxFilingController {

    private final TaxFilingService taxFilingService;
    private final TaxFilingQueryService taxFilingQueryService;

    @PostMapping
    @Operation(summary = "Create a new tax filing")
    public ResponseEntity<TaxFilingResponseDto> createFiling(@Valid @RequestBody CreateFilingRequestDto request) {
        TaxFilingCommand.CreateFilingCommand command = new TaxFilingCommand.CreateFilingCommand(
            RequestContextHolder.getTenantId(),
            request.getFilingPeriod(),
            request.getFilingType(),
            request.getJurisdiction(),
            request.getTaxType(),
            request.getCurrency(),
            request.getDueDate(),
            RequestContextHolder.getUserIdOrDefault()
        );

        TaxFiling filing = taxFilingService.createFiling(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(filing));
    }

    @PostMapping("/auto-generate")
    @Operation(summary = "Auto-generate tax filing for a period")
    public ResponseEntity<TaxFilingResponseDto> autoGenerateFiling(
            @Valid @RequestBody AutoGenerateFilingRequestDto request) {
        TaxFilingCommand.AutoGenerateFilingCommand command = new TaxFilingCommand.AutoGenerateFilingCommand(
            RequestContextHolder.getTenantId(),
            request.getFilingPeriod(),
            request.getJurisdiction(),
            request.getTaxType(),
            request.getFilingType(),
            RequestContextHolder.getUserIdOrDefault()
        );

        TaxFiling filing = taxFilingService.autoGenerateFiling(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(filing));
    }

    @GetMapping("/{filingId}")
    @Operation(summary = "Get tax filing by ID")
    public ResponseEntity<TaxFilingResponseDto> getFiling(
            @Parameter(description = "Filing ID") @PathVariable String filingId) {
        TaxFiling filing = taxFilingQueryService.getById(filingId);
        return ResponseEntity.ok(toDto(filing));
    }

    @GetMapping
    @Operation(summary = "Get tax filings by period")
    public ResponseEntity<List<TaxFilingResponseDto>> getByPeriod(
            @Parameter(description = "Period (yyyy-MM)") @RequestParam String period,
            @Parameter(description = "Jurisdiction") @RequestParam(required = false) String jurisdiction,
            @Parameter(description = "Tax Type") @RequestParam(required = false) String taxType) {
        List<TaxFiling> filings = taxFilingQueryService.getByPeriod(
            YearMonth.parse(period), jurisdiction, taxType);
        return ResponseEntity.ok(filings.stream().map(this::toDto).toList());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get filings by status")
    public ResponseEntity<List<TaxFilingResponseDto>> getByStatus(
            @Parameter(description = "Status") @PathVariable String status) {
        List<TaxFiling> filings = taxFilingQueryService.getByStatus(status);
        return ResponseEntity.ok(filings.stream().map(this::toDto).toList());
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending filings")
    public ResponseEntity<List<TaxFilingResponseDto>> getPendingFilings(
            @Parameter(description = "Due Before") @RequestParam(required = false) String dueBefore,
            @Parameter(description = "Jurisdiction") @RequestParam(required = false) String jurisdiction) {
        LocalDate dueDate = dueBefore != null ? LocalDate.parse(dueBefore) : null;
        List<TaxFiling> filings = taxFilingQueryService.getPendingFilings(dueDate, jurisdiction);
        return ResponseEntity.ok(filings.stream().map(this::toDto).toList());
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue filings")
    public ResponseEntity<List<TaxFilingResponseDto>> getOverdueFilings() {
        List<TaxFiling> filings = taxFilingQueryService.getOverdueFilings();
        return ResponseEntity.ok(filings.stream().map(this::toDto).toList());
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming filings")
    public ResponseEntity<List<TaxFilingResponseDto>> getUpcomingFilings(
            @Parameter(description = "Days Ahead") @RequestParam(defaultValue = "30") int daysAhead) {
        List<TaxFiling> filings = taxFilingQueryService.getUpcomingFilings(daysAhead);
        return ResponseEntity.ok(filings.stream().map(this::toDto).toList());
    }

    @GetMapping("/summary")
    @Operation(summary = "Get filing summary")
    public ResponseEntity<TaxFilingQueryService.FilingSummary> getFilingSummary(
            @Parameter(description = "Period (yyyy-MM)") @RequestParam String period,
            @Parameter(description = "Jurisdiction") @RequestParam(required = false) String jurisdiction,
            @Parameter(description = "Tax Type") @RequestParam(required = false) String taxType) {
        TaxFilingQueryService.FilingSummary summary = taxFilingQueryService.getFilingSummary(
            YearMonth.parse(period), jurisdiction, taxType);
        return ResponseEntity.ok(summary);
    }

    @PutMapping("/{filingId}/figures")
    @Operation(summary = "Update filing figures")
    public ResponseEntity<TaxFilingResponseDto> updateFilingFigures(
            @Parameter(description = "Filing ID") @PathVariable String filingId,
            @RequestBody UpdateFiguresRequestDto request) {
        TaxFilingCommand.UpdateFilingFiguresCommand command = new TaxFilingCommand.UpdateFilingFiguresCommand(
            RequestContextHolder.getTenantId(),
            filingId,
            request.getGrossSales(),
            request.getTaxableSales(),
            request.getExemptSales(),
            request.getTotalTaxCollected(),
            request.getTotalTaxPaid()
        );

        TaxFiling filing = taxFilingService.updateFilingFigures(command);
        return ResponseEntity.ok(toDto(filing));
    }

    @PostMapping("/{filingId}/submit-for-review")
    @Operation(summary = "Submit filing for review")
    public ResponseEntity<Void> submitForReview(
            @Parameter(description = "Filing ID") @PathVariable String filingId) {
        TaxFilingCommand.SubmitForReviewCommand command = new TaxFilingCommand.SubmitForReviewCommand(
            RequestContextHolder.getTenantId(),
            filingId
        );

        taxFilingService.submitForReview(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{filingId}/submit")
    @Operation(summary = "Submit filing")
    public ResponseEntity<Void> submitFiling(
            @Parameter(description = "Filing ID") @PathVariable String filingId) {
        TaxFilingCommand.SubmitFilingCommand command = new TaxFilingCommand.SubmitFilingCommand(
            RequestContextHolder.getTenantId(),
            filingId
        );

        taxFilingService.submitFiling(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{filingId}/acknowledge")
    @Operation(summary = "Acknowledge filing")
    public ResponseEntity<Void> acknowledgeFiling(
            @Parameter(description = "Filing ID") @PathVariable String filingId,
            @RequestBody AcknowledgeRequestDto request) {
        TaxFilingCommand.AcknowledgeFilingCommand command = new TaxFilingCommand.AcknowledgeFilingCommand(
            RequestContextHolder.getTenantId(),
            filingId,
            request.getAcknowledgementNumber()
        );

        taxFilingService.acknowledgeFiling(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{filingId}/accept")
    @Operation(summary = "Accept filing")
    public ResponseEntity<Void> acceptFiling(
            @Parameter(description = "Filing ID") @PathVariable String filingId) {
        TaxFilingCommand.AcceptFilingCommand command = new TaxFilingCommand.AcceptFilingCommand(
            RequestContextHolder.getTenantId(),
            filingId,
            RequestContextHolder.getUserIdOrDefault()
        );

        taxFilingService.acceptFiling(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{filingId}/reject")
    @Operation(summary = "Reject filing")
    public ResponseEntity<Void> rejectFiling(
            @Parameter(description = "Filing ID") @PathVariable String filingId,
            @RequestBody RejectRequestDto request) {
        TaxFilingCommand.RejectFilingCommand command = new TaxFilingCommand.RejectFilingCommand(
            RequestContextHolder.getTenantId(),
            filingId,
            request.getReason()
        );

        taxFilingService.rejectFiling(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{filingId}/adjustments")
    @Operation(summary = "Add adjustment to filing")
    public ResponseEntity<Void> addAdjustment(
            @Parameter(description = "Filing ID") @PathVariable String filingId,
            @RequestBody AdjustmentRequestDto request) {
        TaxFilingCommand.AddAdjustmentCommand command = new TaxFilingCommand.AddAdjustmentCommand(
            RequestContextHolder.getTenantId(),
            filingId,
            request.getAdjustmentType(),
            request.getAmount(),
            request.getReason(),
            request.getReference()
        );

        taxFilingService.addAdjustment(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{filingId}/calculations/{calculationId}")
    @Operation(summary = "Link calculation to filing")
    public ResponseEntity<Void> linkCalculation(
            @Parameter(description = "Filing ID") @PathVariable String filingId,
            @Parameter(description = "Calculation ID") @PathVariable String calculationId) {
        TaxFilingCommand.LinkCalculationCommand command = new TaxFilingCommand.LinkCalculationCommand(
            RequestContextHolder.getTenantId(),
            filingId,
            calculationId
        );

        taxFilingService.linkCalculation(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{filingId}/pay")
    @Operation(summary = "Mark filing as paid")
    public ResponseEntity<Void> markAsPaid(
            @Parameter(description = "Filing ID") @PathVariable String filingId,
            @RequestBody PaymentRequestDto request) {
        TaxFilingCommand.MarkAsPaidCommand command = new TaxFilingCommand.MarkAsPaidCommand(
            RequestContextHolder.getTenantId(),
            filingId,
            request.getPaymentReference()
        );

        taxFilingService.markAsPaid(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{filingId}/archive")
    @Operation(summary = "Archive filing")
    public ResponseEntity<Void> archiveFiling(
            @Parameter(description = "Filing ID") @PathVariable String filingId) {
        TaxFilingCommand.ArchiveFilingCommand command = new TaxFilingCommand.ArchiveFilingCommand(
            RequestContextHolder.getTenantId(),
            filingId
        );

        taxFilingService.archiveFiling(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{filingId}/cancel")
    @Operation(summary = "Cancel filing")
    public ResponseEntity<Void> cancelFiling(
            @Parameter(description = "Filing ID") @PathVariable String filingId,
            @RequestBody CancelRequestDto request) {
        TaxFilingCommand.CancelFilingCommand command = new TaxFilingCommand.CancelFilingCommand(
            RequestContextHolder.getTenantId(),
            filingId,
            request.getReason()
        );

        taxFilingService.cancelFiling(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{filingId}/attachments")
    @Operation(summary = "Add attachment to filing")
    public ResponseEntity<Void> addAttachment(
            @Parameter(description = "Filing ID") @PathVariable String filingId,
            @RequestBody AttachmentRequestDto request) {
        TaxFilingCommand.AddAttachmentCommand command = new TaxFilingCommand.AddAttachmentCommand(
            RequestContextHolder.getTenantId(),
            filingId,
            request.getFileName(),
            request.getFileType(),
            request.getFileSize(),
            request.getStorageLocation(),
            request.getUrl(),
            RequestContextHolder.getUserIdOrDefault()
        );

        taxFilingService.addAttachment(command);
        return ResponseEntity.ok().build();
    }

    private TaxFilingResponseDto toDto(TaxFiling filing) {
        return TaxFilingResponseDto.builder()
            .id(filing.getId())
            .filingId(filing.getFilingId())
            .tenantId(filing.getTenantId())
            .filingPeriod(filing.getFilingPeriod())
            .filingType(mapFilingType(filing.getFilingType()))
            .jurisdiction(mapJurisdiction(filing.getJurisdiction()))
            .taxType(mapTaxType(filing.getTaxType()))
            .currency(filing.getCurrency())
            .grossSales(filing.getGrossSales())
            .taxableSales(filing.getTaxableSales())
            .exemptSales(filing.getExemptSales())
            .totalTaxCollected(filing.getTotalTaxCollected())
            .totalTaxPaid(filing.getTotalTaxPaid())
            .taxDue(filing.getTaxDue())
            .taxRefund(filing.getTaxRefund())
            .penalty(filing.getPenalty())
            .interest(filing.getInterest())
            .netAmount(filing.getNetAmount())
            .status(mapFilingStatus(filing.getStatus()))
            .submissionDate(filing.getSubmissionDate())
            .dueDate(filing.getDueDate())
            .filingDate(filing.getFilingDate())
            .acknowledgementDate(filing.getAcknowledgementDate())
            .acknowledgementNumber(filing.getAcknowledgementNumber())
            .submittedBy(filing.getSubmittedBy())
            .approvedBy(filing.getApprovedBy())
            .approvedAt(filing.getApprovedAt())
            .calculationIds(filing.getCalculationIds())
            .adjustments(filing.getAdjustments().stream().map(this::mapAdjustment).toList())
            .attachments(filing.getAttachments().stream().map(this::mapAttachment).toList())
            .notes(filing.getNotes())
            .internalNotes(filing.getInternalNotes())
            .paymentReference(filing.getPaymentReference())
            .paymentDate(filing.getPaymentDate())
            .metadata(filing.getMetadata())
            .isOverdue(filing.isOverdue())
            .createdAt(filing.getCreatedAt())
            .updatedAt(filing.getUpdatedAt())
            .build();
    }

    private TaxFilingResponseDto.FilingTypeDto mapFilingType(TaxFiling.FilingType type) {
        return type != null ? TaxFilingResponseDto.FilingTypeDto.valueOf(type.name()) : null;
    }

    private TaxFilingResponseDto.JurisdictionDto mapJurisdiction(TaxRate.Jurisdiction jurisdiction) {
        return jurisdiction != null ? TaxFilingResponseDto.JurisdictionDto.valueOf(jurisdiction.name()) : null;
    }

    private TaxFilingResponseDto.TaxTypeDto mapTaxType(TaxRate.TaxType taxType) {
        return taxType != null ? TaxFilingResponseDto.TaxTypeDto.valueOf(taxType.name()) : null;
    }

    private TaxFilingResponseDto.FilingStatusDto mapFilingStatus(TaxFiling.FilingStatus status) {
        return status != null ? TaxFilingResponseDto.FilingStatusDto.valueOf(status.name()) : null;
    }

    private TaxFilingResponseDto.AdjustmentDto mapAdjustment(TaxFiling.Adjustment adjustment) {
        return TaxFilingResponseDto.AdjustmentDto.builder()
            .adjustmentId(adjustment.getAdjustmentId())
            .adjustmentType(adjustment.getAdjustmentType())
            .amount(adjustment.getAmount())
            .reason(adjustment.getReason())
            .reference(adjustment.getReference())
            .adjustmentDate(adjustment.getAdjustmentDate())
            .build();
    }

    private TaxFilingResponseDto.AttachmentDto mapAttachment(TaxFiling.Attachment attachment) {
        return TaxFilingResponseDto.AttachmentDto.builder()
            .attachmentId(attachment.getAttachmentId())
            .fileName(attachment.getFileName())
            .fileType(attachment.getFileType())
            .fileSize(attachment.getFileSize())
            .storageLocation(attachment.getStorageLocation())
            .url(attachment.getUrl())
            .uploadedAt(attachment.getUploadedAt())
            .uploadedBy(attachment.getUploadedBy())
            .build();
    }

    // Request DTOs
    @lombok.Data
    public static class CreateFilingRequestDto {
        private YearMonth filingPeriod;
        private TaxFiling.FilingType filingType;
        private TaxRate.Jurisdiction jurisdiction;
        private TaxRate.TaxType taxType;
        private String currency;
        private LocalDate dueDate;
    }

    @lombok.Data
    public static class AutoGenerateFilingRequestDto {
        private YearMonth filingPeriod;
        private TaxRate.Jurisdiction jurisdiction;
        private TaxRate.TaxType taxType;
        private TaxFiling.FilingType filingType;
    }

    @lombok.Data
    public static class UpdateFiguresRequestDto {
        private BigDecimal grossSales;
        private BigDecimal taxableSales;
        private BigDecimal exemptSales;
        private BigDecimal totalTaxCollected;
        private BigDecimal totalTaxPaid;
    }

    @lombok.Data
    public static class AcknowledgeRequestDto {
        private String acknowledgementNumber;
    }

    @lombok.Data
    public static class RejectRequestDto {
        private String reason;
    }

    @lombok.Data
    public static class AdjustmentRequestDto {
        private String adjustmentType;
        private BigDecimal amount;
        private String reason;
        private String reference;
    }

    @lombok.Data
    public static class PaymentRequestDto {
        private String paymentReference;
    }

    @lombok.Data
    public static class CancelRequestDto {
        private String reason;
    }

    @lombok.Data
    public static class AttachmentRequestDto {
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String storageLocation;
        private String url;
    }
}
