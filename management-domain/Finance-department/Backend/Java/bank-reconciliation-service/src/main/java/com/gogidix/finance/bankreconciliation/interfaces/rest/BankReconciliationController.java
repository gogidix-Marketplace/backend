package com.gogidix.finance.bankreconciliation.interfaces.rest;

import com.gogidix.finance.bankreconciliation.application.dto.response.BankAccountResponseDto;
import com.gogidix.finance.bankreconciliation.application.dto.response.BankStatementResponseDto;
import com.gogidix.finance.bankreconciliation.application.dto.response.ReconciliationResponseDto;
import com.gogidix.finance.bankreconciliation.application.dto.response.ReconciliationSummaryResponseDto;
import com.gogidix.finance.bankreconciliation.application.service.BankAccountQueryService;
import com.gogidix.finance.bankreconciliation.application.service.BankReconciliationService;
import com.gogidix.finance.bankreconciliation.application.service.BankStatementQueryService;
import com.gogidix.finance.bankreconciliation.domain.model.BankAccount;
import com.gogidix.finance.bankreconciliation.domain.model.BankStatement;
import com.gogidix.finance.bankreconciliation.domain.model.Reconciliation;
import com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Bank Reconciliation REST Controller
 * Main entry point for bank reconciliation operations
 * Orchestrates the complete reconciliation workflow
 */
@RestController
@RequestMapping("/bank-reconciliation")
@RequiredArgsConstructor
@Tag(name = "Bank Reconciliation", description = "Bank reconciliation workflow endpoints")
public class BankReconciliationController {

    private final BankReconciliationService bankReconciliationService;
    private final BankAccountQueryService bankAccountQueryService;
    private final BankStatementQueryService bankStatementQueryService;

    @PostMapping("/initiate")
    @Operation(summary = "Initiate a new bank reconciliation process")
    public ResponseEntity<ReconciliationResponseDto> initiateReconciliation(
            @Valid @RequestBody InitiateReconciliationRequestDto request) {

        Reconciliation reconciliation = bankReconciliationService.initiateReconciliation(
                request.accountId,
                request.statementId,
                request.reconciliationDate,
                request.reconciliationMethod
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(toReconciliationDto(reconciliation));
    }

    @PostMapping("/match-transactions")
    @Operation(summary = "Match bank and book transactions")
    public ResponseEntity<BankReconciliationService.MatchingResult> matchTransactions(
            @RequestBody MatchTransactionsRequestDto request) {

        BankReconciliationService.MatchingRules rules = new BankReconciliationService.MatchingRules(
                request.tolerance,
                request.requireExactAmountMatch,
                request.allowDateVariance,
                request.dateVarianceDays,
                request.minimumMatchConfidence
        );

        BankReconciliationService.MatchingResult result = bankReconciliationService.processMatching(
                request.reconciliationId, rules);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/finalize")
    @Operation(summary = "Finalize reconciliation process")
    public ResponseEntity<ReconciliationResponseDto> finalizeReconciliation(
            @Valid @RequestBody FinalizeReconciliationRequestDto request) {

        Reconciliation reconciliation = bankReconciliationService.finalizeReconciliation(
                request.reconciliationId,
                request.bookBalance,
                request.notes
        );

        return ResponseEntity.ok(toReconciliationDto(reconciliation));
    }

    @GetMapping("/accounts/pending-reconciliation")
    @Operation(summary = "Get accounts pending reconciliation")
    public ResponseEntity<List<BankAccountResponseDto>> getPendingAccounts() {
        List<BankAccount> accounts = bankAccountQueryService.getAccountsReadyForReconciliation();
        return ResponseEntity.ok(accounts.stream()
                .map(this::toAccountDto)
                .toList());
    }

    @GetMapping("/statements/pending")
    @Operation(summary = "Get pending statements not yet reconciled")
    public ResponseEntity<List<BankStatementResponseDto>> getPendingStatements() {
        List<BankStatement> statements = bankStatementQueryService.getUnreconciledStatements();
        return ResponseEntity.ok(statements.stream()
                .map(this::toStatementDto)
                .toList());
    }

    @GetMapping("/summary")
    @Operation(summary = "Get reconciliation summary")
    public ResponseEntity<ReconciliationSummaryResponseDto> getSummary(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        BankReconciliationService.ReconciliationSummary summary =
                bankReconciliationService.getReconciliationSummary(accountId, startDate, endDate);

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

    private ReconciliationResponseDto toReconciliationDto(Reconciliation reconciliation) {
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

    private BankAccountResponseDto toAccountDto(BankAccount account) {
        return BankAccountResponseDto.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountName(account.getAccountName())
                .accountType(mapAccountType(account.getAccountType()))
                .bankName(account.getBankName())
                .bankCode(account.getBankCode())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .balanceDate(account.getBalanceDate())
                .status(mapAccountStatus(account.getStatus()))
                .isPrimary(account.getIsPrimary())
                .lastReconciledAt(account.getLastReconciledAt())
                .lastStatementDate(account.getLastStatementDate())
                .openingBalance(account.getOpeningBalance())
                .iban(account.getIban())
                .swiftCode(account.getSwiftCode())
                .routingNumber(account.getRoutingNumber())
                .description(account.getDescription())
                .tags(account.getTags())
                .statementFrequency(mapStatementFrequency(account.getStatementFrequency()))
                .reconciliationTolerance(account.getReconciliationTolerance())
                .autoReconcile(account.getAutoReconcile())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    private BankStatementResponseDto toStatementDto(BankStatement statement) {
        return BankStatementResponseDto.builder()
                .id(statement.getId())
                .statementId(statement.getStatementId())
                .accountId(statement.getAccountId())
                .accountNumber(statement.getAccountNumber())
                .statementDate(statement.getStatementDate())
                .startDate(statement.getStartDate())
                .endDate(statement.getEndDate())
                .openingBalance(statement.getOpeningBalance())
                .closingBalance(statement.getClosingBalance())
                .currency(statement.getCurrency())
                .importStatus(mapImportStatus(statement.getImportStatus()))
                .importSource(mapImportSource(statement.getImportSource()))
                .fileReference(statement.getFileReference())
                .transactionCount(statement.getTransactionCount())
                .totalDebits(statement.getTotalDebits())
                .totalCredits(statement.getTotalCredits())
                .importErrors(statement.getImportErrors())
                .importWarnings(statement.getImportWarnings())
                .processedAt(statement.getProcessedAt())
                .validatedAt(statement.getValidatedAt())
                .reconciled(statement.getReconciled())
                .reconciliationId(statement.getReconciliationId())
                .statementType(mapStatementType(statement.getStatementType()))
                .bankReference(statement.getBankReference())
                .createdAt(statement.getCreatedAt())
                .updatedAt(statement.getUpdatedAt())
                .build();
    }

    // Mapper methods
    private ReconciliationResponseDto.ReconciliationStatusDto mapReconciliationStatus(Reconciliation.ReconciliationStatus status) {
        return status != null ? ReconciliationResponseDto.ReconciliationStatusDto.valueOf(status.name()) : null;
    }

    private ReconciliationResponseDto.ReconciliationMethodDto mapReconciliationMethod(Reconciliation.ReconciliationMethod method) {
        return method != null ? ReconciliationResponseDto.ReconciliationMethodDto.valueOf(method.name()) : null;
    }

    private BankAccountResponseDto.AccountTypeDto mapAccountType(BankAccount.AccountType type) {
        return type != null ? BankAccountResponseDto.AccountTypeDto.valueOf(type.name()) : null;
    }

    private BankAccountResponseDto.AccountStatusDto mapAccountStatus(BankAccount.AccountStatus status) {
        return status != null ? BankAccountResponseDto.AccountStatusDto.valueOf(status.name()) : null;
    }

    private BankAccountResponseDto.StatementFrequencyDto mapStatementFrequency(BankAccount.StatementFrequency frequency) {
        return frequency != null ? BankAccountResponseDto.StatementFrequencyDto.valueOf(frequency.name()) : null;
    }

    private BankStatementResponseDto.ImportStatusDto mapImportStatus(BankStatement.ImportStatus status) {
        return status != null ? BankStatementResponseDto.ImportStatusDto.valueOf(status.name()) : null;
    }

    private BankStatementResponseDto.ImportSourceDto mapImportSource(BankStatement.ImportSource source) {
        return source != null ? BankStatementResponseDto.ImportSourceDto.valueOf(source.name()) : null;
    }

    private BankStatementResponseDto.StatementTypeDto mapStatementType(BankStatement.StatementType type) {
        return type != null ? BankStatementResponseDto.StatementTypeDto.valueOf(type.name()) : null;
    }

    // Request DTOs
    public static class InitiateReconciliationRequestDto {
        public String accountId;
        public String statementId;
        public LocalDate reconciliationDate;
        public Reconciliation.ReconciliationMethod reconciliationMethod;
    }

    public static class MatchTransactionsRequestDto {
        public String reconciliationId;
        public BigDecimal tolerance;
        public boolean requireExactAmountMatch = true;
        public boolean allowDateVariance = false;
        public int dateVarianceDays = 3;
        public double minimumMatchConfidence = 0.85;
    }

    public static class FinalizeReconciliationRequestDto {
        public String reconciliationId;
        public BigDecimal bookBalance;
        public String notes;
    }
}
