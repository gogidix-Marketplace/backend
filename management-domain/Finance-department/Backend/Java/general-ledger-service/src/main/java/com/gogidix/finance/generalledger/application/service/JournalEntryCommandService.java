package com.gogidix.finance.generalledger.application.service;

import com.gogidix.finance.generalledger.domain.repository.JournalEntryRepository;
import com.gogidix.finance.generalledger.domain.repository.LedgerAccountRepository;
import com.gogidix.finance.generalledger.shared.exception.ConflictException;
import com.gogidix.finance.generalledger.shared.exception.NotFoundException;
import com.gogidix.finance.generalledger.shared.exception.ValidationException;
import com.gogidix.finance.generalledger.shared.requestcontext.RequestContextHolder;
import com.gogidix.finance.ledger.domain.model.JournalEntry;
import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import com.gogidix.finance.ledger.domain.model.LedgerTransaction;
import com.gogidix.finance.ledger.domain.port.in.JournalEntryCommand;
import com.gogidix.finance.ledger.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Journal Entry Command Service
 * Handles all write operations for journal entries
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JournalEntryCommandService {

    private final JournalEntryRepository journalEntryRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "journalEntries", allEntries = true),
        @CacheEvict(value = "trialBalance", allEntries = true)
    })
    public JournalEntry create(JournalEntryCommand.CreateJournalEntryCommand command) {
        log.info("Creating journal entry for tenant: {}, date: {}",
            command.getTenantId(), command.getEntryDate());

        validateLines(command.getLines());

        JournalEntry entry = JournalEntry.create(
            command.getTenantId(),
            command.getEntryDate(),
            command.getDescription(),
            command.getCurrency(),
            command.getCreatedBy(),
            command.getCreatedByName() != null ? command.getCreatedByName() : command.getCreatedBy()
        );

        entry.setReference(command.getReference());
        entry.setSourceDocumentType(command.getSourceDocumentType());
        entry.setSourceDocumentId(command.getSourceDocumentId());
        entry.setSourceModule(command.getSourceModule());
        entry.setPeriodId(command.getPeriodId());
        entry.setFiscalYear(command.getFiscalYear());
        entry.setFiscalPeriod(command.getFiscalPeriod());
        entry.setRequiresApproval(command.getRequiresApproval() != null ? command.getRequiresApproval() : false);
        entry.setNotes(command.getNotes());
        entry.setBatchId(command.getBatchId());

        if (command.getExchangeRate() != null) {
            entry.setExchangeRate(command.getExchangeRate());
        }
        if (command.getBaseCurrency() != null) {
            entry.setBaseCurrency(command.getBaseCurrency());
        }

        // Add lines and validate accounts
        for (JournalEntryCommand.JournalEntryLineDto lineDto : command.getLines()) {
            LedgerAccount account = getAndValidateAccount(command.getTenantId(), lineDto.getAccountId());

            entry.addLine(
                lineDto.getAccountId(),
                account.getAccountNumber(),
                account.getAccountName(),
                lineDto.getDebitAmount(),
                lineDto.getCreditAmount(),
                lineDto.getDescription()
            );
        }

        // Generate entry number
        String entryNumber = journalEntryRepository.generateNextEntryNumber(command.getTenantId());
        entry.setEntryNumber(entryNumber);

        JournalEntry savedEntry = journalEntryRepository.save(entry);
        publishEvents(savedEntry);

        log.info("Created journal entry: {} for tenant: {}", savedEntry.getJournalEntryId(), command.getTenantId());
        return savedEntry;
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "journalEntries", allEntries = true),
        @CacheEvict(value = "trialBalance", allEntries = true)
    })
    public JournalEntry update(JournalEntryCommand.UpdateJournalEntryCommand command) {
        log.info("Updating journal entry: {} for tenant: {}", command.getJournalEntryId(), command.getTenantId());

        JournalEntry entry = journalEntryRepository.findByJournalEntryIdAndTenantId(
            command.getJournalEntryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("JournalEntry", command.getJournalEntryId()));

        if (!entry.canModify()) {
            throw new ValidationException("Can only update draft journal entries");
        }

        if (command.getEntryDate() != null) {
            entry.setEntryDate(command.getEntryDate());
        }
        if (command.getDescription() != null) {
            entry.setDescription(command.getDescription());
        }
        if (command.getReference() != null) {
            entry.setReference(command.getReference());
        }
        if (command.getNotes() != null) {
            entry.setNotes(command.getNotes());
        }

        if (command.getLines() != null && !command.getLines().isEmpty()) {
            // Clear existing lines and add new ones
            entry.getLines().clear();

            for (JournalEntryCommand.JournalEntryLineDto lineDto : command.getLines()) {
                LedgerAccount account = getAndValidateAccount(command.getTenantId(), lineDto.getAccountId());

                entry.addLine(
                    lineDto.getAccountId(),
                    account.getAccountNumber(),
                    account.getAccountName(),
                    lineDto.getDebitAmount(),
                    lineDto.getCreditAmount(),
                    lineDto.getDescription()
                );
            }
        }

        JournalEntry savedEntry = journalEntryRepository.save(entry);
        publishEvents(savedEntry);

        return savedEntry;
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "journalEntries", allEntries = true),
        @CacheEvict(value = "trialBalance", allEntries = true)
    })
    public void addLine(JournalEntryCommand.AddLineCommand command) {
        log.info("Adding line to journal entry: {} for tenant: {}",
            command.getJournalEntryId(), command.getTenantId());

        JournalEntry entry = journalEntryRepository.findByJournalEntryIdAndTenantId(
            command.getJournalEntryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("JournalEntry", command.getJournalEntryId()));

        if (!entry.canModify()) {
            throw new ValidationException("Can only modify draft journal entries");
        }

        LedgerAccount account = getAndValidateAccount(command.getTenantId(), command.getAccountId());

        entry.addLine(
            command.getAccountId(),
            account.getAccountNumber(),
            account.getAccountName(),
            command.getDebitAmount(),
            command.getCreditAmount(),
            command.getDescription()
        );

        journalEntryRepository.save(entry);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "journalEntries", allEntries = true),
        @CacheEvict(value = "trialBalance", allEntries = true)
    })
    public void updateLine(JournalEntryCommand.UpdateLineCommand command) {
        log.info("Updating line: {} in journal entry: {}",
            command.getLineId(), command.getJournalEntryId());

        String tenantId = RequestContextHolder.getTenantId();

        JournalEntry entry = journalEntryRepository.findByJournalEntryIdAndTenantId(
            command.getJournalEntryId(), tenantId)
            .orElseThrow(() -> new NotFoundException("JournalEntry", command.getJournalEntryId()));

        if (!entry.canModify()) {
            throw new ValidationException("Can only modify draft journal entries");
        }

        entry.updateLine(command.getLineId(), command.getDebitAmount(),
            command.getCreditAmount(), command.getDescription());

        journalEntryRepository.save(entry);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "journalEntries", allEntries = true),
        @CacheEvict(value = "trialBalance", allEntries = true)
    })
    public void removeLine(JournalEntryCommand.RemoveLineCommand command) {
        log.info("Removing line: {} from journal entry: {}",
            command.getLineId(), command.getJournalEntryId());

        String tenantId = RequestContextHolder.getTenantId();

        JournalEntry entry = journalEntryRepository.findByJournalEntryIdAndTenantId(
            command.getJournalEntryId(), tenantId)
            .orElseThrow(() -> new NotFoundException("JournalEntry", command.getJournalEntryId()));

        if (!entry.canModify()) {
            throw new ValidationException("Can only modify draft journal entries");
        }

        if (entry.getLineCount() <= 2) {
            throw new ValidationException("Journal entry must have at least two lines");
        }

        entry.removeLine(command.getLineId());
        journalEntryRepository.save(entry);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "journalEntries", allEntries = true),
        @CacheEvict(value = "trialBalance", allEntries = true)
    })
    public void submitForApproval(JournalEntryCommand.SubmitForApprovalCommand command) {
        log.info("Submitting journal entry: {} for approval", command.getJournalEntryId());

        JournalEntry entry = journalEntryRepository.findByJournalEntryIdAndTenantId(
            command.getJournalEntryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("JournalEntry", command.getJournalEntryId()));

        entry.submitForApproval();
        journalEntryRepository.save(entry);
        publishEvents(entry);

        log.info("Submitted journal entry: {} for approval", command.getJournalEntryId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "journalEntries", allEntries = true),
        @CacheEvict(value = "trialBalance", allEntries = true)
    })
    public void approve(JournalEntryCommand.ApproveJournalEntryCommand command) {
        log.info("Approving journal entry: {} by: {}", command.getJournalEntryId(), command.getApprovedBy());

        JournalEntry entry = journalEntryRepository.findByJournalEntryIdAndTenantId(
            command.getJournalEntryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("JournalEntry", command.getJournalEntryId()));

        entry.approve(command.getApprovedBy());
        journalEntryRepository.save(entry);
        publishEvents(entry);

        log.info("Approved journal entry: {}", command.getJournalEntryId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "journalEntries", allEntries = true),
        @CacheEvict(value = "ledgerAccounts", allEntries = true),
        @CacheEvict(value = "trialBalance", allEntries = true)
    })
    public void post(JournalEntryCommand.PostJournalEntryCommand command) {
        log.info("Posting journal entry: {} by: {}", command.getJournalEntryId(), command.getPostedBy());

        JournalEntry entry = journalEntryRepository.findByJournalEntryIdAndTenantId(
            command.getJournalEntryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("JournalEntry", command.getJournalEntryId()));

        // Verify all accounts are active
        for (JournalEntry.JournalEntryLine line : entry.getLines()) {
            LedgerAccount account = ledgerAccountRepository
                .findByAccountIdAndTenantId(line.getAccountId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("LedgerAccount", line.getAccountId()));

            if (!account.allowsEntry()) {
                throw new ValidationException("Account " + account.getAccountNumber() +
                    " does not allow manual entries or is not active");
            }
        }

        entry.post(command.getPostedBy());
        journalEntryRepository.save(entry);

        // Create ledger transactions and update account balances
        createLedgerTransactions(entry);
        updateAccountBalances(entry);

        publishEvents(entry);

        log.info("Posted journal entry: {}", command.getJournalEntryId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "journalEntries", allEntries = true),
        @CacheEvict(value = "trialBalance", allEntries = true)
    })
    public JournalEntry reverse(JournalEntryCommand.ReverseJournalEntryCommand command) {
        log.info("Reversing journal entry: {} for tenant: {}",
            command.getJournalEntryId(), command.getTenantId());

        JournalEntry entry = journalEntryRepository.findByJournalEntryIdAndTenantId(
            command.getJournalEntryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("JournalEntry", command.getJournalEntryId()));

        JournalEntry reversingEntry = entry.reverse(
            command.getReversalReason(),
            command.getReversedBy(),
            command.getReversalDate()
        );

        // Generate entry number for reversing entry
        String entryNumber = journalEntryRepository.generateNextEntryNumber(command.getTenantId());
        reversingEntry.setEntryNumber(entryNumber);

        journalEntryRepository.save(entry);
        JournalEntry savedReversingEntry = journalEntryRepository.save(reversingEntry);

        // If original was posted, also post the reversing entry and update balances
        if (entry.getStatus() == JournalEntry.JournalEntryStatus.POSTED) {
            reversingEntry.post(command.getReversedBy());
            createLedgerTransactions(reversingEntry);
            updateAccountBalances(reversingEntry);
            journalEntryRepository.save(reversingEntry);
        }

        publishEvents(entry);
        publishEvents(reversingEntry);

        log.info("Reversed journal entry: {}", command.getJournalEntryId());
        return savedReversingEntry;
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "journalEntries", allEntries = true),
        @CacheEvict(value = "trialBalance", allEntries = true)
    })
    public void cancel(JournalEntryCommand.CancelJournalEntryCommand command) {
        log.info("Cancelling journal entry: {} for tenant: {}",
            command.getJournalEntryId(), command.getTenantId());

        JournalEntry entry = journalEntryRepository.findByJournalEntryIdAndTenantId(
            command.getJournalEntryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("JournalEntry", command.getJournalEntryId()));

        entry.cancel(command.getReason());
        journalEntryRepository.save(entry);
        publishEvents(entry);

        log.info("Cancelled journal entry: {}", command.getJournalEntryId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "journalEntries", allEntries = true),
        @CacheEvict(value = "trialBalance", allEntries = true)
    })
    public void delete(JournalEntryCommand.DeleteJournalEntryCommand command) {
        log.info("Deleting journal entry: {} for tenant: {}",
            command.getJournalEntryId(), command.getTenantId());

        JournalEntry entry = journalEntryRepository.findByJournalEntryIdAndTenantId(
            command.getJournalEntryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("JournalEntry", command.getJournalEntryId()));

        if (entry.getStatus() != JournalEntry.JournalEntryStatus.DRAFT) {
            throw new ValidationException("Can only delete draft journal entries");
        }

        journalEntryRepository.deleteByJournalEntryIdAndTenantId(
            command.getJournalEntryId(), command.getTenantId());

        log.info("Deleted journal entry: {}", command.getJournalEntryId());
    }

    private void validateLines(java.util.List<JournalEntryCommand.JournalEntryLineDto> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new ValidationException("Journal entry must have at least one line");
        }
        if (lines.size() < 2) {
            throw new ValidationException("Journal entry must have at least two lines for double-entry accounting");
        }

        BigDecimal totalDebit = lines.stream()
            .map(l -> l.getDebitAmount() != null ? l.getDebitAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredit = lines.stream()
            .map(l -> l.getCreditAmount() != null ? l.getCreditAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new ValidationException("Journal entry must balance (debits must equal credits)");
        }

        if (totalDebit.compareTo(BigDecimal.ZERO) == 0) {
            throw new ValidationException("Journal entry total cannot be zero");
        }
    }

    private LedgerAccount getAndValidateAccount(String tenantId, String accountId) {
        return ledgerAccountRepository.findByAccountIdAndTenantId(accountId, tenantId)
            .orElseThrow(() -> new NotFoundException("LedgerAccount", accountId));
    }

    private void createLedgerTransactions(JournalEntry entry) {
        java.util.List<LedgerTransaction> transactions = new ArrayList<>();

        for (JournalEntry.JournalEntryLine line : entry.getLines()) {
            LedgerAccount account = ledgerAccountRepository
                .findByAccountIdAndTenantId(line.getAccountId(), entry.getTenantId())
                .orElseThrow(() -> new NotFoundException("LedgerAccount", line.getAccountId()));

            LedgerTransaction transaction = LedgerTransaction.create(
                entry.getTenantId(),
                entry.getJournalEntryId(),
                entry.getEntryNumber(),
                entry.getEntryDate(),
                line.getAccountId(),
                account.getAccountNumber(),
                account.getAccountName(),
                account.getAccountType(),
                line.getDebitAmount(),
                line.getCreditAmount(),
                entry.getCurrency(),
                line.getDescription()
            );

            transaction.setPeriodId(entry.getPeriodId());
            transaction.setFiscalYear(entry.getFiscalYear());
            transaction.setFiscalPeriod(entry.getFiscalPeriod());
            transaction.setReference(entry.getReference());
            transaction.setSourceDocumentType(entry.getSourceDocumentType());
            transaction.setSourceDocumentId(entry.getSourceDocumentId());
            transaction.setCostCenter(line.getCostCenter());
            transaction.setDepartment(line.getDepartment());
            transaction.setProjectId(line.getProjectId());
            transaction.setTaskId(line.getTaskId());
            transaction.setCreatedByUserId(entry.getCreatedByUserId());
            transaction.setPostedByUserId(entry.getPostedByUserId());

            if (entry.getExchangeRate() != null) {
                transaction.convertToBaseCurrency(entry.getExchangeRate(), entry.getBaseCurrency());
            }

            transactions.add(transaction);
        }

        // Save transactions (would need transaction repository)
        // journalEntryRepository.saveTransactions(transactions);
    }

    private void updateAccountBalances(JournalEntry entry) {
        for (JournalEntry.JournalEntryLine line : entry.getLines()) {
            LedgerAccount account = ledgerAccountRepository
                .findByAccountIdAndTenantId(line.getAccountId(), entry.getTenantId())
                .orElseThrow(() -> new NotFoundException("LedgerAccount", line.getAccountId()));

            account.updateBalance(line.getDebitAmount(), line.getCreditAmount());
            ledgerAccountRepository.save(account);
        }
    }

    private void publishEvents(JournalEntry entry) {
        if (!entry.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(entry.getDomainEvents());
            entry.clearDomainEvents();
        }
    }
}
