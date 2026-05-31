package com.gogidix.finance.generalledger.domain.repository;

import com.gogidix.finance.ledger.domain.model.JournalEntry;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Journal Entry Repository Interface
 * Defines the contract for journal entry persistence operations
 */
public interface JournalEntryRepository {

    JournalEntry save(JournalEntry journalEntry);

    List<JournalEntry> saveAll(List<JournalEntry> journalEntries);

    Optional<JournalEntry> findById(String id);

    Optional<JournalEntry> findByJournalEntryIdAndTenantId(String journalEntryId, String tenantId);

    Optional<JournalEntry> findByEntryNumberAndTenantId(String entryNumber, String tenantId);

    List<JournalEntry> findByTenantId(String tenantId);

    List<JournalEntry> findByTenantIdAndStatus(String tenantId, JournalEntry.JournalEntryStatus status);

    List<JournalEntry> findByTenantIdAndEntryDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<JournalEntry> findByTenantIdAndAccountId(String tenantId, String accountId, LocalDate startDate, LocalDate endDate);

    List<JournalEntry> findByTenantIdAndFiscalPeriod(String tenantId, Integer fiscalYear, Integer fiscalPeriod);

    List<JournalEntry> findPendingApprovalByTenantId(String tenantId);

    List<JournalEntry> findPostedByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    List<JournalEntry> findByTenantIdAndSourceDocument(String tenantId, String sourceDocumentType, String sourceDocumentId);

    List<JournalEntry> findByTenantIdAndBatchId(String tenantId, String batchId);

    boolean existsByJournalEntryIdAndTenantId(String journalEntryId, String tenantId);

    boolean existsByEntryNumberAndTenantId(String entryNumber, String tenantId);

    void deleteById(String id);

    void deleteByJournalEntryIdAndTenantId(String journalEntryId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, JournalEntry.JournalEntryStatus status);

    String generateNextEntryNumber(String tenantId);
}
