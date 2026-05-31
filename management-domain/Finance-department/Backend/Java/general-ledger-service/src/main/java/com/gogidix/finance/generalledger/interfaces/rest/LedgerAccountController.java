package com.gogidix.finance.generalledger.interfaces.rest;

import com.gogidix.finance.generalledger.application.dto.response.LedgerAccountResponseDto;
import com.gogidix.finance.generalledger.application.service.LedgerAccountCommandService;
import com.gogidix.finance.generalledger.application.service.LedgerAccountQueryService;
import com.gogidix.finance.generalledger.shared.requestcontext.RequestContextHolder;
import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import com.gogidix.finance.ledger.domain.port.in.LedgerAccountCommand;
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
import java.util.stream.Collectors;

/**
 * Ledger Account REST Controller
 * Handles HTTP requests for ledger account operations
 */
@RestController
@RequestMapping("/api/ledger-accounts")
@RequiredArgsConstructor
@Tag(name = "Ledger Accounts", description = "Ledger account management endpoints")
public class LedgerAccountController {

    private final LedgerAccountCommandService ledgerAccountCommandService;
    private final LedgerAccountQueryService ledgerAccountQueryService;

    @PostMapping
    @Operation(summary = "Create a new ledger account")
    public ResponseEntity<LedgerAccountResponseDto> createAccount(
            @Valid @RequestBody CreateAccountRequestDto request) {

        LedgerAccountCommand.CreateAccountCommand command = new LedgerAccountCommand.CreateAccountCommand();
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setAccountNumber(request.getAccountNumber());
        command.setAccountName(request.getAccountName());
        command.setAccountType(request.getAccountType());
        command.setAccountSubType(request.getAccountSubType());
        command.setCurrency(request.getCurrency());
        command.setCreatedBy(RequestContextHolder.getUserId().orElse("system"));
        command.setParentAccountId(request.getParentAccountId());
        command.setAccountLevel(request.getAccountLevel());
        command.setDescription(request.getDescription());
        command.setCostCenter(request.getCostCenter());
        command.setDepartment(request.getDepartment());
        command.setLocation(request.getLocation());
        command.setIsCashAccount(request.getIsCashAccount());
        command.setIsReconcilable(request.getIsReconcilable());
        command.setIsTaxAccount(request.getIsTaxAccount());
        command.setTaxCode(request.getTaxCode());
        command.setAllowsManualEntry(request.getAllowsManualEntry());
        command.setCreditLimit(request.getCreditLimit());
        command.setNotes(request.getNotes());
        command.setTags(request.getTags());
        command.setOpeningBalance(request.getOpeningBalance());
        command.setOpeningBalanceDate(request.getOpeningBalanceDate());

        LedgerAccount account = ledgerAccountCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(account));
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get ledger account by ID")
    public ResponseEntity<LedgerAccountResponseDto> getAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId) {

        LedgerAccount account = ledgerAccountQueryService.getById(accountId);
        return ResponseEntity.ok(toDto(account));
    }

    @GetMapping("/number/{accountNumber}")
    @Operation(summary = "Get ledger account by account number")
    public ResponseEntity<LedgerAccountResponseDto> getByAccountNumber(
            @Parameter(description = "Account Number") @PathVariable String accountNumber) {

        String tenantId = RequestContextHolder.getTenantId();
        LedgerAccount account = ledgerAccountQueryService.getByAccountNumber(tenantId, accountNumber);
        return ResponseEntity.ok(toDto(account));
    }

    @GetMapping
    @Operation(summary = "Get all ledger accounts for tenant")
    public ResponseEntity<List<LedgerAccountResponseDto>> getAllAccounts() {
        String tenantId = RequestContextHolder.getTenantId();
        List<LedgerAccount> accounts = ledgerAccountQueryService.getAllForTenant(tenantId);
        return ResponseEntity.ok(accounts.stream().map(this::toDto).toList());
    }

    @GetMapping("/type/{accountType}")
    @Operation(summary = "Get ledger accounts by type")
    public ResponseEntity<List<LedgerAccountResponseDto>> getByType(
            @Parameter(description = "Account Type") @PathVariable String accountType) {

        String tenantId = RequestContextHolder.getTenantId();
        var typeEnum = LedgerAccount.AccountType.valueOf(accountType);
        List<LedgerAccount> accounts = ledgerAccountQueryService.getByType(tenantId, typeEnum);
        return ResponseEntity.ok(accounts.stream().map(this::toDto).toList());
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active accounts")
    public ResponseEntity<List<LedgerAccountResponseDto>> getActiveAccounts() {
        String tenantId = RequestContextHolder.getTenantId();
        List<LedgerAccount> accounts = ledgerAccountQueryService.getActiveAccounts(tenantId);
        return ResponseEntity.ok(accounts.stream().map(this::toDto).toList());
    }

    @GetMapping("/balance-sheet")
    @Operation(summary = "Get balance sheet accounts")
    public ResponseEntity<List<LedgerAccountResponseDto>> getBalanceSheetAccounts() {
        String tenantId = RequestContextHolder.getTenantId();
        List<LedgerAccount> accounts = ledgerAccountQueryService.getBalanceSheetAccounts(tenantId);
        return ResponseEntity.ok(accounts.stream().map(this::toDto).toList());
    }

    @GetMapping("/income-statement")
    @Operation(summary = "Get income statement accounts")
    public ResponseEntity<List<LedgerAccountResponseDto>> getIncomeStatementAccounts() {
        String tenantId = RequestContextHolder.getTenantId();
        List<LedgerAccount> accounts = ledgerAccountQueryService.getIncomeStatementAccounts(tenantId);
        return ResponseEntity.ok(accounts.stream().map(this::toDto).toList());
    }

    @PutMapping("/{accountId}")
    @Operation(summary = "Update ledger account")
    public ResponseEntity<LedgerAccountResponseDto> updateAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @Valid @RequestBody UpdateAccountRequestDto request) {

        LedgerAccountCommand.UpdateAccountCommand command = new LedgerAccountCommand.UpdateAccountCommand();
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setAccountId(accountId);
        command.setAccountName(request.getAccountName());
        command.setDescription(request.getDescription());
        command.setCostCenter(request.getCostCenter());
        command.setDepartment(request.getDepartment());
        command.setLocation(request.getLocation());
        command.setIsCashAccount(request.getIsCashAccount());
        command.setIsReconcilable(request.getIsReconcilable());
        command.setTaxCode(request.getTaxCode());
        command.setAllowsManualEntry(request.getAllowsManualEntry());
        command.setCreditLimit(request.getCreditLimit());
        command.setNotes(request.getNotes());
        command.setTags(request.getTags());

        LedgerAccount account = ledgerAccountCommandService.update(command);
        return ResponseEntity.ok(toDto(account));
    }

    @PostMapping("/{accountId}/activate")
    @Operation(summary = "Activate ledger account")
    public ResponseEntity<LedgerAccountResponseDto> activateAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId) {

        LedgerAccountCommand.ActivateAccountCommand command = new LedgerAccountCommand.ActivateAccountCommand(
            RequestContextHolder.getTenantId(), accountId);

        LedgerAccount account = ledgerAccountCommandService.activate(command);
        return ResponseEntity.ok(toDto(account));
    }

    @PostMapping("/{accountId}/freeze")
    @Operation(summary = "Freeze ledger account")
    public ResponseEntity<LedgerAccountResponseDto> freezeAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @RequestBody FreezeRequestDto request) {

        LedgerAccountCommand.FreezeAccountCommand command = new LedgerAccountCommand.FreezeAccountCommand(
            RequestContextHolder.getTenantId(), accountId, request.getReason());

        LedgerAccount account = ledgerAccountCommandService.freeze(command);
        return ResponseEntity.ok(toDto(account));
    }

    @PostMapping("/{accountId}/unfreeze")
    @Operation(summary = "Unfreeze ledger account")
    public ResponseEntity<LedgerAccountResponseDto> unfreezeAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId) {

        LedgerAccountCommand.UnfreezeAccountCommand command = new LedgerAccountCommand.UnfreezeAccountCommand(
            RequestContextHolder.getTenantId(), accountId);

        LedgerAccount account = ledgerAccountCommandService.unfreeze(command);
        return ResponseEntity.ok(toDto(account));
    }

    @PostMapping("/{accountId}/archive")
    @Operation(summary = "Archive ledger account")
    public ResponseEntity<LedgerAccountResponseDto> archiveAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @RequestBody ArchiveRequestDto request) {

        LedgerAccountCommand.ArchiveAccountCommand command = new LedgerAccountCommand.ArchiveAccountCommand(
            RequestContextHolder.getTenantId(), accountId, RequestContextHolder.getUserId().orElse("system"), request.getReason());

        LedgerAccount account = ledgerAccountCommandService.archive(command);
        return ResponseEntity.ok(toDto(account));
    }

    @PostMapping("/{accountId}/opening-balance")
    @Operation(summary = "Set opening balance for account")
    public ResponseEntity<LedgerAccountResponseDto> setOpeningBalance(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @RequestBody OpeningBalanceRequestDto request) {

        LedgerAccountCommand.SetOpeningBalanceCommand command = new LedgerAccountCommand.SetOpeningBalanceCommand(
            RequestContextHolder.getTenantId(), accountId, request.getOpeningBalance(), request.getAsOfDate());

        LedgerAccount account = ledgerAccountCommandService.setOpeningBalance(command);
        return ResponseEntity.ok(toDto(account));
    }

    @PostMapping("/{accountId}/tags")
    @Operation(summary = "Add tag to account")
    public ResponseEntity<LedgerAccountResponseDto> addTag(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @RequestBody TagRequestDto request) {

        LedgerAccountCommand.AddTagCommand command = new LedgerAccountCommand.AddTagCommand(
            RequestContextHolder.getTenantId(), accountId, request.getKey(), request.getValue());

        LedgerAccount account = ledgerAccountCommandService.addTag(command);
        return ResponseEntity.ok(toDto(account));
    }

    @DeleteMapping("/{accountId}/tags/{key}")
    @Operation(summary = "Remove tag from account")
    public ResponseEntity<LedgerAccountResponseDto> removeTag(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @Parameter(description = "Tag Key") @PathVariable String key) {

        LedgerAccountCommand.RemoveTagCommand command = new LedgerAccountCommand.RemoveTagCommand(
            RequestContextHolder.getTenantId(), accountId, key);

        LedgerAccount account = ledgerAccountCommandService.removeTag(command);
        return ResponseEntity.ok(toDto(account));
    }

    @DeleteMapping("/{accountId}")
    @Operation(summary = "Delete ledger account")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId) {

        LedgerAccountCommand.DeleteAccountCommand command = new LedgerAccountCommand.DeleteAccountCommand(
            RequestContextHolder.getTenantId(), accountId);

        ledgerAccountCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search accounts by name")
    public ResponseEntity<List<LedgerAccountResponseDto>> searchByName(
            @Parameter(description = "Search term") @RequestParam String searchTerm) {

        String tenantId = RequestContextHolder.getTenantId();
        List<LedgerAccount> accounts = ledgerAccountQueryService.searchByName(tenantId, searchTerm);
        return ResponseEntity.ok(accounts.stream().map(this::toDto).toList());
    }

    @GetMapping("/{accountId}/balance")
    @Operation(summary = "Get account balance summary")
    public ResponseEntity<LedgerAccountQueryService.AccountBalanceSummary> getBalanceSummary(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @RequestParam(required = false) LocalDate asOfDate) {

        String tenantId = RequestContextHolder.getTenantId();
        LedgerAccountQueryService.AccountBalanceSummary summary =
            ledgerAccountQueryService.getBalanceSummary(tenantId, accountId, asOfDate);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{accountId}/children")
    @Operation(summary = "Get child accounts")
    public ResponseEntity<List<LedgerAccountResponseDto>> getChildAccounts(
            @Parameter(description = "Account ID") @PathVariable String accountId) {

        String tenantId = RequestContextHolder.getTenantId();
        List<LedgerAccount> accounts = ledgerAccountQueryService.getChildAccounts(tenantId, accountId);
        return ResponseEntity.ok(accounts.stream().map(this::toDto).toList());
    }

    private LedgerAccountResponseDto toDto(LedgerAccount account) {
        return LedgerAccountResponseDto.builder()
            .id(account.getId())
            .accountId(account.getAccountId())
            .tenantId(account.getTenantId())
            .accountNumber(account.getAccountNumber())
            .accountName(account.getAccountName())
            .accountType(mapAccountType(account.getAccountType()))
            .accountSubType(mapAccountSubType(account.getAccountSubType()))
            .parentAccountId(account.getParentAccountId())
            .accountLevel(account.getAccountLevel())
            .status(mapAccountStatus(account.getStatus()))
            .currency(account.getCurrency())
            .currentBalance(account.getCurrentBalance())
            .debitBalance(account.getDebitBalance())
            .creditBalance(account.getCreditBalance())
            .openingBalance(account.getOpeningBalance())
            .openingBalanceDate(account.getOpeningBalanceDate())
            .description(account.getDescription())
            .costCenter(account.getCostCenter())
            .department(account.getDepartment())
            .location(account.getLocation())
            .isCashAccount(account.getIsCashAccount())
            .isReconcilable(account.getIsReconcilable())
            .isTaxAccount(account.getIsTaxAccount())
            .taxCode(account.getTaxCode())
            .allowsManualEntry(account.getAllowsManualEntry())
            .normalBalanceSide(account.getNormalBalanceSide())
            .createdByUserId(account.getCreatedByUserId())
            .lastReconciledAt(toInstant(account.getLastReconciledAt()))
            .lastReconciledBy(account.getLastReconciledBy())
            .tags(account.getTags() != null ? account.getTags().stream()
                .map(tag -> LedgerAccountResponseDto.AccountTagDto.builder()
                    .key(tag.getKey())
                    .value(tag.getValue())
                    .build())
                .toList() : null)
            .creditLimit(account.getCreditLimit())
            .notes(account.getNotes())
            .archivedAt(toInstant(account.getArchivedAt()))
            .archivedBy(account.getArchivedBy())
            .archivedReason(account.getArchivedReason())
            .createdAt(account.getCreatedAt())
            .updatedAt(account.getUpdatedAt())
            .build();
    }

    private LedgerAccountResponseDto.AccountTypeDto mapAccountType(LedgerAccount.AccountType type) {
        return type != null ? LedgerAccountResponseDto.AccountTypeDto.valueOf(type.name()) : null;
    }

    private LedgerAccountResponseDto.AccountSubTypeDto mapAccountSubType(LedgerAccount.AccountSubType subType) {
        return subType != null ? LedgerAccountResponseDto.AccountSubTypeDto.valueOf(subType.name()) : null;
    }

    private LedgerAccountResponseDto.AccountStatusDto mapAccountStatus(LedgerAccount.AccountStatus status) {
        return status != null ? LedgerAccountResponseDto.AccountStatusDto.valueOf(status.name()) : null;
    }

    private Instant toInstant(java.time.LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atZone(ZoneId.systemDefault()).toInstant() : null;
    }

    // Request DTOs
    @lombok.Data
    public static class CreateAccountRequestDto {
        public String accountNumber;
        public String accountName;
        public LedgerAccount.AccountType accountType;
        public LedgerAccount.AccountSubType accountSubType;
        public String currency;
        public String parentAccountId;
        public Integer accountLevel;
        public String description;
        public String costCenter;
        public String department;
        public String location;
        public Boolean isCashAccount;
        public Boolean isReconcilable;
        public Boolean isTaxAccount;
        public String taxCode;
        public Boolean allowsManualEntry;
        public BigDecimal creditLimit;
        public String notes;
        public List<LedgerAccount.AccountTag> tags;
        public BigDecimal openingBalance;
        public LocalDate openingBalanceDate;
    }

    @lombok.Data
    public static class UpdateAccountRequestDto {
        public String accountName;
        public String description;
        public String costCenter;
        public String department;
        public String location;
        public Boolean isCashAccount;
        public Boolean isReconcilable;
        public String taxCode;
        public Boolean allowsManualEntry;
        public BigDecimal creditLimit;
        public String notes;
        public List<LedgerAccount.AccountTag> tags;
    }

    @lombok.Data
    public static class FreezeRequestDto {
        public String reason;
    }

    @lombok.Data
    public static class ArchiveRequestDto {
        public String reason;
    }

    @lombok.Data
    public static class OpeningBalanceRequestDto {
        public BigDecimal openingBalance;
        public LocalDate asOfDate;
    }

    @lombok.Data
    public static class TagRequestDto {
        public String key;
        public String value;
    }
}
