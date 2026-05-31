package com.gogidix.finance.generalledger.interfaces.rest;

import com.gogidix.finance.generalledger.application.dto.response.JournalEntryResponseDto;
import com.gogidix.finance.generalledger.application.dto.response.LedgerAccountResponseDto;
import com.gogidix.finance.generalledger.application.dto.response.LedgerBalanceDto;
import com.gogidix.finance.generalledger.application.dto.response.TrialBalanceDto;
import com.gogidix.finance.generalledger.application.service.GeneralLedgerService;
import com.gogidix.finance.generalledger.application.service.JournalEntryCommandService;
import com.gogidix.finance.generalledger.application.service.LedgerAccountQueryService;
import com.gogidix.finance.generalledger.shared.requestcontext.RequestContextHolder;
import com.gogidix.finance.ledger.domain.model.JournalEntry;
import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import com.gogidix.finance.ledger.domain.port.in.JournalEntryCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * General Ledger REST Controller
 * Handles high-level ledger operations and reports
 */
@RestController
@RequestMapping("/api/general-ledger")
@RequiredArgsConstructor
@Tag(name = "General Ledger", description = "General ledger operations and reports")
public class GeneralLedgerController {

    private final GeneralLedgerService generalLedgerService;
    private final JournalEntryCommandService journalEntryCommandService;
    private final LedgerAccountQueryService ledgerAccountQueryService;

    @PostMapping("/journal-entries/create-and-post")
    @Operation(summary = "Create and post journal entry in one operation")
    public ResponseEntity<JournalEntryResponseDto> createAndPostJournalEntry(
            @Valid @RequestBody CreateAndPostRequestDto request) {

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
        command.setLines(request.getLines());

        JournalEntry entry = generalLedgerService.createAndPostJournalEntry(command, true);
        return ResponseEntity.ok(toJournalEntryDto(entry));
    }

    @PostMapping("/periods/close")
    @Operation(summary = "Close an accounting period")
    public ResponseEntity<GeneralLedgerService.PeriodCloseResult> closePeriod(
            @Valid @RequestBody ClosePeriodRequestDto request) {

        GeneralLedgerService.PeriodCloseResult result = generalLedgerService.closePeriod(
            RequestContextHolder.getTenantId(),
            request.getFiscalYear(),
            request.getFiscalPeriod(),
            request.getClosingDate(),
            RequestContextHolder.getUserId().orElse("system")
        );

        return ResponseEntity.ok(result);
    }

    @PostMapping("/accounts/{accountId}/reconcile")
    @Operation(summary = "Reconcile an account")
    public ResponseEntity<Void> reconcileAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @RequestBody ReconcileAccountRequestDto request) {

        generalLedgerService.reconcileAccount(accountId, RequestContextHolder.getUserId().orElse("system"),
            request.getStatementBalance());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/reports/balance-sheet")
    @Operation(summary = "Generate balance sheet report")
    public ResponseEntity<GeneralLedgerService.BalanceSheetResult> generateBalanceSheet(
            @Parameter(description = "As of date") @RequestParam LocalDate asOfDate) {

        GeneralLedgerService.BalanceSheetResult result =
            generalLedgerService.generateBalanceSheet(RequestContextHolder.getTenantId(), asOfDate);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/reports/income-statement")
    @Operation(summary = "Generate income statement report")
    public ResponseEntity<GeneralLedgerService.IncomeStatementResult> generateIncomeStatement(
            @Parameter(description = "Start date") @RequestParam LocalDate startDate,
            @Parameter(description = "End date") @RequestParam LocalDate endDate) {

        GeneralLedgerService.IncomeStatementResult result =
            generalLedgerService.generateIncomeStatement(RequestContextHolder.getTenantId(), startDate, endDate);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/reports/trial-balance")
    @Operation(summary = "Generate trial balance report")
    public ResponseEntity<TrialBalanceDto> getTrialBalance(
            @Parameter(description = "As of date") @RequestParam(required = false) LocalDate asOfDate) {

        String tenantId = RequestContextHolder.getTenantId();
        LocalDate date = asOfDate != null ? asOfDate : LocalDate.now();

        LedgerAccountQueryService.TrialBalance trialBalance =
            ledgerAccountQueryService.getTrialBalance(tenantId, date);

        TrialBalanceDto dto = TrialBalanceDto.builder()
            .asOfDate(trialBalance.getAsOfDate())
            .currency(trialBalance.getCurrency())
            .totalDebits(trialBalance.getTotalDebits())
            .totalCredits(trialBalance.getTotalCredits())
            .isBalanced(trialBalance.getIsBalanced())
            .difference(trialBalance.getDifference())
            .accountDetails(trialBalance.getAccountDetails().stream()
                .map(detail -> TrialBalanceDto.AccountBalanceDetailDto.builder()
                    .accountId(detail.getAccountId())
                    .accountNumber(detail.getAccountNumber())
                    .accountName(detail.getAccountName())
                    .accountType(mapAccountType(detail.getAccountType()))
                    .accountSubType(mapAccountSubType(detail.getAccountSubType()))
                    .debitBalance(detail.getDebitBalance())
                    .creditBalance(detail.getCreditBalance())
                    .netBalance(calculateNetBalance(detail.getDebitBalance(), detail.getCreditBalance()))
                    .currency(detail.getCurrency())
                    .status(mapAccountStatus(detail.getStatus()))
                    .build())
                .toList())
            .generatedAt(java.time.Instant.now())
            .totalAccounts(trialBalance.getAccountDetails().size())
            .build();

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/reports/chart-of-accounts")
    @Operation(summary = "Get chart of accounts")
    public ResponseEntity<LedgerAccountQueryService.ChartOfAccounts> getChartOfAccounts() {

        LedgerAccountQueryService.ChartOfAccounts chart =
            ledgerAccountQueryService.getChartOfAccounts(RequestContextHolder.getTenantId());

        return ResponseEntity.ok(chart);
    }

    @GetMapping("/accounts/{accountId}/balance-detail")
    @Operation(summary = "Get detailed balance for an account")
    public ResponseEntity<LedgerBalanceDto> getAccountBalanceDetail(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @RequestParam(required = false) LocalDate asOfDate) {

        String tenantId = RequestContextHolder.getTenantId();
        LocalDate date = asOfDate != null ? asOfDate : LocalDate.now();

        LedgerAccount account = ledgerAccountQueryService.getById(accountId);

        LedgerBalanceDto dto = LedgerBalanceDto.builder()
            .accountId(account.getAccountId())
            .accountNumber(account.getAccountNumber())
            .accountName(account.getAccountName())
            .accountType(mapAccountType(account.getAccountType()))
            .currentBalance(account.getCurrentBalance())
            .debitBalance(account.getDebitBalance())
            .creditBalance(account.getCreditBalance())
            .openingBalance(account.getOpeningBalance())
            .currency(account.getCurrency())
            .asOfDate(date)
            .normalBalanceSide(account.getNormalBalanceSide())
            .isReconciled(account.getLastReconciledAt() != null)
            .lastReconciledAt(toInstant(account.getLastReconciledAt()))
            .balanceStatus(calculateBalanceStatus(account.getCurrentBalance()))
            .build();

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate the general ledger")
    public ResponseEntity<GeneralLedgerService.LedgerValidationResult> validateLedger() {

        GeneralLedgerService.LedgerValidationResult result =
            generalLedgerService.validateLedger(RequestContextHolder.getTenantId());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/fiscal-years")
    @Operation(summary = "Get fiscal years with data")
    public ResponseEntity<List<FiscalYearDto>> getFiscalYears() {
        // This would typically query a fiscal periods table
        // For now, return current year
        int currentYear = LocalDate.now().getYear();
        FiscalYearDto dto = new FiscalYearDto();
        dto.fiscalYear = currentYear;
        dto.isClosed = false;
        dto.startDate = LocalDate.of(currentYear, 1, 1);
        dto.endDate = LocalDate.of(currentYear, 12, 31);

        return ResponseEntity.ok(List.of(dto));
    }

    @GetMapping("/fiscal-years/{year}/periods")
    @Operation(summary = "Get fiscal periods for a year")
    public ResponseEntity<List<FiscalPeriodDto>> getFiscalPeriods(
            @Parameter(description = "Fiscal Year") @PathVariable int year) {

        List<FiscalPeriodDto> periods = new java.util.ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            FiscalPeriodDto period = new FiscalPeriodDto();
            period.fiscalYear = year;
            period.fiscalPeriod = i;
            period.periodName = getMonthName(i);
            period.isClosed = false;
            periods.add(period);
        }

        return ResponseEntity.ok(periods);
    }

    private String getMonthName(int month) {
        return java.time.Month.of(month).name();
    }

    private String calculateBalanceStatus(BigDecimal balance) {
        if (balance == null || balance.compareTo(BigDecimal.ZERO) == 0) {
            return "ZERO";
        } else if (balance.compareTo(BigDecimal.ZERO) > 0) {
            return "POSITIVE";
        } else {
            return "NEGATIVE";
        }
    }

    private BigDecimal calculateNetBalance(BigDecimal debit, BigDecimal credit) {
        BigDecimal d = debit != null ? debit : BigDecimal.ZERO;
        BigDecimal c = credit != null ? credit : BigDecimal.ZERO;
        return d.subtract(c);
    }

    private Instant toInstant(java.time.LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atZone(ZoneId.systemDefault()).toInstant() : null;
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

    private JournalEntryResponseDto toJournalEntryDto(JournalEntry entry) {
        return JournalEntryResponseDto.builder()
            .id(entry.getId())
            .journalEntryId(entry.getJournalEntryId())
            .tenantId(entry.getTenantId())
            .entryNumber(entry.getEntryNumber())
            .entryDate(entry.getEntryDate())
            .postingDate(entry.getPostingDate() != null ? entry.getPostingDate().atZone(ZoneId.systemDefault()).toInstant() : null)
            .status(JournalEntryResponseDto.JournalEntryStatusDto.valueOf(entry.getStatus().name()))
            .description(entry.getDescription())
            .reference(entry.getReference())
            .totalDebit(entry.getTotalDebit())
            .totalCredit(entry.getTotalCredit())
            .currency(entry.getCurrency())
            .createdAt(entry.getCreatedAt())
            .updatedAt(entry.getUpdatedAt())
            .build();
    }

    // Inner DTOs for requests
    @lombok.Data
    public static class CreateAndPostRequestDto {
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
        public List<JournalEntryCommand.JournalEntryLineDto> lines;
    }

    @lombok.Data
    public static class ClosePeriodRequestDto {
        public Integer fiscalYear;
        public Integer fiscalPeriod;
        public LocalDate closingDate;
    }

    @lombok.Data
    public static class ReconcileAccountRequestDto {
        public String statementBalance;
    }

    @lombok.Data
    public static class FiscalYearDto {
        public int fiscalYear;
        public boolean isClosed;
        public LocalDate startDate;
        public LocalDate endDate;
    }

    @lombok.Data
    public static class FiscalPeriodDto {
        public int fiscalYear;
        public int fiscalPeriod;
        public String periodName;
        public boolean isClosed;
    }
}
