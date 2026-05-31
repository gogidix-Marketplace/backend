package com.gogidix.finance.generalledger.application.service;

import com.gogidix.finance.generalledger.domain.repository.JournalEntryRepository;
import com.gogidix.finance.generalledger.shared.exception.NotFoundException;
import com.gogidix.finance.generalledger.shared.requestcontext.RequestContextHolder;
import com.gogidix.finance.ledger.domain.model.JournalEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Journal Entry Query Service
 * Handles all read operations for journal entries
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JournalEntryQueryService {

    private final JournalEntryRepository journalEntryRepository;

    @Cacheable(value = "journalEntries", key = "#journalEntryId")
    public JournalEntry getById(String journalEntryId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching journal entry: {} for tenant: {}", journalEntryId, tenantId);

        return journalEntryRepository.findByJournalEntryIdAndTenantId(journalEntryId, tenantId)
            .orElseThrow(() -> new NotFoundException("JournalEntry", journalEntryId));
    }

    public JournalEntry getByEntryNumber(String entryNumber) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching journal entry by number: {} for tenant: {}", entryNumber, tenantId);

        return journalEntryRepository.findByEntryNumberAndTenantId(entryNumber, tenantId)
            .orElseThrow(() -> new NotFoundException("JournalEntry", entryNumber));
    }

    @Cacheable(value = "journalEntries", key = "'all'")
    public List<JournalEntry> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all journal entries for tenant: {}", tenantId);

        return journalEntryRepository.findByTenantId(tenantId);
    }

    public List<JournalEntry> getByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching journal entries by status: {} for tenant: {}", status, tenantId);

        JournalEntry.JournalEntryStatus statusEnum = JournalEntry.JournalEntryStatus.valueOf(status);
        return journalEntryRepository.findByTenantIdAndStatus(tenantId, statusEnum);
    }

    public Page<JournalEntry> getByDateRange(LocalDate startDate, LocalDate endDate,
                                              int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching journal entries for date range: {} to {} in tenant: {}",
            startDate, endDate, tenantId);

        List<JournalEntry> entries = journalEntryRepository.findByTenantIdAndEntryDateBetween(
            tenantId, startDate, endDate);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "entryDate"));
        return new PageImpl<>(entries, pageRequest, entries.size());
    }

    public List<JournalEntry> getByAccount(String accountId, LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching journal entries for account: {} in tenant: {}", accountId, tenantId);

        return journalEntryRepository.findByTenantIdAndAccountId(tenantId, accountId, startDate, endDate);
    }

    public List<JournalEntry> getByPeriod(Integer fiscalYear, Integer fiscalPeriod) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching journal entries for period: {}-{} in tenant: {}",
            fiscalYear, fiscalPeriod, tenantId);

        return journalEntryRepository.findByTenantIdAndFiscalPeriod(tenantId, fiscalYear, fiscalPeriod);
    }

    public List<JournalEntry> getPendingApproval() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching pending approval journal entries for tenant: {}", tenantId);

        return journalEntryRepository.findPendingApprovalByTenantId(tenantId);
    }

    public List<JournalEntry> getPostedEntries(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching posted entries for date range: {} to {} in tenant: {}",
            startDate, endDate, tenantId);

        return journalEntryRepository.findPostedByTenantIdAndDateRange(tenantId, startDate, endDate);
    }

    public List<JournalEntry> getBySourceDocument(String sourceDocumentType, String sourceDocumentId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching journal entries for source document: {} - {} in tenant: {}",
            sourceDocumentType, sourceDocumentId, tenantId);

        return journalEntryRepository.findByTenantIdAndSourceDocument(
            tenantId, sourceDocumentType, sourceDocumentId);
    }

    public List<JournalEntry> getByBatch(String batchId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching journal entries for batch: {} in tenant: {}", batchId, tenantId);

        return journalEntryRepository.findByTenantIdAndBatchId(tenantId, batchId);
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return journalEntryRepository.countByTenantId(tenantId);
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        JournalEntry.JournalEntryStatus statusEnum = JournalEntry.JournalEntryStatus.valueOf(status);
        return journalEntryRepository.countByTenantIdAndStatus(tenantId, statusEnum);
    }

    public JournalEntrySummary getSummary(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching journal entry summary for tenant: {} from {} to {}",
            tenantId, startDate, endDate);

        List<JournalEntry> entries = startDate != null && endDate != null
            ? journalEntryRepository.findByTenantIdAndEntryDateBetween(tenantId, startDate, endDate)
            : journalEntryRepository.findByTenantId(tenantId);

        long totalCount = entries.size();
        long draftCount = entries.stream().filter(e -> e.getStatus() == JournalEntry.JournalEntryStatus.DRAFT).count();
        long pendingCount = entries.stream().filter(e -> e.getStatus() == JournalEntry.JournalEntryStatus.PENDING_APPROVAL).count();
        long approvedCount = entries.stream().filter(e -> e.getStatus() == JournalEntry.JournalEntryStatus.APPROVED).count();
        long postedCount = entries.stream().filter(e -> e.getStatus() == JournalEntry.JournalEntryStatus.POSTED).count();

        BigDecimal totalDebit = entries.stream()
            .filter(e -> e.getStatus() == JournalEntry.JournalEntryStatus.POSTED)
            .map(JournalEntry::getTotalDebit)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredit = entries.stream()
            .filter(e -> e.getStatus() == JournalEntry.JournalEntryStatus.POSTED)
            .map(JournalEntry::getTotalCredit)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return JournalEntrySummary.builder()
            .totalCount(totalCount)
            .draftCount(draftCount)
            .pendingApprovalCount(pendingCount)
            .approvedCount(approvedCount)
            .postedCount(postedCount)
            .totalDebit(totalDebit)
            .totalCredit(totalCredit)
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }

    @lombok.Builder
    public record JournalEntrySummary(
        long totalCount,
        long draftCount,
        long pendingApprovalCount,
        long approvedCount,
        long postedCount,
        BigDecimal totalDebit,
        BigDecimal totalCredit,
        LocalDate startDate,
        LocalDate endDate
    ) {}
}
