package com.gogidix.finance.generalledger.interfaces.rest;

import com.gogidix.finance.generalledger.application.dto.response.JournalEntryResponseDto;
import com.gogidix.finance.generalledger.application.service.JournalEntryCommandService;
import com.gogidix.finance.generalledger.application.service.JournalEntryQueryService;
import com.gogidix.finance.generalledger.shared.requestcontext.RequestContextHolder;
import com.gogidix.finance.ledger.domain.model.JournalEntry;
import com.gogidix.finance.ledger.domain.port.in.JournalEntryCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * Journal Entry REST Controller
 * Handles HTTP requests for journal entry operations
 */
@RestController
@RequestMapping("/api/journal-entries")
@RequiredArgsConstructor
@Tag(name = "Journal Entries", description = "Journal entry management endpoints")
public class JournalEntryController {

    private final JournalEntryCommandService journalEntryCommandService;
    private final JournalEntryQueryService journalEntryQueryService;

    @PostMapping
    @Operation(summary = "Create a new journal entry")
    public ResponseEntity<JournalEntryResponseDto> createJournalEntry(
            @Valid @RequestBody CreateJournalEntryRequestDto request) {

        JournalEntryCommand.CreateJournalEntryCommand command = new JournalEntryCommand.CreateJournalEntryCommand();
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setEntryDate(request.getEntryDate());
        command.setDescription(request.getDescription());
        command.setCurrency(request.getCurrency());
        command.setCreatedBy(RequestContextHolder.getUserId().orElse("system"));
        command.setCreatedByName(request.getCreatedByName());
        command.setReference(request.getReference());
        command.setSourceDocumentType(request.getSourceDocumentType());
        command.setSourceDocumentId(request.getSourceDocumentId());
        command.setSourceModule(request.getSourceModule());
        command.setPeriodId(request.getPeriodId());
        command.setFiscalYear(request.getFiscalYear());
        command.setFiscalPeriod(request.getFiscalPeriod());
        command.setRequiresApproval(request.getRequiresApproval());
        command.setNotes(request.getNotes());
        command.setBatchId(request.getBatchId());
        command.setExchangeRate(request.getExchangeRate());
        command.setBaseCurrency(request.getBaseCurrency());
        command.setLines(request.getLines());

        JournalEntry entry = journalEntryCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(entry));
    }

    @GetMapping("/{journalEntryId}")
    @Operation(summary = "Get journal entry by ID")
    public ResponseEntity<JournalEntryResponseDto> getJournalEntry(
            @Parameter(description = "Journal Entry ID") @PathVariable String journalEntryId) {

        JournalEntry entry = journalEntryQueryService.getById(journalEntryId);
        return ResponseEntity.ok(toDto(entry));
    }

    @GetMapping("/number/{entryNumber}")
    @Operation(summary = "Get journal entry by entry number")
    public ResponseEntity<JournalEntryResponseDto> getByEntryNumber(
            @Parameter(description = "Entry Number") @PathVariable String entryNumber) {

        JournalEntry entry = journalEntryQueryService.getByEntryNumber(entryNumber);
        return ResponseEntity.ok(toDto(entry));
    }

    @GetMapping
    @Operation(summary = "Get all journal entries for tenant")
    public ResponseEntity<List<JournalEntryResponseDto>> getAllJournalEntries() {
        List<JournalEntry> entries = journalEntryQueryService.getAllForTenant();
        return ResponseEntity.ok(entries.stream().map(this::toDto).toList());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get journal entries by status")
    public ResponseEntity<List<JournalEntryResponseDto>> getByStatus(
            @Parameter(description = "Status") @PathVariable String status) {

        List<JournalEntry> entries = journalEntryQueryService.getByStatus(status);
        return ResponseEntity.ok(entries.stream().map(this::toDto).toList());
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get journal entries by date range")
    public ResponseEntity<List<JournalEntryResponseDto>> getByDateRange(
            @Parameter(description = "Start Date") @RequestParam LocalDate startDate,
            @Parameter(description = "End Date") @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        var entries = journalEntryQueryService.getByDateRange(startDate, endDate, page, size);
        return ResponseEntity.ok(entries.stream().map(this::toDto).toList());
    }

    @PutMapping("/{journalEntryId}")
    @Operation(summary = "Update journal entry")
    public ResponseEntity<JournalEntryResponseDto> updateJournalEntry(
            @Parameter(description = "Journal Entry ID") @PathVariable String journalEntryId,
            @Valid @RequestBody UpdateJournalEntryRequestDto request) {

        JournalEntryCommand.UpdateJournalEntryCommand command = new JournalEntryCommand.UpdateJournalEntryCommand();
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setJournalEntryId(journalEntryId);
        command.setEntryDate(request.getEntryDate());
        command.setDescription(request.getDescription());
        command.setReference(request.getReference());
        command.setNotes(request.getNotes());
        command.setLines(request.getLines());

        JournalEntry entry = journalEntryCommandService.update(command);
        return ResponseEntity.ok(toDto(entry));
    }

    @PostMapping("/{journalEntryId}/lines")
    @Operation(summary = "Add line to journal entry")
    public ResponseEntity<JournalEntryResponseDto> addLine(
            @Parameter(description = "Journal Entry ID") @PathVariable String journalEntryId,
            @Valid @RequestBody AddLineRequestDto request) {

        JournalEntryCommand.AddLineCommand command = new JournalEntryCommand.AddLineCommand();
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setJournalEntryId(journalEntryId);
        command.setAccountId(request.getAccountId());
        command.setAccountNumber(request.getAccountNumber());
        command.setAccountName(request.getAccountName());
        command.setDebitAmount(request.getDebitAmount());
        command.setCreditAmount(request.getCreditAmount());
        command.setDescription(request.getDescription());
        command.setCostCenter(request.getCostCenter());
        command.setDepartment(request.getDepartment());

        journalEntryCommandService.addLine(command);

        JournalEntry entry = journalEntryQueryService.getById(journalEntryId);
        return ResponseEntity.ok(toDto(entry));
    }

    @PostMapping("/{journalEntryId}/submit")
    @Operation(summary = "Submit journal entry for approval")
    public ResponseEntity<Void> submitForApproval(
            @Parameter(description = "Journal Entry ID") @PathVariable String journalEntryId) {

        JournalEntryCommand.SubmitForApprovalCommand command = new JournalEntryCommand.SubmitForApprovalCommand(
            RequestContextHolder.getTenantId(), journalEntryId);

        journalEntryCommandService.submitForApproval(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{journalEntryId}/approve")
    @Operation(summary = "Approve journal entry")
    public ResponseEntity<Void> approveJournalEntry(
            @Parameter(description = "Journal Entry ID") @PathVariable String journalEntryId) {

        JournalEntryCommand.ApproveJournalEntryCommand command = new JournalEntryCommand.ApproveJournalEntryCommand(
            RequestContextHolder.getTenantId(), journalEntryId, RequestContextHolder.getUserId().orElse("system"));

        journalEntryCommandService.approve(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{journalEntryId}/post")
    @Operation(summary = "Post journal entry")
    public ResponseEntity<Void> postJournalEntry(
            @Parameter(description = "Journal Entry ID") @PathVariable String journalEntryId) {

        JournalEntryCommand.PostJournalEntryCommand command = new JournalEntryCommand.PostJournalEntryCommand(
            RequestContextHolder.getTenantId(), journalEntryId, RequestContextHolder.getUserId().orElse("system"));

        journalEntryCommandService.post(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{journalEntryId}/reverse")
    @Operation(summary = "Reverse journal entry")
    public ResponseEntity<JournalEntryResponseDto> reverseJournalEntry(
            @Parameter(description = "Journal Entry ID") @PathVariable String journalEntryId,
            @Valid @RequestBody ReverseRequestDto request) {

        JournalEntryCommand.ReverseJournalEntryCommand command = new JournalEntryCommand.ReverseJournalEntryCommand(
            RequestContextHolder.getTenantId(), journalEntryId, request.getReversalReason(),
            RequestContextHolder.getUserId().orElse("system"), request.getReversalDate());

        JournalEntry reversingEntry = journalEntryCommandService.reverse(command);
        return ResponseEntity.ok(toDto(reversingEntry));
    }

    @PostMapping("/{journalEntryId}/cancel")
    @Operation(summary = "Cancel journal entry")
    public ResponseEntity<Void> cancelJournalEntry(
            @Parameter(description = "Journal Entry ID") @PathVariable String journalEntryId,
            @Valid @RequestBody CancelRequestDto request) {

        JournalEntryCommand.CancelJournalEntryCommand command = new JournalEntryCommand.CancelJournalEntryCommand(
            RequestContextHolder.getTenantId(), journalEntryId, request.getReason());

        journalEntryCommandService.cancel(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{journalEntryId}")
    @Operation(summary = "Delete journal entry")
    public ResponseEntity<Void> deleteJournalEntry(
            @Parameter(description = "Journal Entry ID") @PathVariable String journalEntryId) {

        JournalEntryCommand.DeleteJournalEntryCommand command = new JournalEntryCommand.DeleteJournalEntryCommand(
            RequestContextHolder.getTenantId(), journalEntryId);

        journalEntryCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    @Operation(summary = "Get journal entry summary")
    public ResponseEntity<JournalEntryQueryService.JournalEntrySummary> getSummary(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        JournalEntryQueryService.JournalEntrySummary summary =
            journalEntryQueryService.getSummary(startDate, endDate);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Get journal entries for an account")
    public ResponseEntity<List<JournalEntryResponseDto>> getByAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        List<JournalEntry> entries = journalEntryQueryService.getByAccount(
            accountId, startDate, endDate);

        return ResponseEntity.ok(entries.stream().map(this::toDto).toList());
    }

    private JournalEntryResponseDto toDto(JournalEntry entry) {
        return JournalEntryResponseDto.builder()
            .id(entry.getId())
            .journalEntryId(entry.getJournalEntryId())
            .tenantId(entry.getTenantId())
            .entryNumber(entry.getEntryNumber())
            .entryDate(entry.getEntryDate())
            .postingDate(toInstant(entry.getPostingDate()))
            .status(mapStatus(entry.getStatus()))
            .description(entry.getDescription())
            .reference(entry.getReference())
            .sourceDocumentType(entry.getSourceDocumentType())
            .sourceDocumentId(entry.getSourceDocumentId())
            .sourceModule(entry.getSourceModule())
            .periodId(entry.getPeriodId())
            .fiscalYear(entry.getFiscalYear())
            .fiscalPeriod(entry.getFiscalPeriod())
            .createdByUserId(entry.getCreatedByUserId())
            .createdByName(entry.getCreatedByName())
            .approvedByUserId(entry.getApprovedByUserId())
            .approvedAt(toInstant(entry.getApprovedAt()))
            .postedByUserId(entry.getPostedByUserId())
            .postedAt(toInstant(entry.getPostedAt()))
            .totalDebit(entry.getTotalDebit())
            .totalCredit(entry.getTotalCredit())
            .currency(entry.getCurrency())
            .exchangeRate(entry.getExchangeRate())
            .baseCurrency(entry.getBaseCurrency())
            .isReversed(entry.getIsReversed())
            .reversedByEntryId(entry.getReversedByEntryId())
            .reversalDate(toInstant(entry.getReversalDate()))
            .lines(entry.getLines().stream().map(this::mapLine).toList())
            .attachmentUrls(entry.getAttachmentUrls())
            .notes(entry.getNotes())
            .batchId(entry.getBatchId())
            .recurrenceId(entry.getRecurrenceId())
            .isRecurring(entry.getIsRecurring())
            .createdAt(entry.getCreatedAt())
            .updatedAt(entry.getUpdatedAt())
            .build();
    }

    private JournalEntryResponseDto.JournalEntryLineDto mapLine(JournalEntry.JournalEntryLine line) {
        return JournalEntryResponseDto.JournalEntryLineDto.builder()
            .lineId(line.getLineId())
            .accountId(line.getAccountId())
            .accountNumber(line.getAccountNumber())
            .accountName(line.getAccountName())
            .debitAmount(line.getDebitAmount())
            .creditAmount(line.getCreditAmount())
            .description(line.getDescription())
            .costCenter(line.getCostCenter())
            .department(line.getDepartment())
            .projectId(line.getProjectId())
            .taskId(line.getTaskId())
            .reference(line.getReference())
            .taxCode(line.getTaxCode())
            .taxRate(line.getTaxRate())
            .taxAmount(line.getTaxAmount())
            .isTaxInclusive(line.getIsTaxInclusive())
            .tags(line.getTags())
            .sequenceNumber(line.getSequenceNumber())
            .build();
    }

    private JournalEntryResponseDto.JournalEntryStatusDto mapStatus(JournalEntry.JournalEntryStatus status) {
        return status != null ? JournalEntryResponseDto.JournalEntryStatusDto.valueOf(status.name()) : null;
    }

    private Instant toInstant(java.time.LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atZone(ZoneId.systemDefault()).toInstant() : null;
    }

    // Request DTOs
    @lombok.Data
    public static class CreateJournalEntryRequestDto {
        public LocalDate entryDate;
        public String description;
        public String currency;
        public String createdByName;
        public String reference;
        public String sourceDocumentType;
        public String sourceDocumentId;
        public String sourceModule;
        public String periodId;
        public Integer fiscalYear;
        public Integer fiscalPeriod;
        public Boolean requiresApproval;
        public String notes;
        public String batchId;
        public BigDecimal exchangeRate;
        public String baseCurrency;
        public List<JournalEntryCommand.JournalEntryLineDto> lines;
    }

    @lombok.Data
    public static class UpdateJournalEntryRequestDto {
        public LocalDate entryDate;
        public String description;
        public String reference;
        public String notes;
        public List<JournalEntryCommand.JournalEntryLineDto> lines;
    }

    @lombok.Data
    public static class AddLineRequestDto {
        public String accountId;
        public String accountNumber;
        public String accountName;
        public BigDecimal debitAmount;
        public BigDecimal creditAmount;
        public String description;
        public String costCenter;
        public String department;
    }

    @lombok.Data
    public static class ReverseRequestDto {
        public String reversalReason;
        public LocalDate reversalDate;
    }

    @lombok.Data
    public static class CancelRequestDto {
        public String reason;
    }
}
