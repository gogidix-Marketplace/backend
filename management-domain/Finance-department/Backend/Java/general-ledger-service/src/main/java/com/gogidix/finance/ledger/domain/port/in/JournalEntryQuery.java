package com.gogidix.finance.ledger.domain.port.in;

import java.time.LocalDate;
import java.util.List;

/**
 * Journal Entry Query (Input Port)
 * Defines query operations for journal entries
 */
public interface JournalEntryQuery {

    Object getById(String journalEntryId);

    Object getByEntryNumber(String tenantId, String entryNumber);

    List<?> getAllForTenant(String tenantId);

    List<?> getByStatus(String tenantId, String status);

    List<?> getByDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    List<?> getByAccount(String tenantId, String accountId, LocalDate startDate, LocalDate endDate);

    List<?> getByPeriod(String tenantId, Integer fiscalYear, Integer fiscalPeriod);

    Object getSummary(String tenantId, LocalDate startDate, LocalDate endDate);

    List<?> getPendingApproval(String tenantId);

    List<?> getPostedEntries(String tenantId, LocalDate startDate, LocalDate endDate);

    List<?> getBySourceDocument(String tenantId, String sourceDocumentType, String sourceDocumentId);

    List<?> getByBatch(String tenantId, String batchId);
}
