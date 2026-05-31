package com.gogidix.finance.bankreconciliation.interfaces.rest;

import com.gogidix.finance.bankreconciliation.application.dto.response.ReconciliationLineResponseDto;
import com.gogidix.finance.bankreconciliation.application.dto.response.ReconciliationResponseDto;
import com.gogidix.finance.bankreconciliation.application.dto.response.ReconciliationSummaryResponseDto;
import com.gogidix.finance.bankreconciliation.application.service.BankReconciliationService;
import com.gogidix.finance.bankreconciliation.application.service.ReconciliationCommandService;
import com.gogidix.finance.bankreconciliation.application.service.ReconciliationQueryService;
import com.gogidix.finance.bankreconciliation.domain.model.BankTransaction;
import com.gogidix.finance.bankreconciliation.domain.model.Reconciliation;
import com.gogidix.finance.bankreconciliation.domain.model.ReconciliationLine;
import com.gogidix.finance.bankreconciliation.domain.port.in.ReconciliationCommand;
import com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Reconciliation REST Controller
 * Handles HTTP requests for reconciliation operations
 */
@RestController
@RequestMapping("/reconciliations")
@RequiredArgsConstructor
@Tag(name = "Reconciliations", description = "Bank reconciliation management endpoints")
public class ReconciliationController {

    private final ReconciliationCommandService reconciliationCommandService;
    private final ReconciliationQueryService reconciliationQueryService;
    private final BankReconciliationService bankReconciliationService;

    @PostMapping
    @Operation(summary = "Create a new reconciliation")
    public ResponseEntity<ReconciliationResponseDto> createReconciliation(
            @Valid @RequestBody CreateReconciliationRequestDto request) {

        ReconciliationCommand.CreateReconciliationCommand command = new ReconciliationCommand.CreateReconciliationCommand(
                RequestContextHolder.getTenantId(),
                request.accountId,
                request.accountNumber,
                request.statementId,
                request.reconciliationDate,
                request.periodStart,
                request.periodEnd,
                request.startingBalance,
                request.endingBalance,
                request.tolerance,
                request.reconciliationMethod,
                request.autoReconcile,
                request.notes,
                RequestContextHolder.getUserId()
        );

        Reconciliation reconciliation = reconciliationCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(reconciliation));
    }

    @GetMapping("/{reconciliationId}")
    @Operation(summary = "Get reconciliation by ID")
    public ResponseEntity<ReconciliationResponseDto> getReconciliation(
            @Parameter(description = "Reconciliation ID") @PathVariable String reconciliationId) {
        Reconciliation reconciliation = reconciliationQueryService.getById(reconciliationId);
        return ResponseEntity.ok(toDto(reconciliation));
    }

    @GetMapping
    @Operation(summary = "Get reconciliations for account")
    public ResponseEntity<Page<ReconciliationResponseDto>> getReconciliationsByAccount(
            @RequestParam String accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Reconciliation.ReconciliationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Reconciliation> reconciliations = reconciliationQueryService.getReconciliationsByAccount(
                accountId, startDate, endDate, status, page, size);

        return ResponseEntity.ok(reconciliations.map(this::toDto));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get reconciliations by status")
    public ResponseEntity<Page<ReconciliationResponseDto>> getReconciliationsByStatus(
            @Parameter(description = "Reconciliation Status") @PathVariable Reconciliation.ReconciliationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Reconciliation> reconciliations = reconciliationQueryService.getReconciliationsByStatus(status, page, size);
        return ResponseEntity.ok(reconciliations.map(this::toDto));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending reconciliations")
    public ResponseEntity<List<ReconciliationResponseDto>> getPendingReconciliations() {
        List<Reconciliation> reconciliations = reconciliationQueryService.getPendingReconciliations();
        return ResponseEntity.ok(reconciliations.stream().map(this::toDto).toList());
    }

    @GetMapping("/in-progress")
    @Operation(summary = "Get in-progress reconciliations")
    public ResponseEntity<List<ReconciliationResponseDto>> getInProgressReconciliations() {
        List<Reconciliation> reconciliations = reconciliationQueryService.getInProgressReconciliations();
        return ResponseEntity.ok(reconciliations.stream().map(this::toDto).toList());
    }

    @GetMapping("/awaiting-approval")
    @Operation(summary = "Get reconciliations awaiting approval")
    public ResponseEntity<List<ReconciliationResponseDto>> getAwaitingApprovalReconciliations() {
        List<Reconciliation> reconciliations = reconciliationQueryService.getAwaitingApprovalReconciliations();
        return ResponseEntity.ok(reconciliations.stream().map(this::toDto).toList());
    }

    @GetMapping("/account/{accountId}/latest")
    @Operation(summary = "Get latest reconciliation for account")
    public ResponseEntity<ReconciliationResponseDto> getLatestByAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId) {
        Reconciliation reconciliation = reconciliationQueryService.getLatestByAccount(accountId);
        return ResponseEntity.ok(toDto(reconciliation));
    }

    @PostMapping("/{reconciliationId}/start")
    @Operation(summary = "Start reconciliation process")
    public ResponseEntity<Void> startReconciliation(
            @Parameter(description = "Reconciliation ID") @PathVariable String reconciliationId) {

        ReconciliationCommand.StartReconciliationCommand command = new ReconciliationCommand.StartReconciliationCommand(
                RequestContextHolder.getTenantId(),
                reconciliationId,
                RequestContextHolder.getUserId()
        );

        reconciliationCommandService.start(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{reconciliationId}/complete")
    @Operation(summary = "Complete reconciliation")
    public ResponseEntity<ReconciliationResponseDto> completeReconciliation(
            @Parameter(description = "Reconciliation ID") @PathVariable String reconciliationId,
            @RequestBody CompleteReconciliationRequestDto request) {

        ReconciliationCommand.CompleteReconciliationCommand command = new ReconciliationCommand.CompleteReconciliationCommand(
                RequestContextHolder.getTenantId(),
                reconciliationId,
                RequestContextHolder.getUserId(),
                request.bookBalance,
                request.bankBalance,
                request.notes
        );

        reconciliationCommandService.complete(command);
        Reconciliation reconciliation = reconciliationQueryService.getById(reconciliationId);
        return ResponseEntity.ok(toDto(reconciliation));
    }

    @PostMapping("/{reconciliationId}/approve")
    @Operation(summary = "Approve reconciliation")
    public ResponseEntity<Void> approveReconciliation(
            @Parameter(description = "Reconciliation ID") @PathVariable String reconciliationId,
            @RequestBody ApprovalRequestDto request) {

        ReconciliationCommand.ApproveReconciliationCommand command = new ReconciliationCommand.ApproveReconciliationCommand(
                RequestContextHolder.getTenantId(),
                reconciliationId,
                RequestContextHolder.getUserId(),
                request.notes
        );

        reconciliationCommandService.approve(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reconciliationId}/cancel")
    @Operation(summary = "Cancel reconciliation")
    public ResponseEntity<Void> cancelReconciliation(
            @Parameter(description = "Reconciliation ID") @PathVariable String reconciliationId,
            @RequestBody CancelRequestDto request) {

        ReconciliationCommand.CancelReconciliationCommand command = new ReconciliationCommand.CancelReconciliationCommand(
                RequestContextHolder.getTenantId(),
                reconciliationId,
                RequestContextHolder.getUserId(),
                request.reason
        );

        reconciliationCommandService.cancel(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/initiate")
    @Operation(summary = "Initiate and auto-start reconciliation")
    public ResponseEntity<ReconciliationResponseDto> initiateReconciliation(
            @RequestBody InitiateReconciliationRequestDto request) {

        Reconciliation reconciliation = bankReconciliationService.initiateReconciliation(
                request.accountId,
                request.statementId,
                request.reconciliationDate,
                request.method
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(reconciliation));
    }

    @PostMapping("/{reconciliationId}/process-matching")
    @Operation(summary = "Process transaction matching")
    public ResponseEntity<BankReconciliationService.MatchingResult> processMatching(
            @Parameter(description = "Reconciliation ID") @PathVariable String reconciliationId,
            @RequestBody MatchingRequestDto request) {

        BankReconciliationService.MatchingRules rules = new BankReconciliationService.MatchingRules(
                request.tolerance,
                request.requireExactAmountMatch,
                request.allowDateVariance,
                request.dateVarianceDays,
                request.minimumMatchConfidence
        );

        BankReconciliationService.MatchingResult result = bankReconciliationService.processMatching(
                reconciliationId, rules);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{reconciliationId}/finalize")
    @Operation(summary = "Finalize reconciliation")
    public ResponseEntity<ReconciliationResponseDto> finalizeReconciliation(
            @Parameter(description = "Reconciliation ID") @PathVariable String reconciliationId,
            @RequestBody FinalizeRequestDto request) {

        Reconciliation reconciliation = bankReconciliationService.finalizeReconciliation(
                reconciliationId, request.bookBalance, request.notes);

        return ResponseEntity.ok(toDto(reconciliation));
    }

    @PostMapping("/match-transactions")
    @Operation(summary = "Match transactions")
    public ResponseEntity<Void> matchTransactions(
            @RequestBody MatchTransactionsRequestDto request) {

        ReconciliationCommand.MatchTransactionsCommand command = new ReconciliationCommand.MatchTransactionsCommand(
                RequestContextHolder.getTenantId(),
                request.reconciliationId,
                request.bankTransactionId,
                request.bookTransactionId,
                RequestContextHolder.getUserId(),
                request.matchConfidence
        );

        reconciliationCommandService.matchTransactions(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unmatch-transactions")
    @Operation(summary = "Unmatch transactions")
    public ResponseEntity<Void> unmatchTransactions(
            @RequestBody UnmatchTransactionsRequestDto request) {

        ReconciliationCommand.UnmatchTransactionsCommand command = new ReconciliationCommand.UnmatchTransactionsCommand(
                RequestContextHolder.getTenantId(),
                request.reconciliationLineId,
                RequestContextHolder.getUserId(),
                request.reason
        );

        reconciliationCommandService.unmatchTransactions(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-match")
    @Operation(summary = "Verify match")
    public ResponseEntity<Void> verifyMatch(
            @RequestBody VerifyMatchRequestDto request) {

        ReconciliationCommand.VerifyMatchCommand command = new ReconciliationCommand.VerifyMatchCommand(
                RequestContextHolder.getTenantId(),
                request.reconciliationLineId,
                RequestContextHolder.getUserId(),
                request.notes
        );

        reconciliationCommandService.verifyMatch(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mark-discrepancy")
    @Operation(summary = "Mark line as discrepancy")
    public ResponseEntity<Void> markDiscrepancy(
            @RequestBody MarkDiscrepancyRequestDto request) {

        ReconciliationCommand.MarkDiscrepancyCommand command = new ReconciliationCommand.MarkDiscrepancyCommand(
                RequestContextHolder.getTenantId(),
                request.reconciliationLineId,
                request.discrepancyCategory,
                request.reason,
                request.actionRequired
        );

        reconciliationCommandService.markDiscrepancy(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{reconciliationId}/lines")
    @Operation(summary = "Get reconciliation lines")
    public ResponseEntity<List<ReconciliationLineResponseDto>> getReconciliationLines(
            @Parameter(description = "Reconciliation ID") @PathVariable String reconciliationId,
            @RequestParam(required = false) ReconciliationLine.MatchStatus matchStatus,
            @RequestParam(required = false) Boolean requiresManualReview) {

        List<ReconciliationLine> lines = reconciliationQueryService.getReconciliationLines(
                reconciliationId, matchStatus, requiresManualReview, 0, 1000);

        return ResponseEntity.ok(lines.stream().map(this::toLineDto).toList());
    }

    @GetMapping("/discrepancies")
    @Operation(summary = "Get discrepancies")
    public ResponseEntity<List<ReconciliationLineResponseDto>> getDiscrepancies(
            @RequestParam(required = false) String reconciliationId,
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) ReconciliationLine.DiscrepancyCategory discrepancyCategory) {

        List<ReconciliationLine> discrepancies = reconciliationQueryService.getDiscrepancies(
                reconciliationId, accountId, startDate, endDate, discrepancyCategory);

        return ResponseEntity.ok(discrepancies.stream().map(this::toLineDto).toList());
    }

    @GetMapping("/summary")
    @Operation(summary = "Get reconciliation summary")
    public ResponseEntity<ReconciliationSummaryResponseDto> getSummary(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        BankReconciliationService.ReconciliationSummary summary = bankReconciliationService.getReconciliationSummary(
                accountId, startDate, endDate);

        return ResponseEntity.ok(ReconciliationSummaryResponseDto.builder()
                .totalReconciliations(summary.totalReconciliations())
                .completedCount(summary.completedCount())
                .pendingCount(summary.pendingCount())
                .balancedCount(summary.balancedCount())
                .totalDiscrepancyAmount(summary.totalDiscrepancyAmount())
                .totalLines(summary.totalLines())
                .totalMatched(summary.totalMatched())
                .pendingStatements(summary.pendingStatements())
                .periodStart(startDate)
                .periodEnd(endDate)
                .accountId(accountId)
                .build());
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get reconciliation statistics")
    public ResponseEntity<ReconciliationQueryService.ReconciliationStatistics> getStatistics(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        ReconciliationQueryService.ReconciliationStatistics statistics =
                reconciliationQueryService.getStatistics(accountId, startDate, endDate);

        return ResponseEntity.ok(statistics);
    }

    @DeleteMapping("/{reconciliationId}")
    @Operation(summary = "Delete reconciliation")
    public ResponseEntity<Void> deleteReconciliation(
            @Parameter(description = "Reconciliation ID") @PathVariable String reconciliationId) {

        ReconciliationCommand.DeleteReconciliationCommand command = new ReconciliationCommand.DeleteReconciliationCommand(
                RequestContextHolder.getTenantId(),
                reconciliationId
        );

        reconciliationCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{reconciliationId}/notes")
    @Operation(summary = "Add notes to reconciliation")
    public ResponseEntity<Void> addNotes(
            @Parameter(description = "Reconciliation ID") @PathVariable String reconciliationId,
            @RequestBody NotesRequestDto request) {

        ReconciliationCommand.AddNotesCommand command = new ReconciliationCommand.AddNotesCommand(
                RequestContextHolder.getTenantId(),
                reconciliationId,
                request.notes
        );

        reconciliationCommandService.addNotes(command);
        return ResponseEntity.ok().build();
    }

    private ReconciliationResponseDto toDto(Reconciliation reconciliation) {
        return ReconciliationResponseDto.builder()
                .id(reconciliation.getId())
                .reconciliationId(reconciliation.getReconciliationId())
                .accountId(reconciliation.getAccountId())
                .accountNumber(reconciliation.getAccountNumber())
                .statementId(reconciliation.getStatementId())
                .reconciliationDate(reconciliation.getReconciliationDate())
                .periodStart(reconciliation.getPeriodStart())
                .periodEnd(reconciliation.getPeriodEnd())
                .status(mapReconciliationStatus(reconciliation.getStatus()))
                .startingBalance(reconciliation.getStartingBalance())
                .endingBalance(reconciliation.getEndingBalance())
                .bookBalance(reconciliation.getBookBalance())
                .bankBalance(reconciliation.getBankBalance())
                .difference(reconciliation.getDifference())
                .tolerance(reconciliation.getTolerance())
                .isBalanced(reconciliation.getIsBalanced())
                .reconciledBy(reconciliation.getReconciledBy())
                .reconciledAt(reconciliation.getReconciledAt())
                .approvedBy(reconciliation.getApprovedBy())
                .approvedAt(reconciliation.getApprovedAt())
                .lineCount(reconciliation.getLineCount())
                .matchedCount(reconciliation.getMatchedCount())
                .unmatchedCount(reconciliation.getUnmatchedCount())
                .discrepancyCount(reconciliation.getDiscrepancyCount())
                .notes(reconciliation.getNotes())
                .autoReconciled(reconciliation.getAutoReconciled())
                .reconciliationMethod(mapReconciliationMethod(reconciliation.getReconciliationMethod()))
                .completionPercentage(reconciliation.getCompletionPercentage())
                .errorMessage(reconciliation.getErrorMessage())
                .createdAt(reconciliation.getCreatedAt())
                .updatedAt(reconciliation.getUpdatedAt())
                .build();
    }

    private ReconciliationLineResponseDto toLineDto(ReconciliationLine line) {
        return ReconciliationLineResponseDto.builder()
                .id(line.getId())
                .lineId(line.getLineId())
                .reconciliationId(line.getReconciliationId())
                .accountId(line.getAccountId())
                .lineNumber(line.getLineNumber())
                .lineType(mapLineType(line.getLineType()))
                .bankTransactionId(line.getBankTransactionId())
                .bankTransactionDate(line.getBankTransactionDate())
                .bankDescription(line.getBankDescription())
                .bankReference(line.getBankReference())
                .bankAmount(line.getBankAmount())
                .bookTransactionId(line.getBookTransactionId())
                .bookTransactionDate(line.getBookTransactionDate())
                .bookDescription(line.getBookDescription())
                .bookReference(line.getBookReference())
                .bookAmount(line.getBookAmount())
                .amountDifference(line.getAmountDifference())
                .matchStatus(mapMatchStatus(line.getMatchStatus()))
                .matchConfidence(line.getMatchConfidence())
                .matchedBy(mapMatchedBy(line.getMatchedBy()))
                .matchedAt(line.getMatchedAt())
                .verifiedBy(line.getVerifiedBy())
                .verifiedAt(line.getVerifiedAt())
                .discrepancyReason(line.getDiscrepancyReason())
                .discrepancyCategory(mapDiscrepancyCategory(line.getDiscrepancyCategory()))
                .actionRequired(mapActionRequired(line.getActionRequired()))
                .actionTaken(line.getActionTaken())
                .notes(line.getNotes())
                .currency(line.getCurrency())
                .autoMatched(line.getAutoMatched())
                .requiresManualReview(line.getRequiresManualReview())
                .createdAt(line.getCreatedAt())
                .updatedAt(line.getUpdatedAt())
                .build();
    }

    private ReconciliationResponseDto.ReconciliationStatusDto mapReconciliationStatus(Reconciliation.ReconciliationStatus status) {
        return status != null ? ReconciliationResponseDto.ReconciliationStatusDto.valueOf(status.name()) : null;
    }

    private ReconciliationResponseDto.ReconciliationMethodDto mapReconciliationMethod(Reconciliation.ReconciliationMethod method) {
        return method != null ? ReconciliationResponseDto.ReconciliationMethodDto.valueOf(method.name()) : null;
    }

    private ReconciliationLineResponseDto.LineTypeDto mapLineType(ReconciliationLine.LineType type) {
        return type != null ? ReconciliationLineResponseDto.LineTypeDto.valueOf(type.name()) : null;
    }

    private ReconciliationLineResponseDto.MatchStatusDto mapMatchStatus(ReconciliationLine.MatchStatus status) {
        return status != null ? ReconciliationLineResponseDto.MatchStatusDto.valueOf(status.name()) : null;
    }

    private ReconciliationLineResponseDto.MatchedByDto mapMatchedBy(ReconciliationLine.MatchedBy matchedBy) {
        return matchedBy != null ? ReconciliationLineResponseDto.MatchedByDto.valueOf(matchedBy.name()) : null;
    }

    private ReconciliationLineResponseDto.DiscrepancyCategoryDto mapDiscrepancyCategory(ReconciliationLine.DiscrepancyCategory category) {
        return category != null ? ReconciliationLineResponseDto.DiscrepancyCategoryDto.valueOf(category.name()) : null;
    }

    private ReconciliationLineResponseDto.ActionRequiredDto mapActionRequired(ReconciliationLine.ActionRequired action) {
        return action != null ? ReconciliationLineResponseDto.ActionRequiredDto.valueOf(action.name()) : null;
    }

    // Request DTOs
    public static class CreateReconciliationRequestDto {
        public String accountId;
        public String accountNumber;
        public String statementId;
        public LocalDate reconciliationDate;
        public LocalDate periodStart;
        public LocalDate periodEnd;
        public BigDecimal startingBalance;
        public BigDecimal endingBalance;
        public BigDecimal tolerance;
        public Reconciliation.ReconciliationMethod reconciliationMethod;
        public Boolean autoReconcile;
        public String notes;
    }

    public static class CompleteReconciliationRequestDto {
        public BigDecimal bookBalance;
        public BigDecimal bankBalance;
        public String notes;
    }

    public static class InitiateReconciliationRequestDto {
        public String accountId;
        public String statementId;
        public LocalDate reconciliationDate;
        public Reconciliation.ReconciliationMethod method;
    }

    public static class MatchingRequestDto {
        public BigDecimal tolerance;
        public boolean requireExactAmountMatch = true;
        public boolean allowDateVariance = false;
        public int dateVarianceDays = 0;
        public double minimumMatchConfidence = 0.8;
    }

    public static class FinalizeRequestDto {
        public BigDecimal bookBalance;
        public String notes;
    }

    public static class MatchTransactionsRequestDto {
        public String reconciliationId;
        public String bankTransactionId;
        public String bookTransactionId;
        public Double matchConfidence;
    }

    public static class UnmatchTransactionsRequestDto {
        public String reconciliationLineId;
        public String reason;
    }

    public static class VerifyMatchRequestDto {
        public String reconciliationLineId;
        public String notes;
    }

    public static class MarkDiscrepancyRequestDto {
        public String reconciliationLineId;
        public ReconciliationLine.DiscrepancyCategory discrepancyCategory;
        public String reason;
        public ReconciliationLine.ActionRequired actionRequired;
    }

    public static class NotesRequestDto {
        public String notes;
    }

    public static class ApprovalRequestDto {
        public String notes;
    }

    public static class CancelRequestDto {
        public String reason;
    }
}
