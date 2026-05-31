package com.gogidix.finance.bankreconciliation.interfaces.rest;

import com.gogidix.finance.bankreconciliation.application.dto.response.BankAccountResponseDto;
import com.gogidix.finance.bankreconciliation.application.service.BankAccountCommandService;
import com.gogidix.finance.bankreconciliation.application.service.BankAccountQueryService;
import com.gogidix.finance.bankreconciliation.domain.model.BankAccount;
import com.gogidix.finance.bankreconciliation.domain.port.in.BankAccountCommand;
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
 * Bank Account REST Controller
 * Handles HTTP requests for bank account operations
 */
@RestController
@RequestMapping("/bank-accounts")
@RequiredArgsConstructor
@Tag(name = "Bank Accounts", description = "Bank account management endpoints")
public class BankAccountController {

    private final BankAccountCommandService bankAccountCommandService;
    private final BankAccountQueryService bankAccountQueryService;

    @PostMapping
    @Operation(summary = "Create a new bank account")
    public ResponseEntity<BankAccountResponseDto> createAccount(
            @Valid @RequestBody CreateAccountRequestDto request) {
        BankAccountCommand.CreateBankAccountCommand command = new BankAccountCommand.CreateBankAccountCommand(
                RequestContextHolder.getTenantId(),
                request.accountNumber,
                request.accountName,
                request.accountType,
                request.bankName,
                request.currency,
                request.bankCode,
                request.openingBalance,
                request.balanceDate,
                request.iban,
                request.swiftCode,
                request.routingNumber,
                request.description,
                request.tags,
                request.statementFrequency,
                request.reconciliationTolerance,
                request.autoReconcile,
                request.isPrimary
        );

        BankAccount account = bankAccountCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(account));
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get bank account by ID")
    public ResponseEntity<BankAccountResponseDto> getAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId) {
        BankAccount account = bankAccountQueryService.getById(accountId);
        return ResponseEntity.ok(toDto(account));
    }

    @GetMapping("/number/{accountNumber}")
    @Operation(summary = "Get bank account by account number")
    public ResponseEntity<BankAccountResponseDto> getAccountByNumber(
            @Parameter(description = "Account Number") @PathVariable String accountNumber) {
        BankAccount account = bankAccountQueryService.getByAccountNumber(accountNumber);
        return ResponseEntity.ok(toDto(account));
    }

    @GetMapping
    @Operation(summary = "Get all bank accounts for tenant")
    public ResponseEntity<Page<BankAccountResponseDto>> getAllAccounts(
            @RequestParam(required = false) BankAccount.AccountStatus status,
            @RequestParam(required = false) BankAccount.AccountType accountType,
            @RequestParam(required = false) Boolean isPrimary,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "accountName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<BankAccount> accounts = bankAccountQueryService.getAccountsByTenant(
                status, accountType, isPrimary, page, size, sortBy, sortDirection);

        return ResponseEntity.ok(accounts.map(this::toDto));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active bank accounts")
    public ResponseEntity<List<BankAccountResponseDto>> getActiveAccounts() {
        List<BankAccount> accounts = bankAccountQueryService.getAllActiveAccounts();
        return ResponseEntity.ok(accounts.stream().map(this::toDto).toList());
    }

    @GetMapping("/primary")
    @Operation(summary = "Get primary bank account")
    public ResponseEntity<BankAccountResponseDto> getPrimaryAccount() {
        BankAccount account = bankAccountQueryService.getPrimaryAccount();
        return ResponseEntity.ok(toDto(account));
    }

    @GetMapping("/currency/{currency}")
    @Operation(summary = "Get bank accounts by currency")
    public ResponseEntity<List<BankAccountResponseDto>> getAccountsByCurrency(
            @Parameter(description = "Currency Code") @PathVariable String currency) {
        List<BankAccount> accounts = bankAccountQueryService.getAccountsByCurrency(currency);
        return ResponseEntity.ok(accounts.stream().map(this::toDto).toList());
    }

    @GetMapping("/ready-for-reconciliation")
    @Operation(summary = "Get accounts ready for reconciliation")
    public ResponseEntity<List<BankAccountResponseDto>> getAccountsReadyForReconciliation() {
        List<BankAccount> accounts = bankAccountQueryService.getAccountsReadyForReconciliation();
        return ResponseEntity.ok(accounts.stream().map(this::toDto).toList());
    }

    @PutMapping("/{accountId}")
    @Operation(summary = "Update bank account")
    public ResponseEntity<BankAccountResponseDto> updateAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @Valid @RequestBody UpdateAccountRequestDto request) {

        BankAccountCommand.UpdateBankAccountCommand command = new BankAccountCommand.UpdateBankAccountCommand(
                RequestContextHolder.getTenantId(),
                accountId,
                request.accountName,
                request.description,
                request.balance,
                request.balanceDate,
                request.iban,
                request.swiftCode,
                request.routingNumber,
                request.tags,
                request.reconciliationTolerance,
                request.statementFrequency,
                request.autoReconcile
        );

        BankAccount account = bankAccountCommandService.update(command);
        return ResponseEntity.ok(toDto(account));
    }

    @PutMapping("/{accountId}/balance")
    @Operation(summary = "Update account balance")
    public ResponseEntity<Void> updateBalance(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @RequestBody UpdateBalanceRequestDto request) {

        BankAccountCommand.UpdateBalanceCommand command = new BankAccountCommand.UpdateBalanceCommand(
                RequestContextHolder.getTenantId(),
                accountId,
                request.newBalance,
                request.balanceDate
        );

        bankAccountCommandService.updateBalance(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{accountId}/set-primary")
    @Operation(summary = "Set account as primary")
    public ResponseEntity<Void> setAsPrimary(
            @Parameter(description = "Account ID") @PathVariable String accountId) {

        BankAccountCommand.SetAsPrimaryCommand command = new BankAccountCommand.SetAsPrimaryCommand(
                RequestContextHolder.getTenantId(),
                accountId
        );

        bankAccountCommandService.setAsPrimary(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{accountId}/activate")
    @Operation(summary = "Activate bank account")
    public ResponseEntity<Void> activateAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId) {

        BankAccountCommand.ActivateAccountCommand command = new BankAccountCommand.ActivateAccountCommand(
                RequestContextHolder.getTenantId(),
                accountId
        );

        bankAccountCommandService.activate(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{accountId}/deactivate")
    @Operation(summary = "Deactivate bank account")
    public ResponseEntity<Void> deactivateAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId) {

        BankAccountCommand.DeactivateAccountCommand command = new BankAccountCommand.DeactivateAccountCommand(
                RequestContextHolder.getTenantId(),
                accountId
        );

        bankAccountCommandService.deactivate(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{accountId}/close")
    @Operation(summary = "Close bank account")
    public ResponseEntity<Void> closeAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId) {

        BankAccountCommand.CloseAccountCommand command = new BankAccountCommand.CloseAccountCommand(
                RequestContextHolder.getTenantId(),
                accountId
        );

        bankAccountCommandService.close(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{accountId}/mark-reconciled")
    @Operation(summary = "Mark account as reconciled")
    public ResponseEntity<Void> markAsReconciled(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @RequestBody MarkReconciledRequestDto request) {

        BankAccountCommand.MarkAsReconciledCommand command = new BankAccountCommand.MarkAsReconciledCommand(
                RequestContextHolder.getTenantId(),
                accountId,
                request.statementDate
        );

        bankAccountCommandService.markAsReconciled(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{accountId}")
    @Operation(summary = "Delete bank account")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId) {

        BankAccountCommand.DeleteBankAccountCommand command = new BankAccountCommand.DeleteBankAccountCommand(
                RequestContextHolder.getTenantId(),
                accountId
        );

        bankAccountCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    @Operation(summary = "Get bank account summary")
    public ResponseEntity<BankAccountQueryService.AccountSummary> getSummary() {
        BankAccountQueryService.AccountSummary summary = bankAccountQueryService.getSummary();
        return ResponseEntity.ok(summary);
    }

    private BankAccountResponseDto toDto(BankAccount account) {
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

    private BankAccountResponseDto.AccountTypeDto mapAccountType(BankAccount.AccountType type) {
        return type != null ? BankAccountResponseDto.AccountTypeDto.valueOf(type.name()) : null;
    }

    private BankAccountResponseDto.AccountStatusDto mapAccountStatus(BankAccount.AccountStatus status) {
        return status != null ? BankAccountResponseDto.AccountStatusDto.valueOf(status.name()) : null;
    }

    private BankAccountResponseDto.StatementFrequencyDto mapStatementFrequency(BankAccount.StatementFrequency frequency) {
        return frequency != null ? BankAccountResponseDto.StatementFrequencyDto.valueOf(frequency.name()) : null;
    }

    // Request DTOs
    public static class CreateAccountRequestDto {
        public String accountNumber;
        public String accountName;
        public BankAccount.AccountType accountType;
        public String bankName;
        public String bankCode;
        public String currency;
        public BigDecimal openingBalance;
        public LocalDate balanceDate;
        public String iban;
        public String swiftCode;
        public String routingNumber;
        public String description;
        public List<String> tags;
        public BankAccount.StatementFrequency statementFrequency;
        public BigDecimal reconciliationTolerance;
        public Boolean autoReconcile;
        public Boolean isPrimary;
    }

    public static class UpdateAccountRequestDto {
        public String accountName;
        public String description;
        public BigDecimal balance;
        public LocalDate balanceDate;
        public String iban;
        public String swiftCode;
        public String routingNumber;
        public List<String> tags;
        public BigDecimal reconciliationTolerance;
        public BankAccount.StatementFrequency statementFrequency;
        public Boolean autoReconcile;
    }

    public static class UpdateBalanceRequestDto {
        public BigDecimal newBalance;
        public LocalDate balanceDate;
    }

    public static class MarkReconciledRequestDto {
        public LocalDate statementDate;
    }
}
