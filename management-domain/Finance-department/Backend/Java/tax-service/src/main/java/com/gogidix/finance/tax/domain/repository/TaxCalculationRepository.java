package com.gogidix.finance.tax.domain.repository;

import com.gogidix.finance.tax.domain.model.TaxCalculation;
import com.gogidix.finance.tax.domain.model.TaxRate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Tax Calculation Repository Interface (Port)
 * Defines the contract for tax calculation persistence operations
 */
public interface TaxCalculationRepository {

    TaxCalculation save(TaxCalculation calculation);

    List<TaxCalculation> saveAll(List<TaxCalculation> calculations);

    Optional<TaxCalculation> findById(String id);

    Optional<TaxCalculation> findByCalculationIdAndTenantId(String calculationId, String tenantId);

    List<TaxCalculation> findByTenantId(String tenantId);

    List<TaxCalculation> findByTenantIdAndTransactionId(String tenantId, String transactionId);

    List<TaxCalculation> findByTenantIdAndStatus(String tenantId, TaxCalculation.CalculationStatus status);

    List<TaxCalculation> findByTenantIdAndTransactionDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<TaxCalculation> findByTenantIdAndPeriod(String tenantId, YearMonth period,
                                                  TaxRate.Jurisdiction jurisdiction, TaxRate.TaxType taxType);

    List<TaxCalculation> findByTenantIdAndPeriodAndStatus(String tenantId, YearMonth period,
                                                         TaxRate.Jurisdiction jurisdiction,
                                                         TaxRate.TaxType taxType,
                                                         TaxCalculation.CalculationStatus status);

    List<TaxCalculation> findByTenantIdAndFilingId(String tenantId, String filingId);

    void deleteById(String id);

    void deleteByCalculationIdAndTenantId(String calculationId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, TaxCalculation.CalculationStatus status);

    List<TaxCalculation> findPendingCalculationsForFiling(String tenantId, YearMonth period,
                                                         TaxRate.Jurisdiction jurisdiction,
                                                         TaxRate.TaxType taxType);

    List<TaxCalculation> findReconcilableCalculations(String tenantId, YearMonth period,
                                                     TaxRate.Jurisdiction jurisdiction,
                                                     TaxRate.TaxType taxType);
}
