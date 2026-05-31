package com.gogidix.finance.tax.application.service;

import com.gogidix.finance.tax.domain.model.TaxFiling;
import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.repository.TaxFilingRepository;
import com.gogidix.finance.tax.shared.exception.NotFoundException;
import com.gogidix.finance.tax.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Tax Filing Query Service
 * Handles all read operations for tax filings
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaxFilingQueryService {

    private final TaxFilingRepository taxFilingRepository;

    public TaxFiling getById(String filingId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax filing: {} for tenant: {}", filingId, tenantId);

        return taxFilingRepository.findByFilingIdAndTenantId(filingId, tenantId)
            .orElseThrow(() -> new NotFoundException("TaxFiling", filingId));
    }

    public List<TaxFiling> getByPeriod(YearMonth period, String jurisdiction, String taxType) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax filings for period: {} in tenant: {}", period, tenantId);

        TaxRate.Jurisdiction jur = jurisdiction != null ? TaxRate.Jurisdiction.valueOf(jurisdiction) : null;
        TaxRate.TaxType type = taxType != null ? TaxRate.TaxType.valueOf(taxType) : null;

        return taxFilingRepository.findByTenantIdAndFilingPeriod(tenantId, period, jur, type);
    }

    public List<TaxFiling> getByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax filings by status: {} for tenant: {}", status, tenantId);

        TaxFiling.FilingStatus statusEnum = TaxFiling.FilingStatus.valueOf(status);
        return taxFilingRepository.findByTenantIdAndStatus(tenantId, statusEnum);
    }

    public List<TaxFiling> getPendingFilings(LocalDate dueBefore, String jurisdiction) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching pending tax filings due before: {} in tenant: {}", dueBefore, tenantId);

        TaxRate.Jurisdiction jur = jurisdiction != null ? TaxRate.Jurisdiction.valueOf(jurisdiction) : null;
        LocalDate dueDate = dueBefore != null ? dueBefore : LocalDate.now();

        return taxFilingRepository.findByTenantIdAndDueBefore(tenantId, dueDate, jur);
    }

    public List<TaxFiling> getOverdueFilings() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching overdue tax filings for tenant: {}", tenantId);

        return taxFilingRepository.findByTenantIdAndStatus(tenantId, TaxFiling.FilingStatus.OVERDUE);
    }

    public List<TaxFiling> getByDateRange(YearMonth startPeriod, YearMonth endPeriod, String jurisdiction, String taxType) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax filings for period range: {} to {} in tenant: {}",
            startPeriod, endPeriod, tenantId);

        TaxRate.Jurisdiction jur = jurisdiction != null ? TaxRate.Jurisdiction.valueOf(jurisdiction) : null;
        TaxRate.TaxType type = taxType != null ? TaxRate.TaxType.valueOf(taxType) : null;

        return taxFilingRepository.findByTenantIdAndFilingPeriodBetween(
            tenantId, startPeriod, endPeriod, jur, type);
    }

    public List<TaxFiling> getFilingHistory(String jurisdiction, String taxType, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching filing history for tenant: {}", tenantId);

        TaxRate.Jurisdiction jur = jurisdiction != null ? TaxRate.Jurisdiction.valueOf(jurisdiction) : null;
        TaxRate.TaxType type = taxType != null ? TaxRate.TaxType.valueOf(taxType) : null;

        List<TaxFiling> filings = taxFilingRepository.findByTenantId(tenantId, jur, type);

        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), filings.size());

        return filings.subList(start, end);
    }

    public FilingSummary getFilingSummary(YearMonth period, String jurisdiction, String taxType) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching filing summary for period: {} in tenant: {}", period, tenantId);

        TaxRate.Jurisdiction jur = jurisdiction != null ? TaxRate.Jurisdiction.valueOf(jurisdiction) : null;
        TaxRate.TaxType type = taxType != null ? TaxRate.TaxType.valueOf(taxType) : null;

        List<TaxFiling> filings = taxFilingRepository.findByTenantIdAndFilingPeriod(tenantId, period, jur, type);

        if (filings.isEmpty()) {
            return new FilingSummary(period, 0L, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, 0L, 0L);
        }

        long totalFilings = filings.size();
        long pendingCount = filings.stream()
            .filter(f -> f.getStatus() == TaxFiling.FilingStatus.DRAFT ||
                       f.getStatus() == TaxFiling.FilingStatus.PENDING_REVIEW)
            .count();
        long submittedCount = filings.stream()
            .filter(f -> f.getStatus() == TaxFiling.FilingStatus.SUBMITTED ||
                       f.getStatus() == TaxFiling.FilingStatus.PROCESSING)
            .count();
        long paidCount = filings.stream()
            .filter(f -> f.getStatus() == TaxFiling.FilingStatus.PAID)
            .count();

        BigDecimal totalTaxDue = filings.stream()
            .map(TaxFiling::getTaxDue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTaxRefund = filings.stream()
            .map(TaxFiling::getTaxRefund)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netAmount = filings.stream()
            .map(TaxFiling::getNetAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new FilingSummary(
            period,
            totalFilings,
            totalTaxDue,
            totalTaxRefund,
            netAmount,
            netAmount.compareTo(BigDecimal.ZERO) > 0 ? netAmount : BigDecimal.ZERO,
            pendingCount,
            paidCount
        );
    }

    public PageImpl<TaxFiling> getByPeriodPaged(YearMonth period, String jurisdiction, String taxType,
                                               int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax filings for period: {} (page: {}) in tenant: {}", period, page, tenantId);

        TaxRate.Jurisdiction jur = jurisdiction != null ? TaxRate.Jurisdiction.valueOf(jurisdiction) : null;
        TaxRate.TaxType type = taxType != null ? TaxRate.TaxType.valueOf(taxType) : null;

        List<TaxFiling> filings = taxFilingRepository.findByTenantIdAndFilingPeriod(tenantId, period, jur, type);

        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), filings.size());

        List<TaxFiling> pageContent = filings.subList(start, end);

        return new PageImpl<>(pageContent, pageRequest, filings.size());
    }

    public PageImpl<TaxFiling> getByStatusPaged(String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax filings by status: {} (page: {}) in tenant: {}", status, page, tenantId);

        TaxFiling.FilingStatus statusEnum = TaxFiling.FilingStatus.valueOf(status);
        List<TaxFiling> filings = taxFilingRepository.findByTenantIdAndStatus(tenantId, statusEnum);

        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), filings.size());

        List<TaxFiling> pageContent = filings.subList(start, end);

        return new PageImpl<>(pageContent, pageRequest, filings.size());
    }

    public List<TaxFiling> getUpcomingFilings(int daysAhead) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching upcoming tax filings within {} days for tenant: {}", daysAhead, tenantId);

        LocalDate dueBefore = LocalDate.now().plusDays(daysAhead);
        return taxFilingRepository.findByTenantIdAndDueBefore(tenantId, dueBefore, null);
    }

    public List<TaxFiling> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return taxFilingRepository.findByTenantId(tenantId, null, null);
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return taxFilingRepository.countByTenantId(tenantId);
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        TaxFiling.FilingStatus statusEnum = TaxFiling.FilingStatus.valueOf(status);
        return taxFilingRepository.countByTenantIdAndStatus(tenantId, statusEnum);
    }

    public record FilingSummary(
        YearMonth period,
        long totalFilings,
        BigDecimal totalTaxDue,
        BigDecimal totalTaxRefund,
        BigDecimal netAmount,
        BigDecimal netPayable,
        long pendingCount,
        long paidCount
    ) {}
}
