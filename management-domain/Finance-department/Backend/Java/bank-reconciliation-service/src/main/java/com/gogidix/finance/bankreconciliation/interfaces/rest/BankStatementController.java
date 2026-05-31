package com.gogidix.finance.bankreconciliation.interfaces.rest;

import com.gogidix.finance.bankreconciliation.application.dto.response.BankStatementResponseDto;
import com.gogidix.finance.bankreconciliation.application.service.BankStatementCommandService;
import com.gogidix.finance.bankreconciliation.application.service.BankStatementQueryService;
import com.gogidix.finance.bankreconciliation.domain.model.BankStatement;
import com.gogidix.finance.bankreconciliation.domain.port.in.BankStatementCommand;
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
 * Bank Statement REST Controller
 * Handles HTTP requests for bank statement operations
 */
@RestController
@RequestMapping("/bank-statements")
@RequiredArgsConstructor
@Tag(name = "Bank Statements", description = "Bank statement management endpoints")
public class BankStatementController {

    private final BankStatementCommandService bankStatementCommandService;
    private final BankStatementQueryService bankStatementQueryService;

    @PostMapping("/import")
    @Operation(summary = "Import a new bank statement")
    public ResponseEntity<BankStatementResponseDto> importStatement(
            @Valid @RequestBody ImportStatementRequestDto request) {

        BankStatementCommand.ImportBankStatementCommand command = new BankStatementCommand.ImportBankStatementCommand(
                RequestContextHolder.getTenantId(),
                request.accountId,
                request.accountNumber,
                request.statementDate,
                request.startDate,
                request.endDate,
                request.openingBalance,
                request.closingBalance,
                request.currency,
                request.importSource,
                request.fileReference,
                request.bankReference,
                request.statementType,
                request.transactions,
                RequestContextHolder.getUserId()
        );

        BankStatement statement = bankStatementCommandService.importStatement(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(statement));
    }

    @GetMapping("/{statementId}")
    @Operation(summary = "Get bank statement by ID")
    public ResponseEntity<BankStatementResponseDto> getStatement(
            @Parameter(description = "Statement ID") @PathVariable String statementId) {
        BankStatement statement = bankStatementQueryService.getById(statementId);
        return ResponseEntity.ok(toDto(statement));
    }

    @GetMapping
    @Operation(summary = "Get bank statements for account")
    public ResponseEntity<Page<BankStatementResponseDto>> getStatementsByAccount(
            @RequestParam String accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) BankStatement.ImportStatus importStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<BankStatement> statements = bankStatementQueryService.getStatementsByAccount(
                accountId, startDate, endDate, importStatus, page, size);

        return ResponseEntity.ok(statements.map(this::toDto));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get bank statements by status")
    public ResponseEntity<List<BankStatementResponseDto>> getStatementsByStatus(
            @Parameter(description = "Import Status") @PathVariable BankStatement.ImportStatus status) {
        List<BankStatement> statements = bankStatementQueryService.getStatementsByStatus(status);
        return ResponseEntity.ok(statements.stream().map(this::toDto).toList());
    }

    @GetMapping("/unreconciled")
    @Operation(summary = "Get unreconciled bank statements")
    public ResponseEntity<List<BankStatementResponseDto>> getUnreconciledStatements() {
        List<BankStatement> statements = bankStatementQueryService.getUnreconciledStatements();
        return ResponseEntity.ok(statements.stream().map(this::toDto).toList());
    }

    @GetMapping("/ready-for-reconciliation")
    @Operation(summary = "Get statements ready for reconciliation")
    public ResponseEntity<List<BankStatementResponseDto>> getStatementsReadyForReconciliation(
            @RequestParam(required = false) String accountId) {
        List<BankStatement> statements = bankStatementQueryService.getStatementsReadyForReconciliation(accountId);
        return ResponseEntity.ok(statements.stream().map(this::toDto).toList());
    }

    @GetMapping("/account/{accountId}/date/{date}")
    @Operation(summary = "Get statement by account and date")
    public ResponseEntity<BankStatementResponseDto> getStatementByAccountAndDate(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @Parameter(description = "Statement Date") @PathVariable LocalDate date) {
        BankStatement statement = bankStatementQueryService.getStatementByAccountAndDate(accountId, date);
        return ResponseEntity.ok(toDto(statement));
    }

    @PostMapping("/{statementId}/process")
    @Operation(summary = "Process bank statement")
    public ResponseEntity<Void> processStatement(
            @Parameter(description = "Statement ID") @PathVariable String statementId) {

        BankStatementCommand.ProcessBankStatementCommand command = new BankStatementCommand.ProcessBankStatementCommand(
                RequestContextHolder.getTenantId(),
                statementId,
                RequestContextHolder.getUserId()
        );

        bankStatementCommandService.process(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{statementId}/validate")
    @Operation(summary = "Validate bank statement")
    public ResponseEntity<ValidationResponseDto> validateStatement(
            @Parameter(description = "Statement ID") @PathVariable String statementId,
            @RequestBody ValidateRequestDto request) {

        BankStatementCommand.ValidateBankStatementCommand command = new BankStatementCommand.ValidateBankStatementCommand(
                RequestContextHolder.getTenantId(),
                statementId,
                request.tolerance,
                request.validateBalances,
                request.validateTransactions
        );

        boolean isValid = bankStatementCommandService.validate(command);

        return ResponseEntity.ok(new ValidationResponseDto(isValid));
    }

    @PostMapping("/{statementId}/transactions")
    @Operation(summary = "Add transaction to statement")
    public ResponseEntity<Void> addTransaction(
            @Parameter(description = "Statement ID") @PathVariable String statementId,
            @RequestBody AddTransactionRequestDto request) {

        BankStatementCommand.AddTransactionCommand command = new BankStatementCommand.AddTransactionCommand(
                RequestContextHolder.getTenantId(),
                statementId,
                request.transaction
        );

        bankStatementCommandService.addTransaction(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{statementId}/link-reconciliation")
    @Operation(summary = "Link statement to reconciliation")
    public ResponseEntity<Void> linkToReconciliation(
            @Parameter(description = "Statement ID") @PathVariable String statementId,
            @RequestBody LinkReconciliationRequestDto request) {

        BankStatementCommand.LinkToReconciliationCommand command = new BankStatementCommand.LinkToReconciliationCommand(
                RequestContextHolder.getTenantId(),
                statementId,
                request.reconciliationId
        );

        bankStatementCommandService.linkToReconciliation(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{statementId}/retry-import")
    @Operation(summary = "Retry failed import")
    public ResponseEntity<Void> retryImport(
            @Parameter(description = "Statement ID") @PathVariable String statementId) {

        BankStatementCommand.RetryImportCommand command = new BankStatementCommand.RetryImportCommand(
                RequestContextHolder.getTenantId(),
                statementId,
                RequestContextHolder.getUserId()
        );

        bankStatementCommandService.retryImport(command);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{statementId}")
    @Operation(summary = "Delete bank statement")
    public ResponseEntity<Void> deleteStatement(
            @Parameter(description = "Statement ID") @PathVariable String statementId) {

        BankStatementCommand.DeleteBankStatementCommand command = new BankStatementCommand.DeleteBankStatementCommand(
                RequestContextHolder.getTenantId(),
                statementId
        );

        bankStatementCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    @Operation(summary = "Get bank statement summary")
    public ResponseEntity<BankStatementQueryService.StatementSummary> getSummary(
            @RequestParam(required = false) String accountId) {
        BankStatementQueryService.StatementSummary summary = bankStatementQueryService.getSummary(accountId);
        return ResponseEntity.ok(summary);
    }

    private BankStatementResponseDto toDto(BankStatement statement) {
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
                .transactions(statement.getTransactions() != null
                        ? statement.getTransactions().stream()
                                .map(this::toTransactionDto)
                                .toList()
                        : null)
                .createdAt(statement.getCreatedAt())
                .updatedAt(statement.getUpdatedAt())
                .build();
    }

    private BankStatementResponseDto.StatementTransactionDto toTransactionDto(
            BankStatement.StatementTransaction transaction) {
        return BankStatementResponseDto.StatementTransactionDto.builder()
                .transactionId(transaction.getTransactionId())
                .transactionDate(transaction.getTransactionDate())
                .description(transaction.getDescription())
                .reference(transaction.getReference())
                .amount(transaction.getAmount())
                .transactionType(mapTransactionType(transaction.getTransactionType()))
                .category(transaction.getCategory())
                .isReconciled(transaction.getIsReconciled())
                .reconciliationLineId(transaction.getReconciliationLineId())
                .build();
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

    private BankStatementResponseDto.TransactionTypeDto mapTransactionType(BankStatement.TransactionType type) {
        return type != null ? BankStatementResponseDto.TransactionTypeDto.valueOf(type.name()) : null;
    }

    // Request DTOs
    public static class ImportStatementRequestDto {
        public String accountId;
        public String accountNumber;
        public LocalDate statementDate;
        public LocalDate startDate;
        public LocalDate endDate;
        public BigDecimal openingBalance;
        public BigDecimal closingBalance;
        public String currency;
        public BankStatement.ImportSource importSource;
        public String fileReference;
        public String bankReference;
        public BankStatement.StatementType statementType;
        public List<BankStatement.StatementTransaction> transactions;
    }

    public static class ValidateRequestDto {
        public BigDecimal tolerance;
        public Boolean validateBalances = true;
        public Boolean validateTransactions = true;
    }

    public static class AddTransactionRequestDto {
        public BankStatement.StatementTransaction transaction;
    }

    public static class LinkReconciliationRequestDto {
        public String reconciliationId;
    }

    public record ValidationResponseDto(boolean isValid) {}
}
