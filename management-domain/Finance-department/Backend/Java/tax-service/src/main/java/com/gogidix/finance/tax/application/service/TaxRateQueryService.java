package com.gogidix.finance.tax.application.service;

import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.repository.TaxRateRepository;
import com.gogidix.finance.tax.shared.exception.NotFoundException;
import com.gogidix.finance.tax.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Tax Rate Query Service
 * Handles all read operations for tax rates
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaxRateQueryService {

    private final TaxRateRepository taxRateRepository;

    public TaxRate getById(String taxRateId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax rate: {} for tenant: {}", taxRateId, tenantId);

        return taxRateRepository.findByTaxRateIdAndTenantId(taxRateId, tenantId)
            .orElseThrow(() -> new NotFoundException("TaxRate", taxRateId));
    }

    public List<TaxRate> getByJurisdiction(String jurisdiction) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax rates for jurisdiction: {} in tenant: {}", jurisdiction, tenantId);

        TaxRate.Jurisdiction jur = TaxRate.Jurisdiction.valueOf(jurisdiction);
        return taxRateRepository.findByTenantIdAndJurisdiction(tenantId, jur);
    }

    public List<TaxRate> getByTaxType(String taxType) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax rates for type: {} in tenant: {}", taxType, tenantId);

        TaxRate.TaxType type = TaxRate.TaxType.valueOf(taxType);
        return taxRateRepository.findByTenantIdAndTaxType(tenantId, type);
    }

    public List<TaxRate> getByJurisdictionAndType(String jurisdiction, String taxType) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax rates for jurisdiction: {} and type: {} in tenant: {}",
            jurisdiction, taxType, tenantId);

        TaxRate.Jurisdiction jur = TaxRate.Jurisdiction.valueOf(jurisdiction);
        TaxRate.TaxType type = TaxRate.TaxType.valueOf(taxType);
        return taxRateRepository.findByTenantIdAndJurisdictionAndTaxType(tenantId, jur, type);
    }

    public TaxRate getEffectiveRate(String jurisdiction, String taxType, String taxCode, LocalDate date) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching effective tax rate for jurisdiction: {}, type: {}, date: {}",
            jurisdiction, taxType, date);

        TaxRate.Jurisdiction jur = TaxRate.Jurisdiction.valueOf(jurisdiction);
        TaxRate.TaxType type = TaxRate.TaxType.valueOf(taxType);
        LocalDate queryDate = date != null ? date : LocalDate.now();

        return taxRateRepository.findEffectiveRateForDate(tenantId, jur, type, taxCode, queryDate)
            .orElseThrow(() -> new NotFoundException("TaxRate", "No effective rate found for the given criteria"));
    }

    public List<TaxRate> getByDateRange(LocalDate startDate, LocalDate endDate, String jurisdiction, String taxType) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax rates for date range: {} to {} in tenant: {}",
            startDate, endDate, tenantId);

        return taxRateRepository.findByTenantIdAndEffectiveDateBetween(tenantId, startDate, endDate);
    }

    public List<TaxRate> getByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax rates by status: {} for tenant: {}", status, tenantId);

        TaxRate.TaxRateStatus statusEnum = TaxRate.TaxRateStatus.valueOf(status);
        return taxRateRepository.findByTenantIdAndStatus(tenantId, statusEnum);
    }

    public List<TaxRate> getActiveRatesForJurisdiction(String jurisdiction) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching active tax rates for jurisdiction: {} in tenant: {}", jurisdiction, tenantId);

        TaxRate.Jurisdiction jur = TaxRate.Jurisdiction.valueOf(jurisdiction);
        return taxRateRepository.findActiveRatesForJurisdiction(tenantId, jur);
    }

    public List<TaxRate> getActiveRatesForJurisdictionAndType(String jurisdiction, String taxType) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching active tax rates for jurisdiction: {} and type: {} in tenant: {}",
            jurisdiction, taxType, tenantId);

        TaxRate.Jurisdiction jur = TaxRate.Jurisdiction.valueOf(jurisdiction);
        TaxRate.TaxType type = TaxRate.TaxType.valueOf(taxType);
        return taxRateRepository.findActiveRatesForJurisdictionAndType(tenantId, jur, type);
    }

    public List<TaxRate> getByTaxCode(String taxCode) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax rates by tax code: {} for tenant: {}", taxCode, tenantId);

        return taxRateRepository.findByTaxCodeAndTenantId(taxCode, tenantId);
    }

    public List<TaxRate> getVersionsByTaxCode(String taxCode) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax rate versions by tax code: {} for tenant: {}", taxCode, tenantId);

        return taxRateRepository.findVersionsByTaxCode(tenantId, taxCode);
    }

    public List<TaxRate> getExpiringBetween(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax rates expiring between: {} and {} in tenant: {}",
            startDate, endDate, tenantId);

        return taxRateRepository.findExpiringBetween(tenantId, startDate, endDate);
    }

    public List<TaxRate> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all tax rates for tenant: {}", tenantId);

        return taxRateRepository.findByTenantId(tenantId);
    }

    public PageImpl<TaxRate> search(String searchTerm, String jurisdiction, String taxType,
                                    String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Searching tax rates for tenant: {} with term: {}", tenantId, searchTerm);

        // Get all rates for tenant and filter
        List<TaxRate> allRates = taxRateRepository.findByTenantId(tenantId);

        List<TaxRate> filtered = allRates.stream()
            .filter(rate -> {
                if (searchTerm != null && !searchTerm.isBlank()) {
                    boolean matchesCode = rate.getTaxCode() != null &&
                        rate.getTaxCode().toLowerCase().contains(searchTerm.toLowerCase());
                    boolean matchesDesc = rate.getDescription() != null &&
                        rate.getDescription().toLowerCase().contains(searchTerm.toLowerCase());
                    if (!matchesCode && !matchesDesc) return false;
                }
                if (jurisdiction != null && !jurisdiction.isBlank() &&
                    rate.getJurisdiction().name().equalsIgnoreCase(jurisdiction)) {
                    return false;
                }
                if (taxType != null && !taxType.isBlank() &&
                    rate.getTaxType().name().equalsIgnoreCase(taxType)) {
                    return false;
                }
                if (status != null && !status.isBlank() &&
                    rate.getStatus().name().equalsIgnoreCase(status)) {
                    return false;
                }
                return true;
            })
            .toList();

        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), filtered.size());

        List<TaxRate> pageContent = filtered.subList(start, end);

        return new PageImpl<>(pageContent, pageRequest, filtered.size());
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return taxRateRepository.countByTenantId(tenantId);
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        TaxRate.TaxRateStatus statusEnum = TaxRate.TaxRateStatus.valueOf(status);
        return taxRateRepository.countByTenantIdAndStatus(tenantId, statusEnum);
    }

    public record TaxRateSummary(
        long totalCount,
        long activeCount,
        long draftCount,
        long expiredCount
    ) {}

    public TaxRateSummary getSummary() {
        String tenantId = RequestContextHolder.getTenantId();

        long total = taxRateRepository.countByTenantId(tenantId);
        long active = taxRateRepository.countByTenantIdAndStatus(tenantId, TaxRate.TaxRateStatus.ACTIVE);
        long draft = taxRateRepository.countByTenantIdAndStatus(tenantId, TaxRate.TaxRateStatus.DRAFT);
        long expired = taxRateRepository.countByTenantIdAndStatus(tenantId, TaxRate.TaxRateStatus.EXPIRED);

        return new TaxRateSummary(total, active, draft, expired);
    }
}
