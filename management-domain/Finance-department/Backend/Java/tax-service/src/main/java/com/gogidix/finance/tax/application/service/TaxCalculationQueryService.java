package com.gogidix.finance.tax.application.service;

import com.gogidix.finance.tax.domain.model.TaxCalculation;
import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.repository.TaxCalculationRepository;
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
 * Tax Calculation Query Service
 * Handles all read operations for tax calculations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaxCalculationQueryService {

    private final TaxCalculationRepository taxCalculationRepository;

    public TaxCalculation getById(String calculationId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax calculation: {} for tenant: {}", calculationId, tenantId);

        return taxCalculationRepository.findByCalculationIdAndTenantId(calculationId, tenantId)
            .orElseThrow(() -> new NotFoundException("TaxCalculation", calculationId));
    }

    public List<TaxCalculation> getByTransactionId(String transactionId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax calculations for transaction: {} in tenant: {}", transactionId, tenantId);

        return taxCalculationRepository.findByTenantIdAndTransactionId(tenantId, transactionId);
    }

    public List<TaxCalculation> getByDateRange(LocalDate startDate, LocalDate endDate, String status) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax calculations for date range: {} to {} in tenant: {}",
            startDate, endDate, tenantId);

        TaxCalculation.CalculationStatus statusEnum = status != null ?
            TaxCalculation.CalculationStatus.valueOf(status) : null;

        return taxCalculationRepository.findByTenantIdAndTransactionDateBetween(
            tenantId, startDate, endDate);
    }

    public List<TaxCalculation> getByPeriod(YearMonth period, String jurisdiction, String taxType) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax calculations for period: {} in tenant: {}", period, tenantId);

        TaxRate.Jurisdiction jur = jurisdiction != null ? TaxRate.Jurisdiction.valueOf(jurisdiction) : null;
        TaxRate.TaxType type = taxType != null ? TaxRate.TaxType.valueOf(taxType) : null;

        return taxCalculationRepository.findByTenantIdAndPeriod(
            tenantId, period, jur, type);
    }

    public List<TaxCalculation> getByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax calculations by status: {} for tenant: {}", status, tenantId);

        TaxCalculation.CalculationStatus statusEnum = TaxCalculation.CalculationStatus.valueOf(status);
        return taxCalculationRepository.findByTenantIdAndStatus(tenantId, statusEnum);
    }

    public PageImpl<TaxCalculation> getByTransactionIdPaged(String transactionId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax calculations for transaction: {} (page: {}) in tenant: {}",
            transactionId, page, tenantId);

        List<TaxCalculation> calculations = taxCalculationRepository.findByTenantIdAndTransactionId(
            tenantId, transactionId);

        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), calculations.size());

        List<TaxCalculation> pageContent = calculations.subList(start, end);

        return new PageImpl<>(pageContent, pageRequest, calculations.size());
    }

    public PageImpl<TaxCalculation> getByDateRangePaged(LocalDate startDate, LocalDate endDate,
                                                       String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax calculations for date range: {} to {} (page: {}) in tenant: {}",
            startDate, endDate, page, tenantId);

        List<TaxCalculation> calculations = taxCalculationRepository.findByTenantIdAndTransactionDateBetween(
            tenantId, startDate, endDate);

        if (status != null) {
            TaxCalculation.CalculationStatus statusEnum = TaxCalculation.CalculationStatus.valueOf(status);
            calculations = calculations.stream()
                .filter(c -> c.getStatus() == statusEnum)
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), calculations.size());

        List<TaxCalculation> pageContent = calculations.subList(start, end);

        return new PageImpl<>(pageContent, pageRequest, calculations.size());
    }

    public TaxSummary getTaxSummary(LocalDate startDate, LocalDate endDate,
                                   String jurisdiction, String taxType, String currency) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax summary for tenant: {}", tenantId);

        List<TaxCalculation> calculations;

        if (startDate != null && endDate != null) {
            calculations = taxCalculationRepository.findByTenantIdAndTransactionDateBetween(
                tenantId, startDate, endDate);
        } else {
            calculations = taxCalculationRepository.findByTenantId(tenantId);
        }

        // Filter by jurisdiction and tax type if provided
        if (jurisdiction != null || taxType != null) {
            TaxRate.Jurisdiction jur = jurisdiction != null ? TaxRate.Jurisdiction.valueOf(jurisdiction) : null;
            TaxRate.TaxType type = taxType != null ? TaxRate.TaxType.valueOf(taxType) : null;

            final TaxRate.Jurisdiction finalJur = jur;
            final TaxRate.TaxType finalType = type;

            calculations = calculations.stream()
                .filter(c -> finalJur == null || c.getJurisdiction() == finalJur)
                .filter(c -> {
                    if (finalType == null) return true;
                    return c.getTaxBreakdown().stream()
                        .anyMatch(item -> item.getTaxType() == finalType);
                })
                .toList();
        }

        BigDecimal totalBaseAmount = calculations.stream()
            .map(TaxCalculation::getBaseAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTax = calculations.stream()
            .map(TaxCalculation::getTotalTax)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRecoverable = calculations.stream()
            .map(TaxCalculation::getTotalRecoverableTax)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalNonRecoverable = calculations.stream()
            .map(TaxCalculation::getTotalNonRecoverableTax)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        long calculatedCount = calculations.stream()
            .filter(c -> c.getStatus() == TaxCalculation.CalculationStatus.CALCULATED)
            .count();

        long verifiedCount = calculations.stream()
            .filter(c -> c.getStatus() == TaxCalculation.CalculationStatus.VERIFIED)
            .count();

        long appliedCount = calculations.stream()
            .filter(c -> c.getStatus() == TaxCalculation.CalculationStatus.APPLIED)
            .count();

        return new TaxSummary(
            calculations.size(),
            totalBaseAmount,
            totalTax,
            totalRecoverable,
            totalNonRecoverable,
            calculatedCount,
            verifiedCount,
            appliedCount
        );
    }

    public List<TaxCalculation> getReconcilableCalculations(YearMonth period, String jurisdiction, String taxType) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching reconcilable calculations for period: {} in tenant: {}", period, tenantId);

        TaxRate.Jurisdiction jur = jurisdiction != null ? TaxRate.Jurisdiction.valueOf(jurisdiction) : null;
        TaxRate.TaxType type = taxType != null ? TaxRate.TaxType.valueOf(taxType) : null;

        return taxCalculationRepository.findByTenantIdAndPeriodAndStatus(
            tenantId, period, jur, type, TaxCalculation.CalculationStatus.VERIFIED);
    }

    public TaxLiabilityReport getTaxLiabilityReport(YearMonth period, String jurisdiction, String taxType,
                                                   Boolean includePending, Boolean includeVerified) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching tax liability report for period: {} in tenant: {}", period, tenantId);

        TaxRate.Jurisdiction jur = jurisdiction != null ? TaxRate.Jurisdiction.valueOf(jurisdiction) : null;
        TaxRate.TaxType type = taxType != null ? TaxRate.TaxType.valueOf(taxType) : null;

        List<TaxCalculation> calculations = taxCalculationRepository.findByTenantIdAndPeriod(
            tenantId, period, jur, type);

        // Filter by status based on parameters
        if (includePending != null && !includePending) {
            calculations = calculations.stream()
                .filter(c -> c.getStatus() != TaxCalculation.CalculationStatus.PENDING)
                .toList();
        }

        if (includeVerified != null && !includeVerified) {
            calculations = calculations.stream()
                .filter(c -> c.getStatus() != TaxCalculation.CalculationStatus.VERIFIED)
                .toList();
        }

        BigDecimal totalLiability = calculations.stream()
            .filter(c -> c.getStatus() == TaxCalculation.CalculationStatus.VERIFIED ||
                       c.getStatus() == TaxCalculation.CalculationStatus.APPLIED)
            .map(TaxCalculation::getTotalNonRecoverableTax)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pendingLiability = calculations.stream()
            .filter(c -> c.getStatus() == TaxCalculation.CalculationStatus.CALCULATED ||
                       c.getStatus() == TaxCalculation.CalculationStatus.PENDING)
            .map(TaxCalculation::getTotalNonRecoverableTax)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TaxLiabilityReport(
            period,
            calculations.size(),
            totalLiability,
            pendingLiability,
            totalLiability.add(pendingLiability)
        );
    }

    public List<TaxCalculation> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return taxCalculationRepository.findByTenantId(tenantId);
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return taxCalculationRepository.countByTenantId(tenantId);
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        TaxCalculation.CalculationStatus statusEnum = TaxCalculation.CalculationStatus.valueOf(status);
        return taxCalculationRepository.countByTenantIdAndStatus(tenantId, statusEnum);
    }

    public record TaxSummary(
        long totalCount,
        BigDecimal totalBaseAmount,
        BigDecimal totalTax,
        BigDecimal totalRecoverable,
        BigDecimal totalNonRecoverable,
        long calculatedCount,
        long verifiedCount,
        long appliedCount
    ) {}

    public record TaxLiabilityReport(
        YearMonth period,
        long calculationCount,
        BigDecimal confirmedLiability,
        BigDecimal pendingLiability,
        BigDecimal totalLiability
    ) {}
}
