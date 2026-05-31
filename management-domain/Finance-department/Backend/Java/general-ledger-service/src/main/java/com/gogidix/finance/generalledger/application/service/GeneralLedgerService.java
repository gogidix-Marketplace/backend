package com.gogidix.finance.generalledger.application.service;

import com.gogidix.finance.generalledger.domain.repository.JournalEntryRepository;
import com.gogidix.finance.generalledger.domain.repository.LedgerAccountRepository;
import com.gogidix.finance.generalledger.shared.exception.NotFoundException;
import com.gogidix.finance.generalledger.shared.exception.ValidationException;
import com.gogidix.finance.generalledger.shared.requestcontext.RequestContextHolder;
import com.gogidix.finance.ledger.domain.model.JournalEntry;
import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import com.gogidix.finance.ledger.domain.port.in.JournalEntryCommand;
import com.gogidix.finance.ledger.domain.port.in.LedgerAccountQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * General Ledger Service
 * Orchestrates high-level ledger operations including:
 * - Period closing
 * - Trial balance generation
 * - Financial statement preparation
 * - Account reconciliation
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GeneralLedgerService {

    private final JournalEntryRepository journalEntryRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final JournalEntryCommandService journalEntryCommandService;
    private final LedgerAccountCommandService ledgerAccountCommandService;
    private final JournalEntryQueryService journalEntryQueryService;
    private final LedgerAccountQueryService ledgerAccountQueryService;

    /**
     * Creates a journal entry and posts it immediately if autoPost is true
     */
    @Transactional
    @CacheEvict(value = {"journalEntries", "ledgerAccounts", "trialBalance"}, allEntries = true)
    public JournalEntry createAndPostJournalEntry(
            JournalEntryCommand.CreateJournalEntryCommand command,
            boolean autoPost) {

        log.info("Creating and auto-posting journal entry for tenant: {}", command.getTenantId());

        // Create the journal entry using command service
        JournalEntry entry = journalEntryCommandService.create(command);

        if (autoPost) {
            // Submit for approval
            JournalEntryCommand.SubmitForApprovalCommand submitCommand =
                new JournalEntryCommand.SubmitForApprovalCommand(command.getTenantId(), entry.getJournalEntryId());
            journalEntryCommandService.submitForApproval(submitCommand);

            // Approve
            JournalEntryCommand.ApproveJournalEntryCommand approveCommand =
                new JournalEntryCommand.ApproveJournalEntryCommand(
                    command.getTenantId(),
                    entry.getJournalEntryId(),
                    command.getCreatedBy()
                );
            journalEntryCommandService.approve(approveCommand);

            // Post
            JournalEntryCommand.PostJournalEntryCommand postCommand =
                new JournalEntryCommand.PostJournalEntryCommand(
                    command.getTenantId(),
                    entry.getJournalEntryId(),
                    command.getCreatedBy()
                );
            journalEntryCommandService.post(postCommand);
        }

        return entry;
    }

    /**
     * Closes the accounting period by creating closing entries
     */
    @Transactional
    @CacheEvict(value = {"journalEntries", "ledgerAccounts", "trialBalance"}, allEntries = true)
    public PeriodCloseResult closePeriod(String tenantId, Integer fiscalYear, Integer fiscalPeriod,
                                         LocalDate closingDate, String closedBy) {

        log.info("Closing period: {}-{} for tenant: {}", fiscalYear, fiscalPeriod, tenantId);

        // Verify all entries for the period are posted
        List<JournalEntry> periodEntries = journalEntryRepository
            .findByTenantIdAndFiscalPeriod(tenantId, fiscalYear, fiscalPeriod);

        for (JournalEntry entry : periodEntries) {
            if (entry.getStatus() != JournalEntry.JournalEntryStatus.POSTED) {
                throw new ValidationException("Cannot close period with unposted entries. " +
                    "Entry " + entry.getEntryNumber() + " is not posted.");
            }
        }

        // Get income and expense accounts
        List<LedgerAccount> revenueAccounts = ledgerAccountRepository
            .findByTenantIdAndAccountType(tenantId, LedgerAccount.AccountType.REVENUE);
        List<LedgerAccount> expenseAccounts = ledgerAccountRepository
            .findByTenantIdAndAccountType(tenantId, LedgerAccount.AccountType.EXPENSE);

        // Calculate net income/loss
        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (LedgerAccount account : revenueAccounts) {
            if (account.getCurrentBalance() != null) {
                totalRevenue = totalRevenue.add(account.getCurrentBalance());
            }
        }

        BigDecimal totalExpenses = BigDecimal.ZERO;
        for (LedgerAccount account : expenseAccounts) {
            if (account.getCurrentBalance() != null) {
                totalExpenses = totalExpenses.add(account.getCurrentBalance());
            }
        }

        BigDecimal netIncome = totalRevenue.subtract(totalExpenses);

        // Get retained earnings account
        LedgerAccount retainedEarningsAccount = ledgerAccountRepository
            .findByTenantIdAndAccountSubType(tenantId, LedgerAccount.AccountSubType.RETAINED_EARNINGS)
            .stream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Retained Earnings account not found"));

        // Create closing entry
        List<JournalEntryCommand.JournalEntryLineDto> closingLines = new ArrayList<>();

        // Close revenue accounts (debit to reduce)
        for (LedgerAccount account : revenueAccounts) {
            if (account.getCurrentBalance() != null && account.getCurrentBalance().compareTo(BigDecimal.ZERO) != 0) {
                closingLines.add(createClosingLine(account, account.getCurrentBalance(), BigDecimal.ZERO));
            }
        }

        // Close expense accounts (credit to reduce)
        for (LedgerAccount account : expenseAccounts) {
            if (account.getCurrentBalance() != null && account.getCurrentBalance().compareTo(BigDecimal.ZERO) != 0) {
                closingLines.add(createClosingLine(account, BigDecimal.ZERO, account.getCurrentBalance()));
            }
        }

        // Add retained earnings line
        if (netIncome.compareTo(BigDecimal.ZERO) >= 0) {
            // Net income - credit retained earnings
            closingLines.add(createClosingLine(
                retainedEarningsAccount,
                BigDecimal.ZERO,
                netIncome
            ));
        } else {
            // Net loss - debit retained earnings
            closingLines.add(createClosingLine(
                retainedEarningsAccount,
                netIncome.abs(),
                BigDecimal.ZERO
            ));
        }

        if (!closingLines.isEmpty()) {
            JournalEntryCommand.CreateJournalEntryCommand closingCommand =
                new JournalEntryCommand.CreateJournalEntryCommand();

            closingCommand.setTenantId(tenantId);
            closingCommand.setEntryDate(closingDate);
            closingCommand.setDescription("Period Closing Entry for " + fiscalYear + "-" + fiscalPeriod);
            closingCommand.setCurrency(revenueAccounts.isEmpty() ? "USD" : revenueAccounts.get(0).getCurrency());
            closingCommand.setCreatedBy(closedBy);
            closingCommand.setCreatedByName("System");
            closingCommand.setFiscalYear(fiscalYear);
            closingCommand.setFiscalPeriod(fiscalPeriod);
            closingCommand.setLines(closingLines);
            closingCommand.setRequiresApproval(false);

            JournalEntry closingEntry = journalEntryCommandService.create(closingCommand);

            // Post the closing entry
            JournalEntryCommand.SubmitForApprovalCommand submitCommand =
                new JournalEntryCommand.SubmitForApprovalCommand(tenantId, closingEntry.getJournalEntryId());
            journalEntryCommandService.submitForApproval(submitCommand);

            JournalEntryCommand.ApproveJournalEntryCommand approveCommand =
                new JournalEntryCommand.ApproveJournalEntryCommand(tenantId, closingEntry.getJournalEntryId(), closedBy);
            journalEntryCommandService.approve(approveCommand);

            JournalEntryCommand.PostJournalEntryCommand postCommand =
                new JournalEntryCommand.PostJournalEntryCommand(tenantId, closingEntry.getJournalEntryId(), closedBy);
            journalEntryCommandService.post(postCommand);

            log.info("Period closed successfully. Closing entry: {}", closingEntry.getJournalEntryId());

            return PeriodCloseResult.builder()
                .success(true)
                .closingEntryId(closingEntry.getJournalEntryId())
                .closingEntryNumber(closingEntry.getEntryNumber())
                .fiscalYear(fiscalYear)
                .fiscalPeriod(fiscalPeriod)
                .totalRevenue(totalRevenue)
                .totalExpenses(totalExpenses)
                .netIncome(netIncome)
                .closedAt(java.time.LocalDateTime.now())
                .build();
        }

        throw new ValidationException("No accounts to close");
    }

    /**
     * Reconciles an account balance
     */
    @Transactional
    @CacheEvict(value = "ledgerAccounts", allEntries = true)
    public void reconcileAccount(String accountId, String reconciledBy, String statementBalance) {
        String tenantId = RequestContextHolder.getTenantId();

        log.info("Reconciling account: {} for tenant: {}", accountId, tenantId);

        LedgerAccount account = ledgerAccountRepository.findByAccountIdAndTenantId(accountId, tenantId)
            .orElseThrow(() -> new NotFoundException("LedgerAccount", accountId));

        if (!account.getIsReconcilable()) {
            throw new ValidationException("Account " + account.getAccountNumber() + " is not reconcilable");
        }

        account.markAsReconciled(reconciledBy);
        ledgerAccountRepository.save(account);

        log.info("Account reconciled: {}", accountId);
    }

    /**
     * Generates a balance sheet as of a given date
     */
    public BalanceSheetResult generateBalanceSheet(String tenantId, LocalDate asOfDate) {
        log.info("Generating balance sheet for tenant: {} as of: {}", tenantId, asOfDate);

        List<LedgerAccount> assetAccounts = ledgerAccountRepository
            .findByTenantIdAndAccountType(tenantId, LedgerAccount.AccountType.ASSET);
        List<LedgerAccount> liabilityAccounts = ledgerAccountRepository
            .findByTenantIdAndAccountType(tenantId, LedgerAccount.AccountType.LIABILITY);
        List<LedgerAccount> equityAccounts = ledgerAccountRepository
            .findByTenantIdAndAccountType(tenantId, LedgerAccount.AccountType.EQUITY);

        BigDecimal totalAssets = sumActiveAccountBalances(assetAccounts);
        BigDecimal totalLiabilities = sumActiveAccountBalances(liabilityAccounts);
        BigDecimal totalEquity = sumActiveAccountBalances(equityAccounts);

        return BalanceSheetResult.builder()
            .asOfDate(asOfDate)
            .totalAssets(totalAssets)
            .totalLiabilities(totalLiabilities)
            .totalEquity(totalEquity)
            .liabilitiesPlusEquity(totalLiabilities.add(totalEquity))
            .isBalanced(totalAssets.compareTo(totalLiabilities.add(totalEquity)) == 0)
            .assetAccounts(filterActiveAccounts(assetAccounts))
            .liabilityAccounts(filterActiveAccounts(liabilityAccounts))
            .equityAccounts(filterActiveAccounts(equityAccounts))
            .build();
    }

    /**
     * Generates an income statement for a period
     */
    public IncomeStatementResult generateIncomeStatement(String tenantId, LocalDate startDate, LocalDate endDate) {
        log.info("Generating income statement for tenant: {} from {} to {}", tenantId, startDate, endDate);

        List<LedgerAccount> revenueAccounts = ledgerAccountRepository
            .findByTenantIdAndAccountType(tenantId, LedgerAccount.AccountType.REVENUE);
        List<LedgerAccount> expenseAccounts = ledgerAccountRepository
            .findByTenantIdAndAccountType(tenantId, LedgerAccount.AccountType.EXPENSE);

        BigDecimal totalRevenue = sumActiveAccountBalances(revenueAccounts);
        BigDecimal totalExpenses = sumActiveAccountBalances(expenseAccounts);
        BigDecimal netIncome = totalRevenue.subtract(totalExpenses);

        return IncomeStatementResult.builder()
            .startDate(startDate)
            .endDate(endDate)
            .totalRevenue(totalRevenue)
            .totalExpenses(totalExpenses)
            .netIncome(netIncome)
            .profitMargin(totalRevenue.compareTo(BigDecimal.ZERO) > 0
                ? netIncome.divide(totalRevenue, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
                : BigDecimal.ZERO)
            .revenueAccounts(filterActiveAccounts(revenueAccounts))
            .expenseAccounts(filterActiveAccounts(expenseAccounts))
            .build();
    }

    /**
     * Validates the general ledger for errors
     */
    public LedgerValidationResult validateLedger(String tenantId) {
        log.info("Validating ledger for tenant: {}", tenantId);

        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // Check trial balance
        LedgerAccountQuery.TrialBalance trialBalance = ledgerAccountQueryService.getTrialBalance(tenantId, LocalDate.now());
        if (!trialBalance.getIsBalanced()) {
            errors.add("Trial balance is not balanced. Difference: " + trialBalance.getDifference());
        }

        // Check for accounts with zero opening balance but non-zero current balance
        List<LedgerAccount> allAccounts = ledgerAccountRepository.findByTenantId(tenantId);
        for (LedgerAccount account : allAccounts) {
            if (account.getOpeningBalance() == null || account.getOpeningBalance().compareTo(BigDecimal.ZERO) == 0) {
                if (account.getCurrentBalance() != null && account.getCurrentBalance().compareTo(BigDecimal.ZERO) != 0) {
                    warnings.add("Account " + account.getAccountNumber() + " has zero opening balance but non-zero current balance");
                }
            }
        }

        return LedgerValidationResult.builder()
            .isValid(errors.isEmpty())
            .errors(errors)
            .warnings(warnings)
            .validatedAt(java.time.LocalDateTime.now())
            .build();
    }

    private JournalEntryCommand.JournalEntryLineDto createClosingLine(LedgerAccount account,
                                                                       BigDecimal debitAmount,
                                                                       BigDecimal creditAmount) {
        JournalEntryCommand.JournalEntryLineDto line = new JournalEntryCommand.JournalEntryLineDto();
        line.setAccountId(account.getAccountId());
        line.setAccountNumber(account.getAccountNumber());
        line.setAccountName(account.getAccountName());
        line.setDebitAmount(debitAmount);
        line.setCreditAmount(creditAmount);
        line.setDescription("Closing entry for " + account.getAccountNumber());
        return line;
    }

    private BigDecimal sumActiveAccountBalances(List<LedgerAccount> accounts) {
        return accounts.stream()
            .filter(a -> a.getStatus() == LedgerAccount.AccountStatus.ACTIVE)
            .map(a -> a.getCurrentBalance() != null ? a.getCurrentBalance() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<AccountBalanceInfo> filterActiveAccounts(List<LedgerAccount> accounts) {
        return accounts.stream()
            .filter(a -> a.getStatus() == LedgerAccount.AccountStatus.ACTIVE)
            .map(a -> AccountBalanceInfo.builder()
                .accountId(a.getAccountId())
                .accountNumber(a.getAccountNumber())
                .accountName(a.getAccountName())
                .balance(a.getCurrentBalance() != null ? a.getCurrentBalance() : BigDecimal.ZERO)
                .currency(a.getCurrency())
                .build())
            .toList();
    }

    // Result DTOs
    @lombok.Builder
    @lombok.Data
    public static class PeriodCloseResult {
        private boolean success;
        private String closingEntryId;
        private String closingEntryNumber;
        private Integer fiscalYear;
        private Integer fiscalPeriod;
        private BigDecimal totalRevenue;
        private BigDecimal totalExpenses;
        private BigDecimal netIncome;
        private java.time.LocalDateTime closedAt;
    }

    @lombok.Builder
    @lombok.Data
    public static class BalanceSheetResult {
        private LocalDate asOfDate;
        private BigDecimal totalAssets;
        private BigDecimal totalLiabilities;
        private BigDecimal totalEquity;
        private BigDecimal liabilitiesPlusEquity;
        private boolean isBalanced;
        private List<AccountBalanceInfo> assetAccounts;
        private List<AccountBalanceInfo> liabilityAccounts;
        private List<AccountBalanceInfo> equityAccounts;
    }

    @lombok.Builder
    @lombok.Data
    public static class IncomeStatementResult {
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal totalRevenue;
        private BigDecimal totalExpenses;
        private BigDecimal netIncome;
        private BigDecimal profitMargin;
        private List<AccountBalanceInfo> revenueAccounts;
        private List<AccountBalanceInfo> expenseAccounts;
    }

    @lombok.Builder
    @lombok.Data
    public static class LedgerValidationResult {
        private boolean isValid;
        private List<String> errors;
        private List<String> warnings;
        private java.time.LocalDateTime validatedAt;
    }

    @lombok.Builder
    @lombok.Data
    public static class AccountBalanceInfo {
        private String accountId;
        private String accountNumber;
        private String accountName;
        private BigDecimal balance;
        private String currency;
    }
}
